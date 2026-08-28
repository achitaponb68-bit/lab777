package com.example.lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddnoteActivity extends AppCompatActivity {

    private EditText Title, Content;
    private Button addNote;
    private TextView showNote;
    private CheckBox checkBoxIsChecklist;

    // ประกาศ Controller สำหรับจัดการ Business Logic
    private NoteController noteController;

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

        Title = findViewById(R.id.editTextText);
        Content = findViewById(R.id.editTextText2);
        addNote = findViewById(R.id.button4);
        showNote = findViewById(R.id.textView2);
        checkBoxIsChecklist = findViewById(R.id.checkBoxIsChecklist);

        // สร้าง Controller โดยส่ง User ปัจจุบันเข้าไป[cite: 2, 7]
        noteController = new NoteController(MainActivity.currentUser);

        // แสดงผลโน้ตเริ่มต้นเมื่อเปิดหน้า Activity
        updateNoteDisplay();

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strofTitle = Title.getText().toString().trim();
                String strofContent = Content.getText().toString().trim();

                if (strofTitle.isEmpty()) {
                    Toast.makeText(AddnoteActivity.this, "กรุณากรอกชื่อเรื่อง", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ส่งข้อมูลไปบันทึกผ่าน Controller (ส่ง v เพื่อไปดึง Context บันทึกลง DB)
                noteController.addNote(strofTitle, strofContent, checkBoxIsChecklist.isChecked(), v);

                // อัปเดตการแสดงผลบน TextView[cite: 2]
                updateNoteDisplay();

                // เคลียร์ค่า UI[cite: 2]
                Title.setText("");
                Content.setText("");
                checkBoxIsChecklist.setChecked(false);

                Toast.makeText(AddnoteActivity.this, "เพิ่มโน้ตเรียบร้อย!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoteDisplay() {
        // ดึงข้อความสรุปโน้ตที่ฟอร์แมตแล้วจาก Controller มาแสดงผล (ไม่ต้องส่ง Parameter)[cite: 2]
        showNote.setText(noteController.getFormattedNotes());
    }
}