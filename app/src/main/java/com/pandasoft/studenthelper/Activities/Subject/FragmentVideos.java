package com.pandasoft.studenthelper.Activities.Subject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandasoft.studenthelper.Adapters.AdapterVideo;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityVideo;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelVideo;

import java.util.ArrayList;
import java.util.List;

public class FragmentVideos extends Fragment {


    RecyclerView recyclerView;
    FloatingActionButton fab;
    ViewModelVideo viewModelVideo;
    AdapterVideo adapter;
    List<EntityVideo> mList;

    String id_sub;
    private boolean is_admin;

    public FragmentVideos(String id_sub) {
        this.id_sub = id_sub;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        initialize(view);
        setEvents();
        setAdapter();
        if (is_admin)
            getUploads();
        return view;
    }

    private void initialize(View view) {
        is_admin = DataCls.getBoolean(requireContext(), "is_admin");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        fab = view.findViewById(R.id.fab_add_course);
        viewModelVideo = new ViewModelProvider(this).get(ViewModelVideo.class);
        adapter = new AdapterVideo(getContext(), is_admin);

        if(!is_admin)fab.setVisibility(View.GONE);
    }

    private void setEvents() {
        if(is_admin)
        fab.setOnClickListener(v -> {
            DialogAddEditVideo dialog = new DialogAddEditVideo(requireContext(), viewModelVideo, MyToolsCls.QUERY_TYPE.INSERT, null, id_sub);
            dialog.show();
        });
        adapter.setOnClickActionListener(new AdapterVideo.OnActionClickListener() {
            @Override
            public void optionClick(int position) {
                BottomSheetOptions options = new BottomSheetOptions();
                options.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                    @Override
                    public void onClickButtonDelete() {
                        EntityVideo entity = mList.get(position);
                        viewModelVideo.setDeleted(entity, msg -> {
                            adapter.notifyItemRemoved(position);
                        });
                    }

                    @Override
                    public void onClickButtonEdit() {
                        show_edit_dialog(position);
                    }
                });
                options.show(getParentFragmentManager(), "BOTTOM_SHEET_Videos");
            }

            @Override
            public void ItemCard(int position) {
                String url = mList.get(position).getCloud_url();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });
    }

    private void show_edit_dialog(int position) {
        EntityVideo entity = mList.get(position);
        DialogAddEditVideo dialog = new DialogAddEditVideo(getContext(), viewModelVideo, MyToolsCls.QUERY_TYPE.UPDATE, entity, id_sub);
        dialog.show();
    }

    private void getUploads() {
        if (!MyToolsCls.isNetworkConnected(requireContext())) return;
        viewModelVideo.getUploads().observe(getViewLifecycleOwner(), list -> {
            for (EntityVideo e : list) {
                if (!e.getIs_uploaded()) {
                    AsyncTask<Void, Void, Void> task = new UploadCls.UploadEntityTask<>(requireContext(), "video", e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            e.setIs_uploaded(true);
                            if (e.getUpdate_type() == 2)
                                viewModelVideo.delete(e, msg -> {
                                });
                            else
                                viewModelVideo.update(e, msg -> {
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

    private void setAdapter() {
        viewModelVideo.getVideos(id_sub).observe(getViewLifecycleOwner(), list -> {
            if (mList == null) mList = new ArrayList<>();
            if (list != null) mList.addAll(list);
            adapter.setAdapter(list);
        });
        recyclerView.setAdapter(adapter);
    }
}