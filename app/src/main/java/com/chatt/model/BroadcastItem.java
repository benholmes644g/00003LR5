package com.chatt.model;

/**
 * Created by mithramedia on 09/06/16.
 */
public class BroadcastItem {

    private String name;
    private String title;
    private String icon;
    private boolean online;
    private String date;
    private String third;
    private String language;

    public BroadcastItem(String name, String title, String icon, boolean online, String date,String third, String language){

    this.name = name;
        this.title = title;
        this.icon = icon;
        this.online = online;
        this.date = date;
        this.third = third;
        this.language = language;
    }

    public String getName() { return name; }
    public void setName(String name) {  this.name=name; }
    public String getTitle() {return title; }
    public void setTitle(String title) {  this.title=title; }
    public String getIcon() {return  icon; }
    public void setIcon(String icon) { this.icon=icon; }
    public boolean isOnline() {return  online;}
    public void setOnline(boolean online) { this.online=online; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date=date; }
    public String getThird() { return third; }
    public void setThird(String third) { this.third=third; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language=language; }



}
