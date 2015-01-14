package com.rwteam.gscrum.controller.parsers;

import com.rwteam.gscrum.controller.utils.ParsersUtils;
import com.rwteam.gscrum.model.Task;

/**
 * Created by wrabel on 1/13/2015.
 */
public class TaskParser {
    public static Task parseTask(com.google.api.services.tasks.model.Task googleTask) {
        System.out.println("Parsing task: " + googleTask.getTitle());
        if (googleTask == null || googleTask.getTitle().isEmpty() || googleTask.getNotes().isEmpty())
            return null;

        String value = googleTask.getNotes();


//        task.setId(ParsersUtils.cutoutValueFromTag(value, "id"));


//        Date startDate = new Date(Date.parse(ParsersUtils.cutoutValueFromTag(value, "start_date")));
//        userStory.setStartDate(startDate);


        Task task = new Task();
        task.setId(ParsersUtils.cutoutValueFromTag(value, "id"));
        task.setDescription(ParsersUtils.cutoutValueFromTag(value, "description"));
        task.setAssignedPerson(ParsersUtils.cutoutValueFromTag(value, "assigned_person"));
        task.setPriority(ParsersUtils.cutoutValueFromTag(value, "priority"));
        String estimatedHours = ParsersUtils.cutoutValueFromTag(value, "estimated_hours").replace("h", "");
        try {

            task.setEstimatedHours(Double.parseDouble(estimatedHours));
        } catch (Exception ex)

        {
            task.setEstimatedHours(null);
        }

        return task;
    }

}
