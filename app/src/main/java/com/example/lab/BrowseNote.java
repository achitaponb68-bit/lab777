package com.example.lab;

import android.os.Bundle;
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

        // 2. ตั้งค่า Event Listener เมื่อกดปุ่ม Search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // แสดง ProgressBar และเคลียร์ข้อความเดิมออกก่อนเริ่มค้นหา
                progressBar.setVisibility(View.VISIBLE);
                tvSearchResult.setText("");

                // 3. ใช้ Thread ในการจำลองการโหลดข้อมูล
                new Thread(() -> {
                    try {
                        // ดีเลย์ 2 วินาที (2000 milliseconds)
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 4. เมื่อโหลดเสร็จ กลับมาอัปเดต UI ที่ Main Thread
                    runOnUiThread(() -> {
                        // ซ่อน ProgressBar และแสดงข้อความ "ไม่พบข้อมูล"
                        progressBar.setVisibility(View.GONE);
                        tvSearchResult.setText("ไม่พบข้อมูล");
                    });
                }).start();
            }
        });
    }
}