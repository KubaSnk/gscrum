package com.rwteam.gscrum.controller.parsers;

import com.google.api.services.calendar.model.Event;
import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * @author mateusz.rutski@sagiton.pl
 */
@RunWith(MockitoJUnitRunner.class)
public class UserStoryParserTest {

    @Mock
    private DataProvider dataProvider;

    private Event event;
    private Task task;

    @Before
    public void setUp() {
        event = new Event();

        task = new Task();
    }

    @Test
    public void shouldParseUserStory() {

        when(dataProvider.getTask(anyString()))
                .thenReturn(task);

        UserStory userStory = UserStoryParser.parseUserStory(event, dataProvider);

        assertThat(userStory.getId(), equalTo(""));

    }
}
