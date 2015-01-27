package com.rwteam.gscrum.model;

import java.util.Date;

/**
 * Created by wrabel on 11/30/2014.
 */
public class Task implements Comparable {
    String id;
    String author;
    String name;
    String description;
    String assignedPerson;
    String priority;
    String status;
    Date startDate;
    Date endDate;
    Double estimatedHours;
    Double spentHours;
    UserStory userStory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Double getSpentHours() {
        return spentHours;
    }

    public void setSpentHours(Double spentHours) {
        this.spentHours = spentHours;
    }

    public UserStory getUserStory() {
        return userStory;
    }

    public void setUserStory(UserStory userStoryID) {
        this.userStory = userStoryID;
    }

    @Override
    public String toString() {
        return id;
    }


    public String getAllInfo() {
        return "Task{" +
                "\nid='" + id + '\'' +
                "\nauthor='" + author + '\'' +
                "\nname='" + name + '\'' +
                "\ndescription='" + description + '\'' +
                "\nassignedPerson='" + assignedPerson + '\'' +
                "\npriority='" + priority + '\'' +
                "\nstatus='" + status + '\'' +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\nestimatedHours=" + estimatedHours +
                "\nspentHours=" + spentHours +
                "\nuserStory='" + userStory + '\'' +
                "\n}";

    }

    public com.google.api.services.tasks.model.Task convertToGoogleTask() {
        com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();
        googleTask.setNotes(getDescription());
        googleTask.setTitle("[" + getUserStory() + "] - [" + getId() + "] " + getDescription());

        StringBuilder sbNotes = new StringBuilder();
        sbNotes.append("<task>");
        if (getId() != null) {
            sbNotes.append("\n\t<id>" + getId() + "</id>");
        }
        if (getDescription() != null) {
            sbNotes.append("\n" +
                    "\t<description>" + getDescription() + "</description>");
        }
        if (getAssignedPerson() != null) {
            sbNotes.append("\n" +
                    "\t<assigned_person>" + getAssignedPerson() + "</assigned_person>");
        }
        if (getPriority() != null) {
            sbNotes.append("\n" +
                    "\t<priority>" + getPriority() + "</priority>");
        }
        if (getEstimatedHours() != null) {
            sbNotes.append("\n" +
                    "\t<estimated_hours>" + getEstimatedHours() + "</estimated_hours>");
        }

        sbNotes.append("\n</task>");

        googleTask.setNotes(sbNotes.toString());


        return googleTask;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Task) {
            Task taskToCompare = (Task) o;
            return getId().compareTo(taskToCompare.getId());
        }
        return 0;
    }
}
