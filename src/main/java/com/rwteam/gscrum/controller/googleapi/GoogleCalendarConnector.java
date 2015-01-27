package com.rwteam.gscrum.controller.googleapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.rwteam.gscrum.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by wrabel on 12/1/2014.
 */
public class GoogleCalendarConnector {
    private static final String APPLICATION_NAME = "gscrumplugin";
    private static final String KEYSTORE_DIR_PATH = System.getProperty("user.home") + "/.gscrum_store/";
    private static final String CALENDAR_KEYSTORE_SUBDIR_PATH = "calendar_store";
    private static final String TASKS_KEYSTORE_SUBDIR_PATH = "tasks_store";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static GoogleCalendarConnector INSTANCE = new GoogleCalendarConnector();
    private static HttpTransport httpTransport;
    private static FileDataStoreFactory calendarDataStoreFactory;
    private static FileDataStoreFactory tasksDataStoreFactory;
    private static com.google.api.services.calendar.Calendar calendarClient;
    private static Tasks tasksClient;
    private final Logger logger = new Logger(this.getClass());

    private GoogleCalendarConnector() {
    }

    public static GoogleCalendarConnector getInstance() {
        return INSTANCE;
    }

    private static Credential authorize() throws Exception {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleCalendarConnector.class.getResourceAsStream("/client_secrets.json")));


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(CalendarScopes.CALENDAR))
                .setDataStoreFactory(calendarDataStoreFactory)
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static Credential authorizeTasks() throws Exception {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleCalendarConnector.class.getResourceAsStream("/client_secrets.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(TasksScopes.TASKS))
                .setDataStoreFactory(tasksDataStoreFactory)
                .build();

        flow.getClientId();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static void addEvent(Calendar calendar) throws IOException {
        Event event = newEvent();
        Event result = calendarClient.events().insert(calendar.getId(), event).execute();
    }

    private static Event newEvent() {
        Event event = new Event();
        event.setSummary("New Event");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));
        return event;
    }

    public static String[] getProfiles() {
        File file = new File(KEYSTORE_DIR_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        String[] fileList = file.list();
        return fileList;
    }

    public static void addNewProfile(String profileName) {
        File newProfileDir = new File(KEYSTORE_DIR_PATH + profileName);
        newProfileDir.mkdir();
    }

    public static void deleteProfile(String profileName) throws IOException {
        File profileDir = new File(KEYSTORE_DIR_PATH + profileName);
        if (profileDir.exists()) {
            org.apache.commons.io.FileUtils.deleteDirectory(profileDir);
        }
    }

    public List<Task> getTasks() {
        System.out.println("Get tasks");
        List<Task> tasks = new ArrayList<Task>();
        try {
            tasks = tasksClient.tasks().list("@default").execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public CalendarList getCalendars() throws IOException {
        return calendarClient.calendarList().list().execute();
    }

    public List<Event> getEventsForCalendarID(String calendarID) throws IOException {
        return calendarClient.events().list(calendarID).execute().getItems();
    }

    public String connect(String profileName) throws Exception {
        logger.log("Connecting... ");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        logger.log("Keystore dir: " + KEYSTORE_DIR_PATH);

        calendarDataStoreFactory = new FileDataStoreFactory(new File(KEYSTORE_DIR_PATH + profileName + "/" + CALENDAR_KEYSTORE_SUBDIR_PATH));
        Credential credential = authorize();
        tasksDataStoreFactory = new FileDataStoreFactory(new File(KEYSTORE_DIR_PATH + profileName + "/" + TASKS_KEYSTORE_SUBDIR_PATH));
        Credential credentialTasks = authorizeTasks();

        calendarClient = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        tasksClient = new Tasks.Builder(httpTransport, JSON_FACTORY, credentialTasks).setApplicationName(APPLICATION_NAME).build();

        return getMainCalendarName();
    }

    private String getMainCalendarName() throws IOException {
        List<CalendarListEntry> items = calendarClient.calendarList().list().execute().getItems();
        if (!items.isEmpty()) {
            CalendarListEntry calendarListEntry = items.get(0);
            if (calendarListEntry != null) {
                return calendarListEntry.getId();
            }
        }
        return null;
    }

    public void saveTask(Task task) {
        if (task != null) {
            try {
                Task result = tasksClient.tasks().insert("@default", task).execute();
                logger.log("Saved task " + result.getId());
            } catch (IOException e) {
                logger.logError(e);
            }
        }
    }
}
