package com.rwteam.gscrum.controller.googleapi;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.parsers.TaskParser;
import com.rwteam.gscrum.controller.parsers.UserStoryParser;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector.getInstance;

/**
 * Created by kubasnk on 12/17/14.
 */
public class DataProvider {
    List<Task> tasksCache = null;

    public Collection<UserStory> getUserStories(String calendarID) throws IOException {
        refreshTasksInfo();
        List<UserStory> userStories = new ArrayList<>();
        for (Event event : getInstance().getEventsForCalendarID(calendarID)) {
            userStories.add(UserStoryParser.parseUserStory(event, this));
        }
        Collections.sort(userStories);
        return userStories;
    }

    private void refreshTasksInfo() {
        tasksCache = new ArrayList<Task>();
        for (com.google.api.services.tasks.model.Task taskFromGoogleAPI : getInstance().getTasks()) {
            Task task = TaskParser.parseTask(taskFromGoogleAPI);
            if (task != null) {
                tasksCache.add(task);
            }
        }

    }

    public List<Task> getTasks() {
        if(tasksCache == null){
            refreshTasksInfo();
        }
        return tasksCache;
    }

    public Task getTask(String taskId){
        for(Task task : getTasks()){
            if(task != null && task.getId() != null && task.getId().equals(taskId)){
                return task;
            }
        }

        return null;
    }
}
