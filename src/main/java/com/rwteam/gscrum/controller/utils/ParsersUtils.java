package com.rwteam.gscrum.controller.utils;

/**
 * Created by wrabel on 1/13/2015.
 */
public class ParsersUtils {

    public static String cutoutValueFromTag(String value, String tag) {
        String result = "";

        value = value.trim();
        String[] split = value.split("<" + tag + ">");
        if (split.length > 1) {
            result = split[1].trim().split("</" + tag + ">")[0].trim();
        }
        return result;
    }
}
