package com.app.myapplication.staff_management.Fragments;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.app.myapplication.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ShowPdf extends AppCompatActivity {
    private PDFView pdfView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpdf);
        String id = getIntent().getStringExtra("id");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+id+".pdf");
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(file).load();
    }
}
