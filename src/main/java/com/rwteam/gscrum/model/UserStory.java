package com.rwteam.gscrum.model;

import java.util.Collection;
import java.util.Date;

/**
 * Created by wrabel on 12/1/2014.
 */
public class UserStory {
    String id;
    String author;
    String title;
    String description;
    String priority;
    String status;
    Date startDate;
    Date endDate;
    Date deadlineDate;
    Integer estimatedStoryPoints;
    Integer spentStoryPoints;
    Collection<Task> taskCollection;

    public Collection<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(Collection<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Integer getEstimatedStoryPoints() {
        return estimatedStoryPoints;
    }

    public void setEstimatedStoryPoints(Integer estimatedStoryPoints) {
        this.estimatedStoryPoints = estimatedStoryPoints;
    }

    public Integer getSpentStoryPoints() {
        return spentStoryPoints;
    }

    public void setSpentStoryPoints(Integer spentStoryPoints) {
        this.spentStoryPoints = spentStoryPoints;
    }

    @Override
    public String toString() {
        return id;
    }

//    @Override
//    public String toString() {
//        return "UserStory{" +
//                "id='" + id + '\'' +
//                ", author='" + author + '\'' +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", priority='" + priority + '\'' +
//                ", status='" + status + '\'' +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", deadlineDate=" + deadlineDate +
//                ", estimatedStoryPoints=" + estimatedStoryPoints +
//                ", spentStoryPoints=" + spentStoryPoints +
//                ", taskCollection=" + taskCollection +
//                '}';
//    }
}
