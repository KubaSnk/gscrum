package com.rwteam.gscrum.utils;

import com.rwteam.gscrum.controller.googleapi.GoogleCalendarConnector;

import java.io.IOException;
import java.util.Date;

/**
 * Created by wrabel on 1/14/2015.
 */
public class Logger {
    private final Class classObj;

    public Logger(Class classObj){
        this.classObj = classObj;
    }
    public void log(String msg) {
        System.out.println("[" + new Date() + "] " + classObj.getName() + "\n\t\t " + msg);
    }

    public void logError(IOException e) {
        System.err.println("[" + new Date() + "] " + classObj.getName() + "\n\t\t " + e.getMessage());
    }
}
