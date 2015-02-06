package com.rwteam.gscrum.model;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wrabel on 12/1/2014.
 */
public class UserStory implements Comparable {
    public static final UserStory EMPTY = new UserStory();

    static {
        EMPTY.setGoogleApiId("");
        EMPTY.setEndDate(new Date());
        EMPTY.setStartDate(new Date());
        EMPTY.setId("Set US name");
        EMPTY.setDescription("Enter description");
    }

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
    List<Task> taskCollection;
    private String googleApiId;

    public List<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(List<Task> taskCollection) {
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
//        getTaskCollection().stream().filter(e -> e.endDate.equals(e.startDate)).forEach(e -> System.out.println(e));
        return id;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof UserStory) {
            UserStory userStoryToCompare = (UserStory) o;
            return id.compareTo(userStoryToCompare.getId());
        }
        return 0;
    }

    public String getAllInfo() {

        return "UserStory{" +
                "\nid='" + id + '\'' +
                "\nauthor='" + author + '\'' +
                "\ndescription='" + description + '\'' +
                "\npriority='" + priority + '\'' +
                "\nstatus='" + status + '\'' +
                "\nstartDate=" + startDate +
                "\nendDate=" + endDate +
                "\n}";
    }

    public String getGoogleApiId() {
        return googleApiId;
    }

    public void setGoogleApiId(String googleApiId) {
        this.googleApiId = googleApiId;
    }

    public Map<String, String> getChangesSet(UserStory userStoryToCompare) {
        Map<String, String> changes = new ArrayMap<>();

        addToChangesetIfDifferent(getAuthor(), userStoryToCompare.getAuthor(), "author", changes);
        addToChangesetIfDifferent(getDescription(), userStoryToCompare.getDescription(), "description", changes);
        addToChangesetIfDifferent(getPriority(), userStoryToCompare.getPriority(), "priority", changes);
        addToChangesetIfDifferent(getStatus(), userStoryToCompare.getStatus(), "status", changes);
        addToChangesetIfDifferent(getEndDate(), userStoryToCompare.getEndDate(), "endDate", changes);
        addToChangesetIfDifferent(getStartDate(), userStoryToCompare.getStartDate(), "startDate", changes);

        return changes;
    }

    private void addToChangesetIfDifferent(Object value1, Object value2, String key, Map<String, String> changes) {
        if (value1 == null && value2 != null || value1 != null && !value1.equals(value2)) {
            changes.put(key, value1 + " -> " + value2);
        }
    }

    public Event convertToGoogleEventy() {
        Event event = new Event();
        event.setId(getGoogleApiId());
        StringBuilder sbNotes = new StringBuilder();
        sbNotes.append("<user_story>");
        event.setSummary(getId());

//        if (getId() != null) {
//            sbNotes.append("\n\t<id>" + getId() + "</id>");
//        }
        if (getDescription() != null) {
            sbNotes.append("\n" +
                    "\t<description>" + getDescription() + "</description>");
        }

        if (getAuthor() != null) {
            sbNotes.append("\n" +
                    "\t<author>" + getAuthor() + "</author>");
        }
        if (getPriority() != null) {
            sbNotes.append("\n" +
                    "\t<priority>" + getPriority() + "</priority>");
        }
        if (getStatus() != null) {
            sbNotes.append("\n" +
                    "\t<status>" + getStatus() + "</status>");
        }

        sbNotes.append("\n\t<tasks>");
        if (getTaskCollection() != null) {
            for (Task taks : getTaskCollection()) {
                sbNotes.append("\n\t\t<task><id>");
                sbNotes.append("\n\t\t\t" + taks.getId());
                sbNotes.append("\n\t\t</id></task>");
            }
        }
        sbNotes.append("\n\t</tasks>");
        EventDateTime startDate = new EventDateTime();
//        startDate.setDateTime()
//        new EventDateTime().setDate(new DateTime(true, new Date(), 0));
        startDate.setDateTime(new DateTime(getStartDate()));
        event.setStart(startDate);

//        event.set("start", "01-01-2015");
//        event.set("end", "01-12-2015");
//
        EventDateTime endDate = new EventDateTime();
        endDate.setDateTime(new DateTime(getEndDate()));
        event.setEnd(endDate);


        sbNotes.append("\n</user_story>");

        event.setDescription(sbNotes.toString());


        return event;
    }

    public void removeTask(Task old) {
        if (taskCollection != null) {
            for (int i = 0; i < getTaskCollection().size(); i++) {
                Task current = getTaskCollection().get(i);
                if (current.getId().equals(old.getId())) {
                    getTaskCollection().remove(i);
                    break;
                }
            }
        }
    }
}
