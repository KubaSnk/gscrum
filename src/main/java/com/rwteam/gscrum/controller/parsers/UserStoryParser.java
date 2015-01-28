package com.rwteam.gscrum.controller.parsers;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import com.rwteam.gscrum.utils.Logger;

import java.text.ParseException;
import java.util.*;

import static com.rwteam.gscrum.controller.utils.ParsersUtils.cutoutValueFromTag;

/**
 * Created by kubasnk on 12/17/14.
 */
public class UserStoryParser {
    private static final String INCORRECT_TASK_INFO = "-- Invalid --";

    public static UserStory parseUserStory(Event event, DataProvider dataProvider) {
        System.out.println("Parsing userStory: " + event);
        if (event == null)
            return null;

        String value = event.getDescription();

        Event event1 = new Event();
        UserStory userStory = new UserStory();

        userStory.setId(cutoutValueFromTag(value, "id"));
        userStory.setDeadlineDate(parseDate(cutoutValueFromTag(value, "deadline_date")));

        if (event.getStart() != null && event.getStart().getDateTime() != null) {
            userStory.setStartDate(new Date(event.getStart().getDateTime().getValue()));
        }
        if (event.getEnd() != null && event.getEnd().getDateTime() != null) {
            userStory.setEndDate(new Date(event.getEnd().getDateTime().getValue()));
        }

        List<Task> taskList = new ArrayList<>();
        String tasksString = cutoutValueFromTag(value, "tasks");
        String[] tasks = value.split("<task>");
        for (int i = 1; i < tasks.length; i++) {
            String currentTask = tasks[i].split("</task>")[0];
            String taskId = cutoutValueFromTag(currentTask, "id");
            Task task = dataProvider.getTask(taskId);

            if (task == null) {
                task = new Task();
                task.setDescription(currentTask);
                task.setId(INCORRECT_TASK_INFO);
            }
            task.setUserStory(userStory);
            taskList.add(task);


        }
        Collections.sort(taskList);
        userStory.setTaskCollection(taskList);
        return userStory;
    }

    private static Date parseDate(String deadline_date) {
        Date date = null;
        try {
            date = org.apache.commons.lang3.time.DateUtils.parseDate(deadline_date, new String[]{"dd-MM-yyyy"});
        } catch (ParseException e) {
            new Logger(UserStoryParser.class).logError(e);
            e.printStackTrace();
        }
        return org.apache.commons.lang3.time.DateUtils.round(date, Calendar.DAY_OF_MONTH);
    }

}
