package com.example.lab;

import java.util.Date;
import java.util.List;

public class NoteController {

    private User user;

    public NoteController(User user) {
        this.user = user;
    }

    // สร้างและเพิ่ม Note เข้า User
    public void addNote(String title, String content, boolean isChecklist) {
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

        note.setCreatedDate(new Date());
        user.addNote(note); // ผูก Note เข้ากับ User พร้อมเซ็ต Owner[cite: 8]
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