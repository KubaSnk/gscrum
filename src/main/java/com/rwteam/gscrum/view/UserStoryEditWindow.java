package com.rwteam.gscrum.view;

import com.rwteam.gscrum.model.UserStory;

import javax.swing.*;

/**
 * Created by wrabel on 1/13/2015.
 */
public class UserStoryEditWindow extends JFrame {
    public UserStoryEditWindow(GSMainWindow gsMainWindow, UserStory userStory) {
        setVisible(true);
        setSize(360, 300);
        setTitle("Edit/Add User Story");
        UserStoryEditPanel editPanel = new UserStoryEditPanel(gsMainWindow, userStory);


        add(editPanel);
        repaint();
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String... args) {
        new UserStoryEditWindow(null, null).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
