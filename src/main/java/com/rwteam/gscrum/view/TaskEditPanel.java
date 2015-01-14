package com.rwteam.gscrum.view;

import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by wrabel on 1/13/2015.
 */
public class TaskEditPanel extends JPanel {
    private final GSMainWindow gsMainWindow;
//    JScrollPane scrollPane;
//    JPanel contentPanel;
//
//    JButton button;

    JLabel lblId;
    JLabel lblPriority;
    JLabel lblAssignedPerson;
    JLabel lblUserStory;
    JLabel lblEstimatedHours;
    JLabel lblDescription;


    JTextField txtId;
    JTextField txtPriority;
    JTextField txtAssignedPerson;
    JComboBox<UserStoryHolder> cbxUserStory;
    JTextField txtEstimatedHours;
    JTextArea txtAreaDescription;


    public TaskEditPanel(GSMainWindow gsMainWindow) {
        this.gsMainWindow = gsMainWindow;
        setLayout(null);

        lblId = new JLabel("ID:");
        lblPriority = new JLabel("Priority:");
        lblAssignedPerson = new JLabel("Assigned:");
        lblUserStory = new JLabel("US:");
        lblEstimatedHours = new JLabel("Estimated hours:");
        lblDescription = new JLabel("Desciption:");

        txtId = new JTextField();
        txtPriority = new JTextField();
        txtAssignedPerson = new JTextField();
        cbxUserStory = new JComboBox<UserStoryHolder>();
        txtEstimatedHours = new JTextField();
        txtAreaDescription = new JTextArea();

        lblId.setBounds(0, 0, 50, 20);
        txtId.setBounds(60, 0, 100, 20);

        lblPriority.setBounds(170, 0, 50, 20);
        txtPriority.setBounds(230, 0, 100, 20);

        lblAssignedPerson.setBounds(0, 30, 60, 20);
        txtAssignedPerson.setBounds(70, 30, 100, 20);

        lblUserStory.setBounds(180, 30, 30, 20);
        cbxUserStory.setBounds(220, 30, 100, 20);

        lblEstimatedHours.setBounds(0, 60, 100, 20);
        txtEstimatedHours.setBounds(110, 60, 100, 20);

        lblDescription.setBounds(0, 90, 70, 20);
        txtAreaDescription.setBounds(80, 90, 250, 150);
        txtAreaDescription.setLineWrap(true);

        add(lblId);
        add(lblPriority);
        add(lblAssignedPerson);
        add(lblUserStory);
        add(lblEstimatedHours);
        add(lblDescription);
        add(txtId);
        add(txtPriority);
        add(txtAssignedPerson);
        add(cbxUserStory);
        add(txtEstimatedHours);
        add(txtAreaDescription);


        this.setPreferredSize(new Dimension(300, 300));
        this.setBackground(Color.RED);

    }

    public static void main(String... args) {
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(400, 400);
        TaskEditPanel taskEditPanel = new TaskEditPanel(null);

        jFrame.add(taskEditPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void clearData() {
        txtId.setText("");
        txtAreaDescription.setText("");
        txtEstimatedHours.setText("");
        txtAssignedPerson.setText("");
        txtPriority.setText("");
//        cbxUserStory.setText("");
    }

    public void populateWithTask(Task task, ListModel<UserStory> listUserStories) {
        clearData();
        txtId.setText(task.getId());
        txtAreaDescription.setText(task.getDescription());
        txtPriority.setText(task.getPriority());
        txtAssignedPerson.setText(task.getAssignedPerson());

//        if(gsMainWindow.c)
// TODO
//        cbxUserStory.setText(task.getUserStory().getId());

        UserStory userStory = task.getUserStory();
        String usId = "";
        if (userStory != null) {
            usId = userStory.getId();
        }
        cbxUserStory.setModel(createUSHoldersListModel(listUserStories, usId));


        Double estimatedHours = task.getEstimatedHours();
        if (estimatedHours == null) {
            txtEstimatedHours.setText("--");
        } else {
            txtEstimatedHours.setText(estimatedHours.toString());
        }
    }

    private DefaultComboBoxModel<UserStoryHolder> createUSHoldersListModel(ListModel<UserStory> listUserStories, String usIdToSelect) {
        DefaultComboBoxModel<UserStoryHolder> holderList = new DefaultComboBoxModel<UserStoryHolder>();

        boolean usIdFound = false;
        if (listUserStories != null) {
            for (int i = 0; i < listUserStories.getSize(); i++) {
                UserStoryHolder userStoryHolder = new UserStoryHolder(listUserStories.getElementAt(i));
                holderList.addElement(userStoryHolder);
                if (userStoryHolder.getUserStory().getId().equals(usIdToSelect)) {
                    holderList.setSelectedItem(userStoryHolder);
                    usIdFound = true;
                }
            }
        }
        UserStoryHolder userStoryHolderEmpty = new UserStoryHolder(null);
        holderList.addElement(userStoryHolderEmpty);

        if (!usIdFound) {
            holderList.setSelectedItem(userStoryHolderEmpty);
        }

        return holderList;
    }

    public Task retrieveTaskObject() {
        Task task = new Task();
        task.setDescription(txtAreaDescription.getText());
        task.setPriority(txtPriority.getText());
        task.setAssignedPerson(txtAssignedPerson.getText());
        task.setId(txtId.getText());

        try {
            double estimatedHours = Double.parseDouble(txtEstimatedHours.getText());
            task.setEstimatedHours(estimatedHours);
        } catch (Exception ex) {
            task.setEstimatedHours(null);
        }

        if(cbxUserStory.getSelectedItem().equals(UserStoryHolder.UNKNOWN)){
            task.setUserStory(null);
        }
        else {
            UserStory userStory =new UserStory();
            userStory.setId(cbxUserStory.getSelectedItem().toString());
            task.setUserStory(userStory);
        }

        return task;
    }
}
