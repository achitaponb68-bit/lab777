package com.example.lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox; // [เพิ่ม Import]
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
    CheckBox checkBoxIsChecklist; // [เพิ่ม 1]: ตัวแปร CheckBox

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
        checkBoxIsChecklist = findViewById(R.id.checkBoxIsChecklist); // [เพิ่ม 2]: ผูก ID

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

                Note note;

                // [เพิ่ม 3]: เช็กถ้ามีการติ๊กถูก หรือ พิมพ์ comma ให้เป็น CheckListNote
                if (checkBoxIsChecklist.isChecked() || strofContent.contains(",")) {
                    CheckListNote checkListNote = new CheckListNote(strofTitle, strofContent);
                    String[] items = strofContent.split("[,\\n]");
                    for (String item : items) {
                        if (!item.trim().isEmpty()) {
                            checkListNote.getCheckList().add(item.trim());
                        }
                    }
                    note = checkListNote;
                } else {
                    note = new TextNote(strofTitle, strofContent);
                }

                note.setCreatedDate(new Date());
                MainActivity.currentUser.addNote(note);

                updateNoteDisplay();

                Title.setText("");
                Content.setText("");
                checkBoxIsChecklist.setChecked(false); // [เพิ่ม 4]: เคลียร์สถานะติ๊กถูกหลังบันทึก

                Toast.makeText(AddnoteActivity.this, "เพิ่มโน้ตเรียบร้อย!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoteDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- โน้ตทั้งหมดของ ").append(MainActivity.currentUser.getFullName()).append(" ---\n\n");

        for (Note n : MainActivity.currentUser.getAllNote()) {
            sb.append(n.getSummary()).append("\n-----------------------------------\n");
        }

        showNote.setText(sb.toString());
    }
}