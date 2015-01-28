package com.rwteam.gscrum.controller.parsers;

import com.google.api.services.tasks.model.Task;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author mateusz.rutski@sagiton.pl
 */
public class TaskParserTest {

    public static Task TASK;

    @Before
    public void setUpTask() {
        TASK = new Task();
        TASK.setTitle("GS-1");
        TASK.setNotes("         <id>GS-1</id>\n" +
                "                <description>Create task list view</description>\n" +
                "                <assigned_person>mrutski</assigned_person>\n" +
                "                <priority>1</priority>\n" +
                "                <estimated_hours>8h</estimated_hours>\n");
    }

    @Test
    public void shouldParseTask() {

        com.rwteam.gscrum.model.Task task = TaskParser.parseTask(TASK);

        assertThat(task.getId(), equalTo("GS-1"));
        assertThat(task.getDescription(), equalTo("Create task list view"));
        assertThat(task.getAssignedPerson(), equalTo("mrutski"));
        assertThat(task.getPriority(), equalTo("1"));
        assertThat(task.getEstimatedHours(), equalTo(8d));
    }
}
