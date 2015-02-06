package com.rwteam.gscrum.controller.googleapi;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.parsers.TaskParser;
import com.rwteam.gscrum.controller.parsers.UserStoryParser;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector.getInstance;

/**
 * Created by kubasnk on 12/17/14.
 */
public class DataProvider {
    List<Task> tasksCache = null;
    List<UserStory> userStoriesCache = null;
    GoogleCalendarConnector connector = getInstance();


    public List<UserStory> getUserStories(String calendarID) {
        refreshTasksInfo();
        if (userStoriesCache == null) {
            refreshUserStoriesInfo(calendarID);
        }
        return userStoriesCache;
    }

    public void refreshUserStoriesInfo(String calendarID) {
        userStoriesCache = new ArrayList<>();
        for (Event event : connector.getEventsForCalendarID(calendarID)) {
            if (event.getSummary() != null) {
                userStoriesCache.add(UserStoryParser.parseUserStory(event, this));

            }
        }
        Collections.sort(userStoriesCache);
    }

    public void refreshTasksInfo() {
        tasksCache = new ArrayList<Task>();
        for (com.google.api.services.tasks.model.Task taskFromGoogleAPI : connector.getTasks()) {
            Task task = TaskParser.parseTask(taskFromGoogleAPI);
            if (task != null) {
                tasksCache.add(task);
            }
        }

    }

    public List<Task> getTasks() {
        if (tasksCache == null) {
            refreshTasksInfo();
        }
        return tasksCache;
    }

    public Task getTask(String taskId) {
        for (Task task : getTasks()) {
            if (task != null && task.getId() != null && task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    public UserStory getUserStory(String id, String calendarId) {
        for (UserStory userStory : getUserStories(calendarId)) {
            if (userStory != null && userStory.getId() != null && (userStory.getId().equals(id))) {
                return userStory;
            }
        }
        return null;
    }
}
