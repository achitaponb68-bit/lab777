package com.example.lab;

import java.util.ArrayList;
import java.util.List;

public class CheckListNote extends Note {
    private List<String> checkList;

    public CheckListNote(String title, String content) {
        super(title, content);
        this.checkList = new ArrayList<>();
    }

    public List<String> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<String> checkList) {
        this.checkList = checkList;
    }

    @Override
    public String getSummary() {
        // [ข้อ 3] ดึงชื่อ User มาแสดงผล
        String ownerName = (getOwner() != null) ? getOwner().getFullName() : "ไม่ระบุเจ้าของ";
        return "[" + formatDate() + "] " + getTitle() + " (เช็คลิสต์ " + checkList.size() + " รายการ): " + checkList.toString() + " | เจ้าของ: " + ownerName;
    }

    public List<String> getItems() {
        return checkList; // คืนค่ารายการ checkList ออกไป
    }
}