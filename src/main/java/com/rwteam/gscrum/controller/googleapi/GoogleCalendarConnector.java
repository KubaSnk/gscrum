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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by wrabel on 12/1/2014.
 */
public class GoogleCalendarConnector {

    private static final String APPLICATION_NAME = "gscrumplugin";
    private static final java.io.File CALENDAR_DATA_STORE_DIR = new java.io.File("D:/userdata/wrabel/.store/calendar_sample");
    private static final java.io.File TASKS_DATA_STORE_DIR = new java.io.File("D:/userdata/wrabel/.store/tasks_sample");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static GoogleCalendarConnector INSTANCE = new GoogleCalendarConnector();
    private static FileDataStoreFactory dataStoreFactory;
    private static FileDataStoreFactory tasksDataStoreFactory;
    private static HttpTransport httpTransport;
    private static com.google.api.services.calendar.Calendar client;
    Tasks clientTasks;

    private GoogleCalendarConnector() {

    }

    public static GoogleCalendarConnector getInstance() {
        return INSTANCE;
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleCalendarConnector.class.getResourceAsStream("/client_secrets.json")));

//        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
//            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
//                    + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
//            System.exit(1);
//        }
        // set up authorization code flow


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(CalendarScopes.CALENDAR))
                .setDataStoreFactory(dataStoreFactory)
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static Credential authorizeTasks() throws Exception {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(GoogleCalendarConnector.class.getResourceAsStream("/client_secrets.json")));

//        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
//            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
//                    + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
//            System.exit(1);
//        }
        // set up authorization code flow


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(TasksScopes.TASKS))
                .setDataStoreFactory(tasksDataStoreFactory)
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) {
        try {
            GoogleCalendarConnector.getInstance().connect();
            GoogleCalendarConnector.getInstance().showCalendars();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEvent(Calendar calendar) throws IOException {
        Event event = newEvent();
        Event result = client.events().insert(calendar.getId(), event).execute();
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


    static void display(CalendarList feed) throws IOException {
        if (feed.getItems() != null) {
            for (CalendarListEntry entry : feed.getItems()) {

                System.out.println();
                System.out.println("-----------------------------------------------");
                display(entry);
                List<Event> items = client.events().list(entry.getId()).execute().getItems();
                for (Event ev : items) {
                    System.out.println(ev);
                }
//                items.stream().forEach(e-> System.out.println(e.toString()));
            }
        }
    }

    static void display(CalendarListEntry entry) {
        System.out.println("ID: " + entry.getId());
        System.out.println("Summary: " + entry.getSummary());
        if (entry.getDescription() != null) {
            System.out.println("Description: " + entry.getDescription());
        }
    }

    private static void showEvents(Calendar calendar) throws IOException {
        System.out.println("Show Events");
        Events feed = client.events().list(calendar.getId()).execute();
        display(feed);
    }

    static void display(Event event) {
        if (event.getStart() != null) {
            System.out.println("Start Time: " + event.getStart());
        }
        if (event.getEnd() != null) {
            System.out.println("End Time: " + event.getEnd());
        }
    }

    static void display(Events feed) {
        if (feed.getItems() != null) {
            for (Event entry : feed.getItems()) {
                System.out.println();
                System.out.println("-----------------------------------------------");
                display(entry);
            }
        }
    }

    public void showCalendars() throws IOException {
        System.out.println("Show Calendars");
        CalendarList feed = client.calendarList().list().execute();

        display(feed);
    }

    public List<Task> getTasks() {
        System.out.println("Get tasks");
        List<Task> tasks = new ArrayList<Task>();
        try {
            tasks = clientTasks.tasks().list("@default").execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
//                    clientTasks.tasks().list("@default").setFields("items/title").execute().getItems();
        return tasks;


    }


    public CalendarList getCalendars() throws IOException {
        return client.calendarList().list().execute();
    }

    public List<Event> getEventsForCalendarID(String calendarID) throws IOException {
        return client.events().list(calendarID).execute().getItems();
    }

    public void connect() throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(CALENDAR_DATA_STORE_DIR);
        Credential credential = authorize();
        tasksDataStoreFactory = new FileDataStoreFactory(TASKS_DATA_STORE_DIR);
        Credential credentialTasks = authorizeTasks();

        client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

        clientTasks = new Tasks.Builder(httpTransport, JSON_FACTORY, credentialTasks).setApplicationName(APPLICATION_NAME).build();
    }

    public void saveTask(Task task) {
        try {

            System.out.println("Pushing task");
            System.out.println(task.toPrettyString());
//            clientTasks.tasklists().insert(new TaskList());
//            clientTasks.tasks().li
            Task result = clientTasks.tasks().insert("@default", task).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
