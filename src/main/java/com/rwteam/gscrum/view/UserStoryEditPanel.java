package com.rwteam.gscrum.view;

import com.rwteam.gscrum.model.UserStory;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * Created by wrabel on 1/13/2015.
 */
public class UserStoryEditPanel extends JPanel {
    private static final Color DEFAULT_TEXT_COMPONENTS_BACKGROUND_COLOR = Color.WHITE;
    private static final Color HIGHLIGHTED_TEXT_COMPONENT_BACKGROUND_COLOR = Color.GREEN;
    private static final String EMPTY_NUMBER_STRING = "--";

    private final GSMainWindow gsMainWindow;

    private final JButton btnSaveUserStory;
    private JTextArea txtAreaDescription;
    private JTextField txtId;
    private JDatePickerImpl dateStart;
    private JDatePickerImpl dateEnd;
    private JLabel lblId;
    private JLabel lblStartDate;
    private JLabel lblEndDate;
    private JLabel lblDescription;
    private UserStory currentUS;

    private boolean isInAddNewUsMode = false;
    private boolean trackChanges = false;


    public UserStoryEditPanel(GSMainWindow gsMainWindow, UserStory userStory) {
        this.gsMainWindow = gsMainWindow;
        setLayout(null);

        lblId = new JLabel("ID:");
        lblEndDate = new JLabel("End date:");
        lblStartDate = new JLabel("Start date:");
        lblDescription = new JLabel("Desciption:");
        btnSaveUserStory = new JButton("Save US");

        txtId = new JTextField();
        txtAreaDescription = new JTextArea();
        lblId.setBounds(5, 5, 60, 20);
        txtId.setBounds(70, 5, 270, 20);

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        dateStart = new JDatePickerImpl(datePanel);

        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
        dateEnd = new JDatePickerImpl(datePanel2);

        lblStartDate.setBounds(5, 30, 100, 20);
        dateStart.setBounds(70, 30, 180, 30);

        lblEndDate.setBounds(5, 65, 60, 20);
        dateEnd.setBounds(70, 65, 180, 30);

        lblDescription.setBounds(5, 100, 70, 20);
        txtAreaDescription.setBounds(70, 100, 270, 80);
        txtAreaDescription.setLineWrap(true);

        btnSaveUserStory.setBounds(240, 200, 100, 30);

        setTooltips();

        add(lblId);
        add(lblEndDate);
        add(lblStartDate);
        add(lblDescription);
        add(txtId);
        add(dateEnd);
        add(dateStart);
        add(txtAreaDescription);
        add(btnSaveUserStory);

        initButtonsListeners();
        initChangeListeners();

        this.setEditable(true);
        this.setPreferredSize(new Dimension(300, 300));


        if (userStory == null) {
            isInAddNewUsMode = true;
            userStory = UserStory.EMPTY;
        }
        populateWithUserStory(userStory, true);
    }

    public static void main(String... args) {
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(400, 400);
        UserStoryEditPanel taskEditPanel = new UserStoryEditPanel(null, new UserStory());

        jFrame.add(taskEditPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initButtonsListeners() {
        btnSaveUserStory.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserStoryAction();
            }
        });
    }

    private void saveUserStoryAction() {
        UserStory userStory = retrieveUserStoryObject();

        if (isInAddNewUsMode) {
            gsMainWindow.saveNewUS(userStory);
        } else {
            gsMainWindow.updateUS(userStory);
        }
    }

    private void initChangeListeners() {
        if (currentUS != null) {
            setTextComponentListener(txtId, currentUS.getId());
//            setTextComponentListener(txtStartDate, currentUS.getStartDate().toString());
//            setTextComponentListener(txtEndDate, currentUS.getEndDate().toString());
            setTextComponentListener(txtAreaDescription, currentUS.getDescription());
        }
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
                String innerDefaultValue = defaultValue == null ? "" : defaultValue;
                if (currentUS != null && !textField.getText().equals(innerDefaultValue) && trackChanges) {
                    textField.setBackground(HIGHLIGHTED_TEXT_COMPONENT_BACKGROUND_COLOR);
                } else {
                    System.out.println("Setting default color");
                    textField.setBackground(DEFAULT_TEXT_COMPONENTS_BACKGROUND_COLOR);
                }
            }
        });
    }

    private void setTooltips() {
        txtId.setToolTipText("User story ID");
        dateStart.setToolTipText("User story start date");
        dateEnd.setToolTipText("User story end date");
        txtAreaDescription.setToolTipText("User story detailed description");
    }

    public void clearData() {
        txtId.setText("");
        txtAreaDescription.setText("");
        setDate(dateStart, new Date());
        setDate(dateEnd, new Date());
    }

    private void setDate(JDatePickerImpl dateStart, Date date) {

//        dateStart.getModel().setValue(date);
        dateStart.getModel().setDay(date.getDay());
        dateStart.getModel().setMonth(date.getMonth());
        dateStart.getModel().setYear(1900 + date.getYear());
        dateStart.getModel().setSelected(true);
    }

    public void populateWithUserStory(UserStory userStory, boolean trackChanges) {
        this.currentUS = userStory;
        this.trackChanges = trackChanges;

        clearData();
        initChangeListeners();

        txtId.setText(userStory.getId());
        txtAreaDescription.setText(userStory.getDescription());
        setDate(dateStart, userStory.getStartDate());
        setDate(dateEnd, userStory.getEndDate());
    }

    public UserStory retrieveUserStoryObject() {
        UserStory userStory = new UserStory();
        userStory.setGoogleApiId(currentUS.getGoogleApiId());
        userStory.setDescription(txtAreaDescription.getText());
        userStory.setId(txtId.getText());
        userStory.setStartDate((Date) dateStart.getModel().getValue());
        userStory.setEndDate((Date) dateEnd.getModel().getValue());
        userStory.setTaskCollection(currentUS.getTaskCollection());

        if (userStory.getStartDate() == null || userStory.getEndDate() == null) {
            gsMainWindow.displayErrorDialog("Wrong date!");
            return null;
        }

        if (userStory.getStartDate() == null) {
            userStory.setStartDate(new Date());
        }

        if (userStory.getEndDate() == null) {
            userStory.setEndDate(new Date());
        }

        return userStory;
    }

    public void setEditable(boolean editable) {
        txtAreaDescription.setEditable(editable);
        txtId.setEditable(editable);
        dateEnd.setEnabled(editable);
        dateStart.setEnabled(editable);
    }

    public void setEditable(boolean editable, boolean isInAddNewTaskMode) {
        setEditable(editable);
        txtId.setEditable(editable && !isInAddNewTaskMode);
    }
}
