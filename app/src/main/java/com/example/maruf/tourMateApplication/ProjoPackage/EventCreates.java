package com.example.maruf.tourMateApplication.ProjoPackage;

public class EventCreates {
    private String id;
    private String eventName;
    private String fromDate;
    private String toDate;
    private Double estimatedBudget;




    public EventCreates(String id, String eventName, String fromDate, String toDate, Double estimatedBudget) {
        this.id = id;
        this.eventName = eventName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.estimatedBudget = estimatedBudget;
        }

    public String getEventName() {
        return eventName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public Double getEstimatedBudget() {
        return estimatedBudget;
    }


    public EventCreates() {
    }

    public String getId() {
        return id;
    }


}
