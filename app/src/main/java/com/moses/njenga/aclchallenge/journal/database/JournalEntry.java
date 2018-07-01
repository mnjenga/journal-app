package com.moses.njenga.aclchallenge.journal.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journal")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private int mood;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    private Date createdAt;

    @Ignore
    public JournalEntry(String title, String content,Date createdAt, int mood, Date updatedAt) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public JournalEntry(int id, String title,String content, int mood, Date updatedAt) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.mood = mood;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
