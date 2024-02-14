package com.example.newproject.helper;

import java.util.ArrayList;
import java.util.Date;

public class Expense {
    public static ArrayList<Expense> expenseArrayList = new ArrayList<>();
    private long id;
    private String title;
    private double cost;
    private Type type;
    private String date;
    private String description;

    public Expense(String title, double cost, Type type, String date, String description){
        this.type = type;
        this.title = title;
        this.cost = cost;
        this.date = date;
        this.description = description;
    }


    public long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public double getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
    public void setId(long id){
        this.id = id;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
