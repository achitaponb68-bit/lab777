package com.example.lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button mButton;
    Button mAddnote;
    Button bBrowsenote;
    ImageView logoImage;
    ProgressBar loadData;

    // สร้าง User เป็น static object เพื่อใช้งานร่วมกันในทุก Activity
    public static User currentUser = new User("A01", "achi", "032", "achi Achitapon");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // event source
        mButton = findViewById(R.id.button);
        mAddnote = findViewById(R.id.button2);
        bBrowsenote = findViewById(R.id.button5);
        // [แก้ไข] แยก Listener ออกจากกัน ไม่ซ้อนกัน
        mAddnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clickkk Addnote");
                Intent addNoteIntent = new Intent(getApplicationContext(), AddnoteActivity.class);
                startActivity(addNoteIntent);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clickkk AboutMe");
                Intent aboutMeIntent = new Intent(getApplicationContext(), AboutMeActivity.class);
                startActivity(aboutMeIntent);
            }
        });
        loadData = findViewById(R.id.progressBar);
        loadData.setVisibility(View.GONE);
        bBrowsenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show progress bar
                loadData.setVisibility(View.VISIBLE);

                //Create Thread
                new Thread(()->{
                    //load data from DB (deley 4 seconds)
                    try {
                        Thread.sleep(4000);
                    }catch (InterruptedException e){
                    }
                    //return to main Thread
                    runOnUiThread(()->{
                        loadData.setVisibility(View.GONE);
                        Intent BrowseNote = new Intent(getApplicationContext(), BrowseNote.class);
                        startActivity(BrowseNote);
                        finish();
                    });
        }).start();
        logoImage = findViewById(R.id.imageView);
        logoImage.setImageResource(R.drawable.sssss);

    }
});
    }
}
