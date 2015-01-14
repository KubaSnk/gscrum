package com.rwteam.gscrum.controller;

import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import com.rwteam.gscrum.view.GSMainWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by wrabel on 1/10/2015.
 */
public class GSMainWindowController {
    private GSMainWindow view;
    private boolean userLogged;

    public GSMainWindowController(GSMainWindow gsMainWindow) {
        this.view = gsMainWindow;
    }

    public void login() {

        System.out.println("Button login handler");
        try {
            String userName = GoogleCalendarConnector.getInstance().connect();
            view.setStatus("Successfully logged as " + userName);
            view.populateCalendarComboBox(GoogleCalendarConnector.getInstance().getCalendars().getItems());
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
        DefaultListModel defaultListModel = new DefaultListModel();
        try {
            if (currentCalendarId != null) {
                DataProvider dataProvider = new DataProvider();
                Collection<UserStory> userStories = dataProvider.getUserStories(currentCalendarId);
                for (UserStory userStory : userStories) {
                    defaultListModel.addElement(userStory);
                }
                System.out.println("------------- PRINTING TASKS");
                Collection<Task> tasks = dataProvider.getTasks();
                for (Task task : tasks) {
                    System.out.println(task.getAllInfo());
                }
//
//                        java.util.List<com.google.api.services.calendar.model.Event> items = GoogleCalendarConnector.getInstance().getEventsForCalendarID(currentCalendarId);
//                        for (com.google.api.services.calendar.model.Event ev : items) {
//                            System.out.println(ev);
//                            if (ev != null && ev.getDescription() != null)
//                                listUserStoriesModel.addElement(ev.getDescription());
//                        }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        return defaultListModel;
    }


    public void loginOrLogout() {
        if (isUserLogged()) {
            logout();
        } else {
            login();
        }

    }

    public boolean isUserLogged() {
        return userLogged;
    }

    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
        view.setLogged(userLogged);
    }
}
