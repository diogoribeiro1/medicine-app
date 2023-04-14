package com.example.medicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.medicalapp.Adapter.RemedioAdapter;
import com.example.medicalapp.database.RemedioDAO;
import com.example.medicalapp.model.RemedioModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements  DialogCloseListener {

    RemedioDAO dao = new RemedioDAO();
    private RecyclerView remedioRecyclerView;
    private RemedioAdapter remedioAdapter;
    private List<RemedioModel> remedioList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        createNotificationChannel();

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

        fab.setOnClickListener(view -> AddNewRemedio.newInstance().show(getSupportFragmentManager(), AddNewRemedio.TAG));

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Collections.reverse(remedioList);
        remedioAdapter.setRemedioList(remedioList);
        remedioAdapter.notifyDataSetChanged();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Notif Channel";
            String desc = "A Description of the Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}