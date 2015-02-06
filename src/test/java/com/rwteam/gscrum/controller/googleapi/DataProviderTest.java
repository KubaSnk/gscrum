package com.rwteam.gscrum.controller.googleapi;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.common.collect.Lists;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by mrutski on 14.01.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataProviderTest {

    public static final java.util.Date START_DATE = new GregorianCalendar(2014, Calendar.NOVEMBER, 25).getTime();
    @Mock
    private GoogleCalendarConnector connector;
    @InjectMocks
    private DataProvider dataProvider = new DataProvider();

    @Test
    public void shouldGetUserStories() throws IOException {
        final String calendarId = "1";
        final Event event1 = new Event();
        EventDateTime start = new EventDateTime();
        start.setDateTime(new DateTime(START_DATE));
        event1.setStart(start);
        event1.setDescription("<id>Milestone 1</id>\n" +
                "        <start_date>25-11-2014</start_date>\n" +
                "        <deadline_date>09-12-2014</deadline_date>\n" +
                "        <tasks>\n" +
                "            <task>\n" +
                "                <id>GS-1</id>\n" +
                "                <description>Create task list view</description>\n" +
                "                <assigned_person>mrutski</assigned_person>\n" +
                "                <priority>1</priority>\n" +
                "                <estimated_hours>8h</estimated_hours>\n" +
                "            </task>\n" +
                "            <task>\n" +
                "                <id>GS-2</id>\n" +
                "                <description>Set up google API and prepare DAO interface</description>\n" +
                "                <assigned_person>jwrabel</assigned_person>\n" +
                "                <priority>1</priority>\n" +
                "                <estimated_hours>15h</estimated_hours>\n" +
                "            </task>\n" +
                "        </tasks>");

        when(connector.getEventsForCalendarID(calendarId))
                .thenReturn(Lists.newArrayList(event1));
        com.google.api.services.tasks.model.Task task1 = new com.google.api.services.tasks.model.Task();
        task1.setTitle("GS-1");
        task1.setNotes("         <id>GS-1</id>\n" +
                "                <description>Create task list view</description>\n" +
                "                <assigned_person>mrutski</assigned_person>\n" +
                "                <priority>1</priority>\n" +
                "                <estimated_hours>8h</estimated_hours>\n");
        when(connector.getTasks())
                .thenReturn(Lists.newArrayList(task1));

        List<UserStory> userStories = dataProvider.getUserStories(calendarId);

        assertNotNull(userStories);
//        assertThat(userStories.size(), equalTo(1));

//        UserStory userStory = userStories.get(0);
//        assertThat(userStory.getId(), equalTo("Milestone 1"));
//        assertThat( userStory.getStartDate(), equalTo(START_DATE) );
//        assertThat(userStory.getDeadlineDate(), equalTo(new GregorianCalendar(2014, Calendar.DECEMBER, 9).getTime()));
//
//        List<Task> tasks = userStory.getTaskCollection();
//        assertNotNull(tasks);
//        assertThat(tasks.size(), equalTo(2));
//
//        Task task = tasks.get(1);
//        assertThat(task.getId(), equalTo("GS-1"));
//        assertThat(task.getDescription(), equalTo("Create task list view"));
//        assertThat(task.getAssignedPerson(), equalTo("mrutski"));
//        assertThat(task.getPriority(), equalTo("1"));
//        assertThat(task.getEstimatedHours(), equalTo(8D));
    }


}
