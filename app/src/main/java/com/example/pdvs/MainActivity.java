package com.example.pdvs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdvs.Adapter.DocsAdapter;
import com.example.pdvs.Model.DocModel;
import com.example.pdvs.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView docsRecyclerView;
    private DocsAdapter docAdapter;
    private FloatingActionButton fab;
    private DataBaseHandler db;
    private List<DocModel> docList;

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
        db = new DataBaseHandler(this);
        db.openDatabase();


        docList = new ArrayList<>();


        docsRecyclerView = findViewById(R.id.docsRecyclerView);
        docsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        docAdapter = new DocsAdapter(db, this);
        docsRecyclerView.setAdapter(docAdapter);
//         Testavimui
//        DocModel doc = new DocModel();
//        doc.setDocInfo("some text for test");
//        doc.setStatus(0);
//        doc.setId(1);
//
//        docList.add(doc);
//        docList.add(doc);
//        docList.add(doc);
//        docList.add(doc);
//
//        docAdapter.setDocs(docList);
        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(docAdapter));
        itemTouchHelper.attachToRecyclerView(docsRecyclerView);

        docList = db.getAllDoc();
        Collections.reverse(docList);
        docAdapter.setDocs(docList);

        fab.setOnClickListener(v -> AddNewDoc.newInstance().show(getSupportFragmentManager(), AddNewDoc.TAG));
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog){
        docList = db.getAllDoc();
        Toast.makeText(this, "docList returned", Toast.LENGTH_SHORT).show();
        Collections.reverse(docList);
        docAdapter.setDocs(docList);
        docAdapter.notifyDataSetChanged();
    }
}