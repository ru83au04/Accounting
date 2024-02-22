package com.example.newproject.helper;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Expense{
    private long id;
    private String title;
    private double cost;
    private Type type;
    private Date date;
    private String description;

    public Expense(String title, double cost, Type type, Date date, String description){
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
    public Date getDate() {
        return date;
    }
    public String getDateByString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(this.date);
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
