package com.rwteam.gscrum.controller.googleapi;

import com.google.api.services.calendar.model.Event;
import com.google.common.collect.Lists;
import com.rwteam.gscrum.model.UserStory;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by mrutski on 14.01.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataProviderTest {

    @Mock
    private GoogleCalendarConnector connector;
    @InjectMocks
    private DataProvider dataProvider = new DataProvider();

    @Test
    public void shouldGetUserStories() throws IOException {
        final String calendarId = "1";
        final Event event1 = new Event();
        event1.setDescription("<id>Event1</id><tasks><task></task></tasks>");
        final Event event2 = new Event();
        event2.setDescription("<id>Event2</id><tasks><task></task></tasks>");

        when(connector.getEventsForCalendarID(calendarId))
                .thenReturn(Lists.newArrayList(event1, event2));

        List<UserStory> userStories = dataProvider.getUserStories(calendarId);

        assertThat(userStories.size(), equalTo(2));
        assertThat(userStories.get(0).getId(), equalTo("Event1"));
        assertThat(userStories.get(1).getId(), equalTo("Event2"));
    }


}
