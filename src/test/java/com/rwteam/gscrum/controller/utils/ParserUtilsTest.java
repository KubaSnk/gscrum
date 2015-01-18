package com.rwteam.gscrum.controller.utils;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mrutski on 14.01.15.
 */
public class ParserUtilsTest {

    private static final String VALUE = "<userStory>" +
                "<id>1</id>" +
                "<description>Desc</description>" +
            "</userStory>";
    private static final String TAG = "description";

    @Test
    public void shouldCutoutValueFromTag() {
        String value = ParsersUtils.cutoutValueFromTag(VALUE, TAG);
        Assert.assertThat(value, CoreMatchers.equalTo("Desc"));
    }
}
