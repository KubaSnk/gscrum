package com.rwteam.gscrum.controller.parsers;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.controller.utils.ParsersUtils;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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


        userStory.setId(ParsersUtils.cutoutValueFromTag(value, "id"));


//        Date startDate = new Date(Date.parse(ParsersUtils.cutoutValueFromTag(value, "start_date")));
//        userStory.setStartDate(startDate);


        List<Task> taskList = new ArrayList<>();
        String tasksString = ParsersUtils.cutoutValueFromTag(value, "tasks");
        String[] tasks = value.split("<task>");
        for (int i = 1; i < tasks.length; i++) {
            String currentTask = tasks[i].split("</task>")[0];
            String taskId = ParsersUtils.cutoutValueFromTag(currentTask, "id");
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

}
