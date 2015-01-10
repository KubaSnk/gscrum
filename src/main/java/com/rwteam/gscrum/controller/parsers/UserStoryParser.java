package com.rwteam.gscrum.controller.parsers;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kubasnk on 12/17/14.
 */
public class UserStoryParser {
    public static UserStory parseUserStory(Event event) {
        System.out.println("Parsing userStory: " + event);
        if (event == null)
            return null;

        String value = event.getDescription();


        Event event1 = new Event();
        UserStory userStory = new UserStory();


        userStory.setId(cutoutValueFromTag(value, "id"));


//        Date startDate = new Date(Date.parse(cutoutValueFromTag(value, "start_date")));
//        userStory.setStartDate(startDate);


        List<Task> taskList = new ArrayList<>();
        String tasksString = cutoutValueFromTag(value, "tasks");
        String[] tasks = value.split("<task>");
        for (int i = 1; i < tasks.length; i++) {
            String currentTask = tasks[i].split("</task>")[0];
            Task task = new Task();
            task.setId(cutoutValueFromTag(currentTask, "id"));
            task.setDescription(cutoutValueFromTag(currentTask, "description"));
            task.setAssignedPerson(cutoutValueFromTag(currentTask, "assigned_person"));
            task.setPriority(cutoutValueFromTag(currentTask, "priority"));
            String estimatedHours = cutoutValueFromTag(currentTask, "estimated_hours").replace("h", "");
            task.setEstimatedHours(Double.parseDouble(estimatedHours));
            taskList.add(task);


        }
        userStory.setTaskCollection(taskList);
        return userStory;
    }

    private static String cutoutValueFromTag(String value, String tag) {
        String result = "";

        value = value.trim();
        String[] split = value.split("<" + tag + ">");
        if (split.length > 1) {
            result = split[1].trim().split("</" + tag + ">")[0].trim();
        }
        return result;
    }
}
