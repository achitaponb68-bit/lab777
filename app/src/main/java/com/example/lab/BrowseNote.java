package com.example.lab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class BrowseNote extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_note);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. ผูก View จาก XML
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        tvSearchResult = findViewById(R.id.tvSearchResult);

        // ซ่อน ProgressBar และล้างข้อความเริ่มต้น
        progressBar.setVisibility(View.GONE);
        tvSearchResult.setText("");

        // 2. เมื่อกดปุ่ม Search ให้ดึงข้อความที่พิมพ์ไปค้นหาใน Database
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ดึงข้อความจากช่องพิมพ์ etSearch
                String query = etSearch.getText().toString().trim().toLowerCase();

                // แสดง ProgressBar และล้างข้อความเดิมก่อนค้นหา
                progressBar.setVisibility(View.VISIBLE);
                tvSearchResult.setText("");

                // ค้นหาข้อมูลใน Background Thread
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        // ดึงข้อมูลทั้งหมดจาก Database
                        List<NoteEntity> entities = AppDatabase.getInstance(BrowseNote.this).noteDao().getAll();
                        List<Note> matchedNotes = new ArrayList<>();

                        for (NoteEntity e : entities) {
                            Note note = NoteMapper.fromEntity(e);
                            String title = (note.getTitle() != null) ? note.getTitle().toLowerCase() : "";
                            String content = (note.getContent() != null) ? note.getContent().toLowerCase() : "";
                            String owner = (note.getOwner() != null && note.getOwner().getFullName() != null)
                                    ? note.getOwner().getFullName().toLowerCase()
                                    : "unknown user";

                            // กรองข้อมูล: ถ้าข้อความที่พิมพ์ตรงกับ Title, Content หรือ Owner ให้เก็บเข้ารายการ
                            if (query.isEmpty() || title.contains(query) || content.contains(query) || owner.contains(query)) {
                                matchedNotes.add(note);
                            }
                        }

                        // พ่น Log ใน Android Studio
                        Log.d("DatabaseAction", "Insert successful: Note saved to database.");

                        // อัปเดตผลลัพธ์บน UI Thread
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);

                            // ถ้าไม่พบข้อมูลที่ตรงกัน
                            if (matchedNotes.isEmpty()) {
                                tvSearchResult.setText("ไม่พบข้อมูล");
                            } else {
                                // แสดงรายการที่ค้นพบตามรูปแบบที่กำหนด
                                StringBuilder sb = new StringBuilder();
                                for (Note n : matchedNotes) {
                                    String ownerName = (n.getOwner() != null) ? n.getOwner().getFullName() : "Unknown User";
                                    sb.append("Owner: ").append(ownerName).append("\n");
                                    sb.append("Title: ").append(n.getTitle()).append("\n");
                                    sb.append("Date: ").append(n.getCreatedDate()).append("\n\n");
                                }
                                tvSearchResult.setText(sb.toString().trim());
                            }
                        });
                    } catch (Exception e) {
                        Log.e("DatabaseAction", "Error loading notes: " + e.getMessage());
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            tvSearchResult.setText("ไม่พบข้อมูล");
                        });
                    }
                });
            }
        });
    }
}