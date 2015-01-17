package com.rwteam.gscrum.utils;

import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;

import java.io.IOException;
import java.util.List;

/**
 * Created by wrabel on 1/14/2015.
 */
public class CalendarUtils {
    static void display(CalendarList feed) throws IOException {
        if (feed.getItems() != null) {
            for (CalendarListEntry entry : feed.getItems()) {

                System.out.println();
                System.out.println("-----------------------------------------------");
                display(entry);
//                List<Event> items = GoogleCalendarConnector.getInstance().get.events().list(entry.getId()).execute().getItems();
//                for (Event ev : items) {
//                    System.out.println(ev);
//                }
            }
        }
    }

    static void display(CalendarListEntry entry) {
        System.out.println("ID: " + entry.getId());
        System.out.println("Summary: " + entry.getSummary());
        if (entry.getDescription() != null) {
            System.out.println("Description: " + entry.getDescription());
        }
    }

}
