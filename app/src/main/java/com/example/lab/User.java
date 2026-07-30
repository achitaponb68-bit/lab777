package com.example.lab;

import java.util.ArrayList;
import java.util.List;

public class User {
    // Attributes
    private String userid;
    private String username;
    private String userPassword;
    private String fullName;
    private final List<Note> notes;

    // Method
    public User(String userid, String username, String userPassword, String fullName){
        this.userid = userid;
        this.username = username;
        this.userPassword = userPassword;
        this.fullName = fullName;
        this.notes = new ArrayList<>();
    }

    public String getUserid(){ return userid; }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getFullName(){ return fullName; }
    public void setFullName(String fullName){ this.fullName = fullName; }

    // [ข้อ 3] ผูก Note เข้ากับ User นี้ทันทีที่ถูกเพิ่ม
    public void addNote(Note note){
        note.setOwner(this);
        notes.add(note);
    }

    public void deleteNote(Note note){
        notes.remove(note);
    }

    public List<Note> getAllNote(){
        return notes;
    }
}