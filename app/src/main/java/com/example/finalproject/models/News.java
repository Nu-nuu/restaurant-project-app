package com.example.finalproject.models;

public class News {
    private String title;
    private String content;
    private String time;
    private String author;
    private String imageUrl;

    public News(String title, String content, String time, String author, String imageUrl) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", author='" + author + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
