package com.rwteam.gscrum.controller;

import com.google.common.base.CharMatcher;
import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import com.rwteam.gscrum.utils.Logger;
import com.rwteam.gscrum.view.GSMainWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by wrabel on 1/10/2015.
 */
public class GSMainWindowController {
    private final Logger logger = new Logger(this.getClass());
    private GSMainWindow view;
    private boolean userLogged;
    private DataProvider dataProvider;
    private String currentCalendarId;

    public GSMainWindowController(GSMainWindow gsMainWindow) {
        this.view = gsMainWindow;
        this.dataProvider = new DataProvider();
    }

    public void login(String profileName) {
        if (!checkProfileName(profileName)) {
            view.displayErrorDialog("Incorrect profile name!");
            return;
        }

        System.out.println("Button login handler");
        try {
            GoogleCalendarConnector.getProfiles();
            String userName = GoogleCalendarConnector.getInstance().connect(profileName);
            view.setStatus("Successfully logged as " + userName);
            view.populateCalendarComboBox(GoogleCalendarConnector.getInstance().getCalendars().getItems());
            dataProvider = new DataProvider();
            dataProvider.refreshTasksInfo();
            setUserLogged(true);
        } catch (Exception e1) {
            view.setStatus("Error while logging");
            e1.printStackTrace();
            logout();
        }
    }

    public void logout() {
        setUserLogged(false);
    }

    public DefaultListModel<UserStory> loadCalendarsInfo(String currentCalendarId) {
        this.currentCalendarId = currentCalendarId;
        DefaultListModel defaultListModel = new DefaultListModel();
        if (currentCalendarId != null) {
            DataProvider dataProvider = new DataProvider();
            Collection<UserStory> userStories = dataProvider.getUserStories(currentCalendarId);
            for (UserStory userStory : userStories) {
                defaultListModel.addElement(userStory);
            }
//            System.out.println("------------- PRINTING TASKS");
            Collection<Task> tasks = dataProvider.getTasks();

//
//                        java.util.List<com.google.api.services.calendar.model.Event> items = GoogleCalendarConnector.getInstance().getEventsForCalendarID(currentCalendarId);
//                        for (com.google.api.services.calendar.model.Event ev : items) {
//                            System.out.println(ev);
//                            if (ev != null && ev.getDescription() != null)
//                                listUserStoriesModel.addElement(ev.getDescription());
//                        }
        }

        return defaultListModel;
    }

    public void loginOrLogout(String profileName) {
        if (isUserLogged()) {
            logout();
        } else {
            login(profileName);
        }

    }

    public boolean isUserLogged() {
        return userLogged;
    }

    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
        view.setLogged(userLogged);
    }

    public void init() {
        logger.log("Initializing Controller");
        refreshProfilesComboBox();
    }

    public void refreshProfilesComboBox() {
        view.populateProfilesComboBox(GoogleCalendarConnector.getProfiles());
    }

    public void addNewProfile(String profileName) {
        if (checkProfileName(profileName)) {
            GoogleCalendarConnector.addNewProfile(profileName);
            view.populateProfilesComboBox(GoogleCalendarConnector.getProfiles());
        } else {
            view.displayErrorDialog("Profile name must contains only letters and numbers!");
        }
    }

    private boolean checkProfileName(String profileName) {
        return profileName != null && !profileName.isEmpty() && CharMatcher.JAVA_LETTER_OR_DIGIT.matchesAllOf(profileName);
    }

    public void deleteProfile(String profileName) {
        try {
            GoogleCalendarConnector.deleteProfile(profileName);
        } catch (IOException e) {
            view.displayErrorDialog("Cannot remove profile '" + profileName + "'");
            logger.logError(e);
        }
    }

    public void saveNewTask(Task task) {
        dataProvider.refreshTasksInfo();
        if (existsTaskWithID(task.getId())) {
            view.displayErrorDialog("Cannot add task. Task with id '" + task.getId() + "' currently exists!");
        } else {
            task.getUserStory().getGoogleApiId();
//            dataProvider.getUserStories(currentCalendarId);
            UserStory userStory = dataProvider.getUserStory(task.getUserStory().getId(), currentCalendarId);
            if (userStory != null) {
                userStory.getTaskCollection().add(task);
                updateUserStory(userStory);
            }


            GoogleCalendarConnector.getInstance().saveTask(task.convertToGoogleTask());
            view.displayInfoDialog("Task succesfully added!");
        }
    }

    private boolean existsTaskWithID(String id) {
        for (Task taks : dataProvider.getTasks()) {
            if (taks.getId() != null && taks.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void updateTask(Task task) {
        logger.log("Updating task with id: " + task.getId());
        dataProvider.refreshTasksInfo();
        if (!existsTaskWithID(task.getId())) {
            view.displayErrorDialog("Cannot update task. Task does not exists!");
        } else {
            Task existingTask = dataProvider.getTask(task.getId());
            Map<String, String> changesSet = existingTask.getChangesSet(task);
            String changesetString = getChangesetString(changesSet);
            boolean questionResult = view.displayYesNoDialog("Do you really want to update task: " + existingTask.getId() + "?" + "\n" + changesetString, "Update task?");
//            GoogleCalendarConnector.getInstance().saveTask(task.convertToGoogleTask());


            // TODO !!!!!!!!!!!!!!!!!!!!!
            Task old = dataProvider.getTask(task.getId());
            if (old != null && old.getUserStory() != null) {
                UserStory userStoryOld = dataProvider.getUserStory(old.getUserStory().getId(), currentCalendarId);
                if (userStoryOld != null) {
                    userStoryOld.removeTask(old);
                    updateUserStory(userStoryOld);
                }
            }

            UserStory userStory = dataProvider.getUserStory(task.getUserStory().getId(), currentCalendarId);
            if (userStory != null) {
                userStory.getTaskCollection().add(task);
                updateUserStory(userStory);
            }

            if (questionResult) {
                GoogleCalendarConnector.getInstance().updateTask(task.convertToGoogleTask());
                view.displayInfoDialog("Task succesfully updated!");
            }
        }
    }

    private String getChangesetString(Map<String, String> changesSet) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : changesSet.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public void saveNewUserStory(UserStory userStory) {
        try {
            dataProvider.refreshUserStoriesInfo(currentCalendarId);
            dataProvider.getUserStories(currentCalendarId);

            if (existsUserStoryWithID(userStory.getId())) {
                view.displayErrorDialog("Cannot add User Story. US with id '" + userStory.getId() + "' currently exists!");
            } else {
                GoogleCalendarConnector.getInstance().saveUserStory(userStory.convertToGoogleEventy(), currentCalendarId);
                view.displayInfoDialog("User story succesfully added!");
            }
        } catch (IOException e) {
            logger.logError(e);
            e.printStackTrace();
        }
    }


    private boolean existsUserStoryWithID(String id) throws IOException {
        for (UserStory userStory : dataProvider.getUserStories(currentCalendarId)) {
            if (userStory.getId() != null && userStory.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public void updateUserStory(UserStory userStory) {
        logger.log("Updating US with id: " + userStory.getId());
        try {
            dataProvider.refreshUserStoriesInfo(currentCalendarId);
            if (!existsUserStoryWithID(userStory.getId())) {
                view.displayErrorDialog("Cannot update task. Task does not exists!");
            } else {
                UserStory existingUS = dataProvider.getUserStory(userStory.getId(), currentCalendarId);
                Map<String, String> changesSet = existingUS.getChangesSet(userStory);
                String changesetString = getChangesetString(changesSet);
                boolean questionResult = view.displayYesNoDialog("Do you really want to update US: " + existingUS.getId() + "?" + "\n" + changesetString, "Update User Story?");
//            GoogleCalendarConnector.getInstance().saveTask(task.convertToGoogleTask());
                if (questionResult) {
                    GoogleCalendarConnector.getInstance().updateUserStory(userStory.convertToGoogleEventy(), currentCalendarId);
                    view.displayInfoDialog("User Story succesfully updated!");
                }
            }
        } catch (IOException e) {
            logger.logError(e);
        }
    }
}
