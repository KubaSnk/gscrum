package com.rwteam.gscrum.view;

import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Created by wrabel on 1/13/2015.
 */
public class TaskEditPanel extends JPanel {
    private static final Color DEFAULT_TEXT_COMPONENTS_BACKGROUND_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_TEXT_COMPONENT_BACKGROUND_COLOR = Color.GREEN;
    private static final String EMPTY_NUMBER_STRING = "--";
    private final GSMainWindow gsMainWindow;
    private final JLabel lblHours;

    JTextField txtId;
    JTextField txtPriority;
    JTextField txtAssignedPerson;
    JComboBox<UserStoryHolder> cbxUserStory;
    JTextField txtEstimatedHours;
    JTextArea txtAreaDescription;
    JTextField txtAuthor;
    JTextField txtSpentHours;
    JTextField txtStatus;

    private JLabel lblId;
    private JLabel lblPriority;
    private JLabel lblAssignedPerson;
    private JLabel lblUserStory;
    private JLabel lblEstimatedHours;
    private JLabel lblDescription;
    private JLabel lblAuthor;
    private JLabel lblSpentHours;
    private JLabel lblStatus;
    private Task currentTask;
    private boolean trackChanges = false;


    public TaskEditPanel(GSMainWindow gsMainWindow) {
        this.gsMainWindow = gsMainWindow;
        setLayout(null);

        lblId = new JLabel("ID:");
        lblPriority = new JLabel("Priority:");
        lblAssignedPerson = new JLabel("Assigned:");
        lblUserStory = new JLabel("US:");
        lblHours = new JLabel("Hours:");
        lblEstimatedHours = new JLabel("Estimated (h):");
        lblDescription = new JLabel("Desciption:");
        lblAuthor = new JLabel("Author");
        lblSpentHours = new JLabel("Spent (h):");
        lblStatus = new JLabel("Status:");

        txtId = new JTextField();
        txtPriority = new JTextField();
        txtAssignedPerson = new JTextField();
        cbxUserStory = new JComboBox<UserStoryHolder>();
        txtEstimatedHours = new JTextField();
        txtAreaDescription = new JTextArea();
        txtAuthor = new JTextField();
        txtSpentHours = new JTextField();
        txtStatus = new JTextField();

        lblId.setBounds(5, 5, 45, 20);
        txtId.setBounds(60, 5, 100, 20);

        lblStatus.setBounds(170, 5, 60, 20);
        txtStatus.setBounds(230, 5, 100, 20);

        lblAuthor.setBounds(5, 30, 45, 20);
        txtAuthor.setBounds(60, 30, 100, 20);

        lblAssignedPerson.setBounds(170, 30, 60, 20);
        txtAssignedPerson.setBounds(230, 30, 100, 20);

        lblPriority.setBounds(5, 55, 45, 20);
        txtPriority.setBounds(60, 55, 100, 20);

        lblUserStory.setBounds(5, 80, 45, 20);
        cbxUserStory.setBounds(60, 80, 270, 20);

        lblEstimatedHours.setBounds(5, 105, 100, 20);
        txtEstimatedHours.setBounds(90, 105, 50, 20);
        lblSpentHours.setBounds(145, 105, 80, 20);
        txtSpentHours.setBounds(210, 105, 50, 20);

        lblDescription.setBounds(5, 145, 70, 20);
        txtAreaDescription.setBounds(5, 165, 325, 80);
        txtAreaDescription.setLineWrap(true);

        setTooltips();

        add(lblId);
        add(lblPriority);
        add(lblAssignedPerson);
        add(lblUserStory);
        add(lblEstimatedHours);
        add(lblDescription);
        add(lblAuthor);
        add(lblSpentHours);
        add(lblStatus);
        add(lblHours);
        add(txtId);
        add(txtPriority);
        add(txtAssignedPerson);
        add(cbxUserStory);
        add(txtEstimatedHours);
        add(txtAreaDescription);
        add(txtAuthor);
        add(txtSpentHours);
        add(txtStatus);

        initChangeListeners();

        this.setEditable(false);
        this.setPreferredSize(new Dimension(300, 300));
    }

    public static void main(String... args) {
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(400, 400);
        TaskEditPanel taskEditPanel = new TaskEditPanel(null);

        jFrame.add(taskEditPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initChangeListeners() {
        if (currentTask != null) {
            setTextComponentListener(txtId, currentTask.getId());
            setTextComponentListener(txtAuthor, currentTask.getAuthor());
            System.out.println("Change assigned person ->" + currentTask.getAssignedPerson() + "<-");
            setTextComponentListener(txtAssignedPerson, currentTask.getAssignedPerson());
            setTextComponentListener(txtEstimatedHours, currentTask.getEstimatedHours());
            setTextComponentListener(txtPriority, currentTask.getPriority());
            setTextComponentListener(txtSpentHours, currentTask.getSpentHours());
            setTextComponentListener(txtStatus, currentTask.getStatus());

            setTextComponentListener(txtAreaDescription, currentTask.getDescription());
        }
//        else {
//            setTextComponentListener(txtId, null);
//            setTextComponentListener(txtAuthor, null);
//            setTextComponentListener(txtAssignedPerson, null);
//            setTextComponentListener(txtEstimatedHours, null);
//            setTextComponentListener(txtPriority, null);
//            setTextComponentListener(txtSpentHours, null);
//            setTextComponentListener(txtStatus, null);
//        }

    }

    private void setTextComponentListener(final JTextComponent textField, final Double defaultValue) {
        String defaultValueString = "";
        if (defaultValue != null) {
            defaultValueString = Double.toString(defaultValue);
        }

        setTextComponentListener(textField, defaultValueString);
    }

    private void setTextComponentListener(final JTextComponent textField, final String defaultValue) {
        System.out.println("Setting for component: " + textField + " default value: " + defaultValue);
        String text = textField.getText();
        textField.setDocument(new DefaultStyledDocument());
        textField.setText(text);

        textField.setBackground(DEFAULT_TEXT_COMPONENTS_BACKGROUND_COLOR);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                if (textField.equals(txtAssignedPerson)) {
                    System.out.println("Text: ->" + textField.getText() + "<-");
                    System.out.println("Default: ->" + defaultValue + "<-");
                }
                String innerDefaultValue = defaultValue == null ? "" : defaultValue;
                if (currentTask != null && !textField.getText().equals(innerDefaultValue) && trackChanges) {
                    textField.setBackground(HIGHLIGHTED_TEXT_COMPONENT_BACKGROUND_COLOR);
                } else {
                    System.out.println("Setting default color");
                    textField.setBackground(DEFAULT_TEXT_COMPONENTS_BACKGROUND_COLOR);
                }
            }
        });
    }

    private void setTooltips() {
        txtId.setToolTipText("Task ID");
        txtPriority.setToolTipText("Task  priority");
        txtAssignedPerson.setToolTipText("Assigned person");
        cbxUserStory.setToolTipText("User to story to which task is assigned");
        txtEstimatedHours.setToolTipText("Estimated time for task ending");
        txtAreaDescription.setToolTipText("Task detailed description");
        txtAuthor.setToolTipText("Task author");
        txtSpentHours.setToolTipText("Amount of hours already spent for this task");
        txtStatus.setToolTipText("Current status of the task");
    }

    public void clearData() {
        txtId.setText("");
        txtAreaDescription.setText("");
        txtEstimatedHours.setText("");
        txtAssignedPerson.setText("");
        txtPriority.setText("");
//        cbxUserStory.setText("");
    }

    public void populateWithTask(Task task, ListModel<UserStory> listUserStories, boolean trackChanges) {
        this.currentTask = task;
        this.trackChanges = trackChanges;

        clearData();
        initChangeListeners();

        txtId.setText(task.getId());
        txtAreaDescription.setText(task.getDescription());
        txtPriority.setText(task.getPriority());
        txtAssignedPerson.setText(task.getAssignedPerson());
        txtAuthor.setText(task.getAuthor());
        txtStatus.setText(task.getStatus());


//        if(gsMainWindow.c)
// TODO(
//        cbxUserStory.setText(task.getUserStory().getId());

        UserStory userStory = task.getUserStory();
        String usId = "";
        if (userStory != null) {
            usId = userStory.getId();
        }
        cbxUserStory.setModel(createUSHoldersListModel(listUserStories, usId));


        Double estimatedHours = task.getEstimatedHours();
        if (estimatedHours == null) {
            txtEstimatedHours.setText(EMPTY_NUMBER_STRING);
        } else {
            txtEstimatedHours.setText(estimatedHours.toString());
        }

        Double spentHours = task.getSpentHours();
        if (spentHours == null) {
            txtSpentHours.setText(EMPTY_NUMBER_STRING);
        } else {
            txtSpentHours.setText(spentHours.toString());
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
        task.setGoogleApiId(currentTask.getGoogleApiId());
        task.setDescription(txtAreaDescription.getText());
        task.setPriority(txtPriority.getText());
        task.setAssignedPerson(txtAssignedPerson.getText());
        task.setId(txtId.getText());
        task.setStatus(txtStatus.getText());
        task.setAuthor(txtAuthor.getText());

        try {
            double estimatedHours = Double.parseDouble(txtEstimatedHours.getText());
            task.setEstimatedHours(estimatedHours);
        } catch (Exception ex) {
            task.setEstimatedHours(null);
        }
        try {
            double spentHours = Double.parseDouble(txtSpentHours.getText());
            task.setSpentHours(spentHours);
        } catch (Exception ex) {
            task.setSpentHours(null);
        }

        if (cbxUserStory.getSelectedItem().equals(UserStoryHolder.UNKNOWN)) {
            task.setUserStory(null);
        } else {
            UserStory userStory = new UserStory();
            userStory.setId(cbxUserStory.getSelectedItem().toString());
            task.setUserStory(userStory);
        }

        return task;
    }

    public void setEditable(boolean editable) {
        txtAreaDescription.setEditable(editable);
        txtId.setEditable(editable);
        txtAssignedPerson.setEditable(editable);
        txtAuthor.setEditable(editable);
        txtEstimatedHours.setEditable(editable);
        txtPriority.setEditable(editable);
        txtSpentHours.setEditable(editable);
        txtStatus.setEditable(editable);
        cbxUserStory.setEditable(editable);
    }

    public void setEditable(boolean editable, boolean isInAddNewTaskMode) {
        setEditable(editable);
        txtId.setEditable(editable && !isInAddNewTaskMode);
    }
}
