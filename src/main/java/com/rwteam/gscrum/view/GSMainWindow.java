package com.rwteam.gscrum.view;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.rwteam.gscrum.controller.GSMainWindowController;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by wrabel on 11/30/2014.
 */
public class GSMainWindow implements ToolWindowFactory {
    private JButton btnLogin;
    private JLabel lblLoginStatus;
    private JButton btnLoadCalendarInfo;
    private DefaultComboBoxModel<String> cbxChooseCalendarModel;
    private JComboBox<String> cbxChooseCalendar;

    private DefaultListModel listUserStoriesModel;
    private JList<UserStory> listUserStories;
    private DefaultListModel listTasksModel;
    private JList<Task> listTasks;


    private JTextArea txtTaskDetails;
    private GSMainWindowController controller = new GSMainWindowController(this);
    private boolean isUserLogged = false;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        final Container container = toolWindow.getComponent().getParent();
        container.setLayout(null);

        btnLogin = new JButton("Login");
        lblLoginStatus = new JLabel("You are not logged");
        btnLoadCalendarInfo = new JButton("Refresh calendar info");
        cbxChooseCalendarModel = new DefaultComboBoxModel<String>();
        cbxChooseCalendar = new JComboBox<String>(cbxChooseCalendarModel);
        listUserStoriesModel = new DefaultListModel();
        listUserStories = new JList<UserStory>(listUserStoriesModel);
        listTasksModel = new DefaultListModel();
        listTasks = new JList<Task>(listTasksModel);
        txtTaskDetails = new JTextArea();

        btnLogin.setBounds(10, 10, 150, 30);
        lblLoginStatus.setBounds(180, 10, 200, 30);
        cbxChooseCalendar.setBounds(10, 50, 250, 30);
        btnLoadCalendarInfo.setBounds(270, 50, 150, 30);
        listUserStories.setBounds(10, 90, 150, 500);
        listTasks.setBounds(170, 90, 150, 500);
        txtTaskDetails.setBounds(330, 90, 500, 500);

        container.add(btnLogin);
        container.add(lblLoginStatus);
        container.add(btnLoadCalendarInfo);
        container.add(cbxChooseCalendar);
        container.add(listUserStories);
        container.add(listTasks);
        container.add(txtTaskDetails);

        cbxChooseCalendar.setEnabled(false);
        btnLoadCalendarInfo.setEnabled(false);


        btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUserLogged) {
                    controller.logout();
                } else {
                    controller.login();
                }
            }
        });

        btnLoadCalendarInfo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listUserStoriesModel.clear();
                String currentCalendarId = (String) cbxChooseCalendarModel.getSelectedItem();
                listUserStories.setModel(controller.loadCalendarsInfo(currentCalendarId));
//                listUserStoriesModel = controller.loadCalendarsInfo(currentCalendarId);
            }
        });

        listUserStories.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listTasksModel.clear();
                txtTaskDetails.setText("");

                System.out.println("Events list selection changed");
                UserStory userStory = listUserStories.getSelectedValue();
                if (userStory != null) {
                    for (Task task : userStory.getTaskCollection()) {
                        listTasksModel.addElement(task);
                    }
                }
            }
        });

        listTasks.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                txtTaskDetails.setText("");
                Task task = listTasks.getSelectedValue();
                if (task != null) {
                    txtTaskDetails.setText(task.getAllInfo());
                }
            }
        });
    }

    public void populateCalendarComboBox(List<CalendarListEntry> calendars) {
        System.out.println("Populating calendar comboBox");
        cbxChooseCalendarModel.removeAllElements();
        for (CalendarListEntry entry : calendars) {
            System.out.println("Adding calendar to comboBox: " + entry.getId());
            cbxChooseCalendarModel.addElement(entry.getId());
            cbxChooseCalendar.setEnabled(true);
            btnLoadCalendarInfo.setEnabled(true);
        }
    }

    public void setLogged(boolean isUserLogged) {
        this.isUserLogged = isUserLogged;
        btnLogin.setText(isUserLogged ? "Logout" : "Login");
        cbxChooseCalendar.setEnabled(isUserLogged);
        btnLoadCalendarInfo.setEnabled(isUserLogged);

        if (!isUserLogged) {
            clearAll();
        }
    }

    private void clearAll() {
        System.out.println("Clearing all");
        listUserStoriesModel.clear();
        listTasksModel.clear();
        txtTaskDetails.setText("");
        cbxChooseCalendarModel.removeAllElements();
    }


    public void setStatus(String status) {
        lblLoginStatus.setText(status);
    }
}
