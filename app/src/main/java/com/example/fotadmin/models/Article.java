package com.example.fotadmin.models;

public class Article {
    private String id;         // Firebase key
    private String title;
    private String summary;
    private String date;
    private String imageUrl;   // URL from Firebase Storage

    public Article() {
        // Required empty constructor for Firebase
    }

    public Article(String id, String title, String summary, String date, String imageUrl) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
