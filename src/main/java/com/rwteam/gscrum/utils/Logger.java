package com.rwteam.gscrum.utils;

import java.util.Date;

/**
 * Created by wrabel on 1/14/2015.
 */
public class Logger {
    public static void log(String msg, Class classObj) {
        System.out.println("[" + new Date() + "] " + classObj.getName() + "\n\t\t " + msg);
    }
}
