package com.pandasoft.studenthelper.Activities.Tips;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandasoft.studenthelper.Adapters.AdapterTips;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelTips;

import java.util.ArrayList;
import java.util.List;

public class ActivityTips extends AppCompatActivity {

    boolean is_admin;

    List<EntityTipsAndAdvice> mList;
    RecyclerView recyclerView;
    ViewModelTips viewModelTips;
    FloatingActionButton fab;
    AdapterTips adapterTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        initialize();
        setEvents();
        setAdapter();
        if(is_admin)
        getUploads();
    }

    private void setAdapter() {
        viewModelTips.getAllData().observe(this, list -> {
            if (mList == null) mList = new ArrayList<>();
            mList.clear();
            mList.addAll(list);
            adapterTips.setAdapter(mList);
        });
        recyclerView.setAdapter(adapterTips);
    }

    private void setEvents() {
        adapterTips.setOnItemOptionClick(position -> {
            BottomSheetOptions bottomSheetOptions = new BottomSheetOptions();
            bottomSheetOptions.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                @Override
                public void onClickButtonDelete() {

                    viewModelTips.setDeleted(mList.get(position), msg -> {
                        adapterTips.notifyItemRemoved(position);
                    });
                }

                @Override
                public void onClickButtonEdit() {
                    EntityTipsAndAdvice entity = mList.get(position);
                    DialogAddEditTip tip = new DialogAddEditTip(viewModelTips, DialogAddEditTip.QUERY_TYPE.INSERT, entity);
                    tip.show(getSupportFragmentManager(), "ADD_TIP");
                }
            });
            bottomSheetOptions.show(getSupportFragmentManager(), "BOTTOM_SHEET_OPTION");
        });
        if(is_admin)
        fab.setOnClickListener(v -> {
            DialogAddEditTip tip = new DialogAddEditTip(viewModelTips, DialogAddEditTip.QUERY_TYPE.INSERT, null);
            tip.show(getSupportFragmentManager(), "ADD_TIP");
        });
    }

    private void initialize() {
        is_admin = DataCls.getBoolean(this, "is_admin");
        recyclerView = findViewById(R.id.recycler_view_tips);
        fab = findViewById(R.id.fab_add_tip);
        if(!is_admin) fab.setVisibility(View.GONE);
        viewModelTips = new ViewModelProvider(this).get(ViewModelTips.class);
        adapterTips = new AdapterTips(this, is_admin);
        Toolbar toolbar = findViewById(R.id.toolbar_tips);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUploads() {
        if (!MyToolsCls.isNetworkConnected(getApplication())) return;
        viewModelTips.getUploads().observe(this, list -> {
            for (EntityTipsAndAdvice e : list) {
                if (!e.getIs_uploaded()) {
                    AsyncTask<Void, Void, Void> task = new UploadCls.UploadEntityTask<EntityTipsAndAdvice>(getApplication(), "tips_and_advice", e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            e.setIs_uploaded(true);
                            if (e.getUpdate_type() == 2)
                                viewModelTips.delete(e, msg -> {
                                });
                            else
                                viewModelTips.update(e, msg -> {
                                });
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