package com.pandasoft.studenthelper.Activities.Websites;

import android.content.Intent;
import android.net.Uri;
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
import com.pandasoft.studenthelper.Adapters.AdapterWebsite;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityWebsites;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelWebsite;

import java.util.ArrayList;
import java.util.List;

public class ActivityWebsites extends AppCompatActivity {

    private static final String TABLE_NAME = "website";

    boolean is_admin;
    //________________________
    private RecyclerView recyclerView;
    private AdapterWebsite adapterWebsite;
    private FloatingActionButton fab;
    private List<EntityWebsites> mList;
    private ViewModelWebsite viewModelWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websites);
        Toolbar toolbar = findViewById(R.id.toolbar_websites_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
        setAdapter();
        setEvents();
        if(is_admin)
        getUploads();
    }

    private void initialize() {
        // get if is admin
        is_admin = DataCls.getBoolean(this, "is_admin");
        adapterWebsite = new AdapterWebsite(this, is_admin);
        recyclerView = findViewById(R.id.control_websites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab_add_website);
        viewModelWebsite = new ViewModelProvider(this).get(ViewModelWebsite.class);

        if(!is_admin)fab.setVisibility(View.GONE);
    }

    private void setEvents() {
        adapterWebsite.setOnClickActionListener(new AdapterWebsite.OnClickActionListener() {
            @Override
            public void onClick(int position) {
                BottomSheetOptions sheetOptions = new BottomSheetOptions();
                sheetOptions.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                    @Override
                    public void onClickButtonDelete() {
                        viewModelWebsite.setDeleted(mList.get(position), msg -> {
                            adapterWebsite.notifyItemRemoved(position);
                        });
                    }

                    @Override
                    public void onClickButtonEdit() {
                        EntityWebsites website = mList.get(position);
                        DialogAddEditWebsite fragment = new DialogAddEditWebsite(ActivityWebsites.this, viewModelWebsite, MyToolsCls.QUERY_TYPE.UPDATE, website);
                        fragment.show();
                    }
                });
                sheetOptions.show(getSupportFragmentManager(), "BOTTOM_WEBSITE_ACTION");
            }

            @Override
            public void onCardClick(int position) {
                // Open Website
                String url = mList.get(position).getLink_web();

                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
        // Set Fab To Add new website
        if(is_admin)
        fab.setOnClickListener(v -> {
            DialogAddEditWebsite fragment = new DialogAddEditWebsite(ActivityWebsites.this, viewModelWebsite, MyToolsCls.QUERY_TYPE.INSERT, null);
            fragment.show();
        });
    }

    private void setAdapter() {
        viewModelWebsite.getAllData()
                .observe(this,
                        list -> {
                            if (mList == null) mList = new ArrayList<>();
                            mList.clear();
                            if (list != null) mList.addAll(list);
                            adapterWebsite.setAdapter(mList);
                        });
        recyclerView.setAdapter(adapterWebsite);
    }


    private void getUploads() {
        if (!MyToolsCls.isNetworkConnected(getApplication())) return;
        viewModelWebsite.getUploads().observe(this, list -> {
            for (EntityWebsites e : list) {
                if (!e.getIs_uploaded()) {
                    AsyncTask<Void, Void, Void> task = new UploadCls.UploadEntityTask<EntityWebsites>(getApplication(), TABLE_NAME, e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            if(e.getUpdate_type() == 2){
                                viewModelWebsite.delete(e,msg->{});
                            }else{
                                e.setIs_uploaded(true);
                                viewModelWebsite.update(e, msg -> { });
                            }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}