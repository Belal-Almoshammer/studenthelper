package com.pandasoft.studenthelper.Activities.Subject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Adapters.AdapterSubject;
import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.ViewModels.ViewModelSubject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSubjects extends Fragment {
    RecyclerView recyclerView;
    ViewModelSubject viewModelSubject;
    AdapterSubject adapterSubject;
    List<EntitySubjectsLevel> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);
        initialize(view);
        setEvents();
        setAdapter();

        return view;
    }

    private void initialize(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_subjects_control);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        viewModelSubject = new ViewModelProvider(this).get(ViewModelSubject.class);
        adapterSubject = new AdapterSubject(getContext());
    }

    private void setEvents() {
        adapterSubject.setOnClickItemListener((position, holder) -> {
            Intent intent = new Intent(getContext(), ActivitySubject.class);
            intent.putExtra("id_sub", mList.get(position).getId());
            intent.putExtra("subject_name", mList.get(position).getSubject_name());
            intent.putExtra("image", mList.get(position).getImg_name());
            intent.putExtra("color_code", mList.get(position).getColor_code());

            Pair[] pairs = new Pair[]{
                    Pair.create(holder.subjectImage, "subject_image_transition"),
                    Pair.create(holder.txtName, "subject_name_transition")
            };
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            startActivity(intent, options.toBundle());
        });
    }

    private void setAdapter() {
        String id_level = getContext()
                .getSharedPreferences("main_pref", MODE_PRIVATE)
                .getString("level_id", "");

        // if (Strings.isEmptyOrWhitespace(id_level)) return;
        viewModelSubject.getAllData(id_level).observe(getViewLifecycleOwner(), list -> {
            if (mList == null) mList = new ArrayList<>();
            mList.clear();
            if (list != null) {
                mList.addAll(list);
                adapterSubject.setAdapter(mList);
            }
        });
        recyclerView.setAdapter(adapterSubject);
    }
}