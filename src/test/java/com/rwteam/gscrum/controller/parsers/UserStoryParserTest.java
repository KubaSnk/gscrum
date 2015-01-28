package com.rwteam.gscrum.controller.parsers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.rwteam.gscrum.controller.googleapi.DataProvider;
import com.rwteam.gscrum.model.Task;
import com.rwteam.gscrum.model.UserStory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * @author mateusz.rutski@sagiton.pl
 */
@RunWith(MockitoJUnitRunner.class)
public class UserStoryParserTest {

    public static final DateTime START_DATE = new DateTime(1);
    public static final DateTime END_DATE = new DateTime(3);

    @Mock
    private DataProvider dataProvider;

    private Event event;
    private Task task;

    @Before
    public void setUp() {
        event = new Event();
        event.setDescription(
                "<user_story>" +
                        "<id>Milestone 1</id>" +
                        "<deadline_date>10-12-2014</deadline_date>" +
                        "<tasks>" +
                            "<task>" +
                                "<id>1</id>" +
                            "</task>" +
                        "</tasks>" +
                "</user_story>"
        );
        EventDateTime start = new EventDateTime();
        start.setDateTime(START_DATE);
        event.setStart(start);

        EventDateTime end = new EventDateTime();
        end.setDateTime(END_DATE);
        event.setEnd(end);

        task = new Task();
        task.setId("1");
    }

    @Test
    public void shouldParseUserStory() throws ParseException {

        when(dataProvider.getTask(anyString()))
                .thenReturn(task);

        UserStory userStory = UserStoryParser.parseUserStory(event, dataProvider);

        assertThat(userStory.getId(), equalTo("Milestone 1"));
        assertThat(userStory.getDeadlineDate(), equalTo(org.apache.commons.lang3.time.DateUtils.parseDate("10-12-2014", "dd-MM-yyyy")));
        assertThat(userStory.getStartDate().getTime(), equalTo(START_DATE.getValue()));
        assertThat(userStory.getEndDate().getTime(), equalTo(END_DATE.getValue()));
        assertThat(userStory.getTaskCollection().size(), equalTo(1));
        Task task1 = userStory.getTaskCollection().get(0);
        assertThat(task1.getId(), equalTo("1"));

    }
}
