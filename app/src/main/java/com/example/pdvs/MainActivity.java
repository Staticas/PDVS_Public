package com.example.pdvs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView docsRecyclerView;
    private DocsAdapter docAdapter;
    private FloatingActionButton fab;
    private DataBaseHandler db;
    private List<DocModel> docList;
    TinyDB tinyDB;

    TinyDBManager TDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tinyDB= new TinyDB(this);
        TDB = new TinyDBManager(tinyDB);


        docList = new ArrayList<>();


        docsRecyclerView = findViewById(R.id.docsRecyclerView);
        docsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        docAdapter = new DocsAdapter(TDB, this);
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

        docList = TDB.getDocList();
        Collections.reverse(docList);
        docAdapter.setDocs(docList);

        fab.setOnClickListener(v -> AddNewDoc.newInstance(TDB).show(getSupportFragmentManager(), AddNewDoc.TAG));
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog){
        docList = TDB.getDocList();
        Toast.makeText(this, "docList returned", Toast.LENGTH_SHORT).show();
        Collections.reverse(docList);
        docAdapter.setDocs(docList);
        TDB.putDocList((ArrayList<DocModel>) docList);
//        docList = TDB.getDocList();
        docAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemExpand(int position) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("imageBites", docList.get(position).getImage());
        intent.putExtra("docInfo", docList.get(position).getDocInfo());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu , menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                docAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}