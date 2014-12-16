package com.rwteam.gscrum.view;

import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by wrabel on 11/30/2014.
 */
public class GSMainWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        final Container container = toolWindow.getComponent().getParent();
        container.setLayout(null);

        JButton loginButton = new JButton("Login");
        loginButton.setSize(200, 100);
        container.add(loginButton);

        final JTextArea jTextArea = new JTextArea();
        jTextArea.setBounds(0, 100, 600, 300);
        container.add(jTextArea);

        final DefaultListModel listModel = new DefaultListModel();
        final JList<String> stringJList = new JList<String>(listModel);
        stringJList.setBounds(0, 410, 600, 400);
        container.add(stringJList);

        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button login handler");
                try {
                    GoogleCalendarConnector.getInstance().connect();

                    StringBuilder stringBuilder = new StringBuilder();
                    CalendarList calendars = GoogleCalendarConnector.getInstance().getCalendars();
                    for (CalendarListEntry entry : calendars.getItems()) {

                        stringBuilder.append("ID: " + entry.getId()).append("\n");
                        stringBuilder.append("Summary: " + entry.getSummary()).append("\n");
                        if (entry.getDescription() != null) {
                            stringBuilder.append("Description: " + entry.getDescription()).append("\n");
                        }
                        stringBuilder.append("-----------------").append("\n");

                        stringBuilder.append(entry.toString()).append("\n");


                        java.util.List<com.google.api.services.calendar.model.Event> items = GoogleCalendarConnector.getInstance().getEventsForCalendarID(entry.getId());
                        for (com.google.api.services.calendar.model.Event ev : items) {
                            System.out.println(ev);
                            if (ev != null && ev.getDescription() != null)
                                listModel.addElement(ev.getDescription());
                        }

                    }
                    jTextArea.setText(stringBuilder.toString());
                } catch (Exception e1) {
                    jTextArea.setText(e1.toString());
                    e1.printStackTrace();
                }
            }
        });


    }
}
