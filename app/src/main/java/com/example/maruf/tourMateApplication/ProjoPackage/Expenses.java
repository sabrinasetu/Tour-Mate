package com.example.maruf.tourMateApplication.ProjoPackage;

public class Expenses extends EventCreates {
    private String details;
    private double amount;

    public Expenses() {

    }

    public Expenses(String details, double amount) {
        this.details = details;
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public double getAmount() {
        return amount;
    }
}
