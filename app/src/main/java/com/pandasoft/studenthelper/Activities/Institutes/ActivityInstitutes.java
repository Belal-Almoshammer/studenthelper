package com.pandasoft.studenthelper.Activities.Institutes;

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
import com.pandasoft.studenthelper.Adapters.AdapterInstitute;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityInstitutes;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelInstitutes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityInstitutes extends AppCompatActivity {

    private static final String TABLE_NAME = "institutes";

    private RecyclerView recyclerView;
    private ViewModelInstitutes viewModelInstituties;
    private AdapterInstitute adapterInstitute;
    private List<EntityInstitutes> mList;
    private FloatingActionButton fab;
    private boolean is_admin;

    private void initialize() {
        is_admin = DataCls.getBoolean(this, "is_admin");
        fab = findViewById(R.id.fab_add_institute);
        recyclerView = findViewById(R.id.recycler_view_control_intitutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModelInstituties = new ViewModelProvider(this).get(ViewModelInstitutes.class);
        adapterInstitute = new AdapterInstitute(this, viewModelInstituties, is_admin);

        if(!is_admin)fab.setVisibility(View.GONE);
    }

    private void setEvents() {
        if(is_admin)
        fab.setOnClickListener(v ->
                new DialogAddEditInstitute(viewModelInstituties, MyToolsCls.QUERY_TYPE.INSERT, null)
                        .show(getSupportFragmentManager(), "ADD_INSTITUTE")
        );
        adapterInstitute.setOnButtonActionClickListener(position -> {
            BottomSheetOptions options = new BottomSheetOptions();
            options.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                @Override
                public void onClickButtonDelete() {
                    EntityInstitutes entity = mList.get(position);

                    viewModelInstituties.setDeleted(entity, msg -> {
                        adapterInstitute.notifyItemRemoved(position);
                    });
                }

                @Override
                public void onClickButtonEdit() {
                    EntityInstitutes entity = mList.get(position);
                    DialogAddEditInstitute sheet = new DialogAddEditInstitute(viewModelInstituties,
                            MyToolsCls.QUERY_TYPE.UPDATE, entity);
                    sheet.show(getSupportFragmentManager(), "EDIT_INSTITUTE");
                }
            });
            options.show(getSupportFragmentManager(), "BOTTOM_SHEET_INSTITUTE_ACTION");
        });
    }

    private void setAdapter() {
        viewModelInstituties.getAllData().observe(this, list -> {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.clear();
            mList.addAll(list);
            adapterInstitute.setListItems(mList);
        });
        recyclerView.setAdapter(adapterInstitute);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institutes);

        Toolbar toolbar = findViewById(R.id.init_institutes_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        this.setTitle("قائمة المعاهد");

        initialize();
        setEvents();
        setAdapter();
        if(is_admin)
        getDeletedUploads();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    private void getDeletedUploads() {
        if (!MyToolsCls.isNetworkConnected(this)) return;
        viewModelInstituties.getUploads().observe(this, list -> {
            for (EntityInstitutes e : list) {
                if (!e.getIs_uploaded()) {
                    AsyncTask<Void, Void, Void> task = new UploadCls.UploadEntityTask<>(this, "institutes", e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            viewModelInstituties.delete(e, msg -> { });
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