package com.pandasoft.studenthelper.Activities.Notification;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandasoft.studenthelper.Adapters.AdapterNotification;
import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelNotifications;

import java.util.ArrayList;
import java.util.List;

public class ActivityNotification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterNotification adapterNotification;
    private ViewModelNotifications viewModelNotifications;
    private FloatingActionButton fab;
    private List<EntityNotifications> mList;
    private Toolbar toolbar;
    private boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initialize();
        setEvents();
        setAdapter();
    }

    private void initialize() {
        is_admin = DataCls.getBoolean(this,"is_admin");
        recyclerView = findViewById(R.id.recycler_view_control_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterNotification = new AdapterNotification(this);
        viewModelNotifications = new ViewModelProvider(this).get(ViewModelNotifications.class);
        fab = findViewById(R.id.fab_add_notification);

        if(!is_admin)
            fab.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar_notifications);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEvents() {
        if(is_admin)
        fab.setOnClickListener(v -> {
            DialogAddNotification dialog = new DialogAddNotification(this, viewModelNotifications);
            dialog.show();
        });
        adapterNotification.setOnClickListener(position -> {
            viewModelNotifications.delete(mList.get(position), msg -> {
                adapterNotification.notifyItemRemoved(position);
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter() {
        viewModelNotifications.getList().observe(this, list -> {
            if (mList == null) mList = new ArrayList<>();
            mList.clear();
            mList.addAll(list);
            adapterNotification.setAdapter(mList);
        });
        recyclerView.setAdapter(adapterNotification);
    }
}