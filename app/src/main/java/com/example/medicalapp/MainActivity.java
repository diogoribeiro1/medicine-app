package com.example.medicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.medicalapp.Adapter.RemedioAdapter;
import com.example.medicalapp.database.RemedioDAO;
import com.example.medicalapp.model.RemedioModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements  DialogCloseListener{

    RemedioDAO dao = new RemedioDAO();
    private RecyclerView remedioRecyclerView;
    private RemedioAdapter remedioAdapter;
    private List<RemedioModel> remedioList;

    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        remedioList = new ArrayList<>();

        remedioRecyclerView = findViewById(R.id.RemediosRecycleView);
        remedioRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        remedioAdapter = new RemedioAdapter(this);
        remedioRecyclerView.setAdapter(remedioAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(remedioAdapter));
        itemTouchHelper.attachToRecyclerView(remedioRecyclerView);

        fab = findViewById(R.id.fab);

        remedioList = dao.read();

        remedioAdapter.setRemedioList(remedioList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewRemedio.newInstance().show(getSupportFragmentManager(), AddNewRemedio.TAG);
            }
        });
        
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        Collections.reverse(remedioList);
        remedioAdapter.setRemedioList(remedioList);
        remedioAdapter.notifyDataSetChanged();
    }
}