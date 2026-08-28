package com.example.lab;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class NoteController {

    private User user;

    public NoteController(User user) {
        this.user = user;
    }

    // สร้างและเพิ่ม Note เข้า User พร้อมบันทึกลง Database
    public void addNote(String title, String content, boolean isChecklist, View mView) {
        Note note;

        if (isChecklist || content.contains(",")) {
            CheckListNote checkListNote = new CheckListNote(title, content);
            String[] items = content.split("[,\\n]");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    checkListNote.getCheckList().add(item.trim());
                }
            }
            note = checkListNote;
        } else {
            note = new TextNote(title, content);
        }

        // 1. กำหนดวันที่และผูกโน้ตเข้ากับ User
        note.setCreatedDate(new Date());
        user.addNote(note); // ผูก Note เข้ากับ User พร้อมเซ็ต Owner

        // 2. แปลงเป็น Entity (NoteMapper.toEntity รองรับทั้ง TextNote และ CheckListNote)
        NoteEntity entity = NoteMapper.toEntity(note);

        // 3. บันทึกลง Database พร้อม Log แจ้งสถานะ
        Context context = mView.getContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // บันทึกข้อมูลลง Database
                AppDatabase.getInstance(context).noteDao().insert(entity);
                Log.d("DatabaseAction", "Insert successful: Note saved to database.");
            } catch (Exception e) {
                // กรณีเกิด Error ระหว่างบันทึก
                Log.e("DatabaseAction", "Error inserting note: " + e.getMessage());
            }
        });
    }

    // ดึงสรุปข้อมูลโน้ตทั้งหมดมาร้อยเรียงเป็น String สำหรับนำไปแสดงผลที่ View
    public String getFormattedNotes() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- โน้ตทั้งหมดของ ").append(user.getFullName()).append(" ---\n\n");

        List<Note> notes = user.getAllNote();
        for (Note n : notes) {
            sb.append(n.getSummary()).append("\n-----------------------------------\n");
        }
        return sb.toString();
    }
}