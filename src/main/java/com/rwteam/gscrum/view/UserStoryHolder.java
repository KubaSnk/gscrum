package com.rwteam.gscrum.view;

import com.rwteam.gscrum.model.UserStory;

/**
 * Created by wrabel on 1/14/2015.
 */
public class UserStoryHolder {
    public static final String UNKNOWN = "--- not defined ---";
    UserStory userStory;

    @Override
    public String toString() {
        if (userStory == null) {
            return UNKNOWN;
        } else {
            return userStory.getId();
        }
    }

    public UserStoryHolder(UserStory userStory) {
        this.userStory = userStory;
    }

    public static String getUnknown() {

        return UNKNOWN;
    }

    public UserStory getUserStory() {
        return userStory;
    }

    public void setUserStory(UserStory userStory) {
        this.userStory = userStory;
    }
}
