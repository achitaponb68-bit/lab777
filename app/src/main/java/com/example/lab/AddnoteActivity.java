package com.example.lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class AddnoteActivity extends AppCompatActivity {

    EditText Title, Content;
    Button addNote;
    TextView showNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addnote);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ผูก View ตาม ID ใน activity_addnote.xml จริง
        Title = findViewById(R.id.editTextText);
        Content = findViewById(R.id.editTextText2);
        addNote = findViewById(R.id.button4);
        showNote = findViewById(R.id.textView2);

        // แสดงรายการโน้ตที่มีอยู่เดิมตอนเปิดหน้าขึ้นมา
        updateNoteDisplay();

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from user
                String strofTitle = Title.getText().toString().trim();
                String strofContent = Content.getText().toString().trim();

                if (strofTitle.isEmpty()) {
                    Toast.makeText(AddnoteActivity.this, "กรุณากรอกชื่อเรื่อง", Toast.LENGTH_SHORT).show();
                    return;
                }

                Note note;

                // ตรวจสอบ: ถ้าในเนื้อหามีเครื่องหมาย comma (,) ให้สร้างเป็น CheckListNote
                if (strofContent.contains(",")) {
                    CheckListNote checkListNote = new CheckListNote(strofTitle, strofContent);
                    String[] items = strofContent.split(",");
                    for (String item : items) {
                        checkListNote.getCheckList().add(item.trim());
                    }
                    note = checkListNote;
                } else {
                    // หากไม่มี comma ให้สร้างเป็น TextNote ปกติ
                    note = new TextNote(strofTitle, strofContent);
                }

                note.setCreatedDate(new Date());

                // [ข้อ 3] บันทึก Note ลงใน User
                MainActivity.currentUser.addNote(note);

                // อัปเดตการแสดงผลบน TextView
                updateNoteDisplay();

                // เคลียร์ช่องป้อนข้อมูล
                Title.setText("");
                Content.setText("");

                Toast.makeText(AddnoteActivity.this, "เพิ่มโน้ตเรียบร้อย!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // เมธอดดึงรายการโน้ตทั้งหมดของ User มาแสดงบน TextView2
    private void updateNoteDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- โน้ตทั้งหมดของ ").append(MainActivity.currentUser.getFullName()).append(" ---\n\n");

        for (Note n : MainActivity.currentUser.getAllNote()) {
            sb.append(n.getSummary()).append("\n-----------------------------------\n");
        }

        showNote.setText(sb.toString());
    }
}