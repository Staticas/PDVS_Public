package com.example.pdvs;

import static com.example.pdvs.AddNewDoc.convertCompressedByteArrayToBitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    ImageView imageViewBig;
    TextView textViewBig;
    Bitmap bitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewBig = findViewById(R.id.imageViewBig);
        textViewBig = findViewById(R.id.textViewBig);


        bitmap = convertCompressedByteArrayToBitmap(getIntent().getByteArrayExtra("imageBites"));
        String text = getIntent().getStringExtra("docInfo");



        imageViewBig.setImageBitmap(bitmap);
        textViewBig.setText(text);
    }



}