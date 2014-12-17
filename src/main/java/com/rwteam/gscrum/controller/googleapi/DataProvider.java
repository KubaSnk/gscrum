package com.rwteam.gscrum.controller.googleapi;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.parsers.UserStoryParser;
import com.rwteam.gscrum.model.UserStory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector.getInstance;

/**
 * Created by kubasnk on 12/17/14.
 */
public class DataProvider {
    public static void main(String... args){
        try {
            GoogleCalendarConnector.getInstance().connect();
            DataProvider dataProvider = new DataProvider();
            for(UserStory userStory : dataProvider.getUserStories("gscrumteam@gmail.com")){
                System.out.println(userStory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<UserStory> getUserStories(String calendarID) throws IOException {
        List<UserStory> userStories = new ArrayList<>();
        for (Event event : getInstance().getEventsForCalendarID(calendarID)) {
            userStories.add(UserStoryParser.parseUserStory(event));
        }
        return userStories;
    }
}
