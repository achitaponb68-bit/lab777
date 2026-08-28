package com.example.lab;

public class TextNote extends Note {

    public TextNote(String title, String content) {
        super(title, content);
    }

    @Override
    public String getSummary() {
        String content = getContent();
        String shortContent = content.length() > 30
                ? content.substring(0, 30) + "..."
                : content;

        // [ข้อ 3] ดึงชื่อ User มาแสดงผล
        String ownerName = (getOwner() != null) ? getOwner().getFullName() : "ไม่ระบุเจ้าของ";
        return "[" + formatDate() + "] " + getTitle() + " (ข้อความ): " + shortContent + " | เจ้าของ: " + ownerName;
    }

    public String getTextContent() {
        return getContent();
    }
}