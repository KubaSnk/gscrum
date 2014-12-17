package com.rwteam.gscrum.view;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by wrabel on 11/30/2014.
 */
public class GSMainWindow implements ToolWindowFactory {
    private JButton btnLogin;
    private JLabel lblLoginStatus;
    private JButton btnLoadCalendarInfo;
    private DefaultComboBoxModel<String> cbxChooseCalendarModel;
    private JComboBox<String> cbxChooseCalendar;
    private DefaultListModel listEventsModel;
    private JList<String> listEvents;
    private JTextArea txtEvent;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        final Container container = toolWindow.getComponent().getParent();
        container.setLayout(null);

        btnLogin = new JButton("Login");
        lblLoginStatus = new JLabel("You are not logged");
        btnLoadCalendarInfo = new JButton("Refresh calendar info");
        cbxChooseCalendarModel = new DefaultComboBoxModel<String>();
        cbxChooseCalendar = new JComboBox<String>(cbxChooseCalendarModel);
        listEventsModel = new DefaultListModel();
        listEvents = new JList<String>(listEventsModel);
        txtEvent = new JTextArea();

        btnLogin.setBounds(10, 10, 150, 30);
        lblLoginStatus.setBounds(180, 10, 200, 30);
        cbxChooseCalendar.setBounds(10, 50, 250, 30);
        btnLoadCalendarInfo.setBounds(270, 50, 150, 30);
        listEvents.setBounds(10, 90, 150, 500);
        txtEvent.setBounds(170,90, 500, 500);

        container.add(btnLogin);
        container.add(lblLoginStatus);
        container.add(btnLoadCalendarInfo);
        container.add(cbxChooseCalendar);
        container.add(listEvents);
        container.add(txtEvent);

        cbxChooseCalendar.setEnabled(false);
        btnLoadCalendarInfo.setEnabled(false);



        btnLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button login handler");
                try {
                    GoogleCalendarConnector.getInstance().connect();
                    lblLoginStatus.setText("Successfully logged");
                    btnLogin.setEnabled(false);
                    populateCalendarComboBox();

                } catch (Exception e1) {
                    lblLoginStatus.setText("Error while logging");
                    e1.printStackTrace();
                }
            }
        });

        btnLoadCalendarInfo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                listEventsModel.clear();
                try {

                    String currentCalendarId = (String) cbxChooseCalendarModel.getSelectedItem();
                    if (currentCalendarId != null) {
                        java.util.List<com.google.api.services.calendar.model.Event> items = GoogleCalendarConnector.getInstance().getEventsForCalendarID(currentCalendarId);
                        for (com.google.api.services.calendar.model.Event ev : items) {
                            System.out.println(ev);
                            if (ev != null && ev.getDescription() != null)
                                listEventsModel.addElement(ev.getDescription());
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });

        listEvents.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("Events list selection changed");

                String eventText = listEvents.getSelectedValue();
                if (eventText != null) {
                    txtEvent.setText(eventText);
                }
            }
        });
    }

    private void populateCalendarComboBox() {
        cbxChooseCalendarModel.removeAllElements();
        try {
            for (CalendarListEntry entry : GoogleCalendarConnector.getInstance().getCalendars().getItems()) {
                System.out.println("Adding calendar to comboBox: " + entry.getId());
                cbxChooseCalendarModel.addElement(entry.getId());
                cbxChooseCalendar.setEnabled(true);
                btnLoadCalendarInfo.setEnabled(true);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            cbxChooseCalendar.setEnabled(false);
            btnLoadCalendarInfo.setEnabled(false);
        }
    }


}
