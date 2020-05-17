package com.example.artlet_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;

public class PdfReader extends Activity implements OnPageChangeListener,OnLoadCompleteListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        Intent intent = getIntent();
        String filePath = intent.getStringExtra("pdfPath");
        Log.d("PATH: ", filePath);
        pdfView = (PDFView)findViewById(R.id.pdfView);


        File file = new File(filePath);

        String pdfName = "";
        for(int i = 0; i < filePath.length(); i++) {
            if(filePath.charAt(i) == '/') {
                pdfName = "";
            } else {
                pdfName += filePath.charAt(i);
            }
        }
        EditText pdfTitle = (EditText) findViewById(R.id.pdfName);
        pdfTitle.setText(pdfName);
        Log.d("asdsa", pdfName);
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
    }


}