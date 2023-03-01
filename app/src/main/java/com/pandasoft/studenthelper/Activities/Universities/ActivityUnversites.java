package com.pandasoft.studenthelper.Activities.Universities;

import android.os.AsyncTask;
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
import com.pandasoft.studenthelper.Adapters.AdapterUniversity;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityUniversities;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelUniversities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityUnversites extends AppCompatActivity {

    //___________________
    private static final String TABLE_NAME = "universities";
    private List<EntityUniversities> mList;
    private ViewModelUniversities viewModelUniversities;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private AdapterUniversity adapterUniversity;
    private boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universites);
        Toolbar toolbar = findViewById(R.id.toolbar_universities_list);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initialize();
        loadList();
        setEvents();
        setAdapter();
        if(is_admin)
         getDeletedUploads();
    }

    private void setEvents() {
        // Floating Action Button Add University
        if(is_admin)
        fab.setOnClickListener(v -> {
            DialogAddEditUniversity sheet = new DialogAddEditUniversity(this, viewModelUniversities, MyToolsCls.QUERY_TYPE.INSERT, null);
            sheet.show(getSupportFragmentManager(), "ADD_UNIVERSITY");
        });
        // On Click On Any Item
        adapterUniversity.setOnClickItemListener(position -> {
            // show button sheet
            BottomSheetOptions bookBottomActionSheet = new BottomSheetOptions();
            bookBottomActionSheet.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                @Override
                public void onClickButtonDelete() {
                    viewModelUniversities.setDeleted(mList.get(position), msg -> {
                        if (msg == null) {
                            adapterUniversity.notifyItemRemoved(position);
                        }
                    });
                }

                @Override
                public void onClickButtonEdit() {
                    DialogAddEditUniversity dialog = new DialogAddEditUniversity(ActivityUnversites.this, viewModelUniversities, MyToolsCls.QUERY_TYPE.UPDATE, mList.get(position));
                    dialog.show(getSupportFragmentManager(), "EDIT_UNIVERSITY");
                }
            });
            bookBottomActionSheet.show(getSupportFragmentManager(), "BOOK_BOTTOM_ACTION");
        });
    }

    private void initialize() {

        is_admin = DataCls.getBoolean(this, "is_admin");
        setTitle("الجامعات");
        recyclerView = findViewById(R.id.recycler_view_control_universities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab_add_university);

        viewModelUniversities = new ViewModelProvider(this).get(ViewModelUniversities.class);
        adapterUniversity = new AdapterUniversity(this, is_admin);
        adapterUniversity.setViewModel(viewModelUniversities);
        if(!is_admin)fab.setVisibility(View.GONE);
    }

    private void loadList() {
        viewModelUniversities.getAllData().observe(this,
                list -> {
                    if (mList == null) mList = new ArrayList<>();
                    mList.clear();
                    if (list != null) {
                        mList.addAll(list);
                        adapterUniversity.setAdapter(mList);
                    }
                });
    }

    private void setAdapter() {
        recyclerView.setAdapter(adapterUniversity);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDeletedUploads() {
        if (!MyToolsCls.isNetworkConnected(this)) return;
        viewModelUniversities.getUploads().observe(this, list -> {
            for (EntityUniversities e : list) {
                if (!e.getIs_uploaded()) {
                    AsyncTask<Void, Void, Void> task = new UploadCls.UploadEntityTask<EntityUniversities>(getApplication(), TABLE_NAME, e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                                viewModelUniversities.delete(e, msg -> {});
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    });
                    task.execute();
                }
            }
        });
    }


}