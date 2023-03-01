package com.pandasoft.studenthelper.Activities.Main;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Activities.Institutes.ActivityInstitutes;
import com.pandasoft.studenthelper.Activities.Subject.ActivitySubject;
import com.pandasoft.studenthelper.Activities.Tips.ActivityTips;
import com.pandasoft.studenthelper.Activities.Universities.ActivityUnversites;
import com.pandasoft.studenthelper.Activities.Websites.ActivityWebsites;
import com.pandasoft.studenthelper.Adapters.AdapterSubject;
import com.pandasoft.studenthelper.Entities.EntitySubjectsLevel;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.ViewModels.ViewModelSubject;

import java.util.ArrayList;
import java.util.List;

;

public class FragmentMain extends Fragment {

    CardView btnUnivs, btnInstit, btnTips, btnWebsites;
    RecyclerView recyclerView;
    ViewModelSubject viewModelSubject;
    AdapterSubject adapterSubject;
    List<EntitySubjectsLevel> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initialize(view);
        setAdapter();
        setEvents();
        return view;
    }

    private void initialize(View view) {
        btnUnivs = view.findViewById(R.id.btn_universities);
        btnInstit = view.findViewById(R.id.btn_institutes);
        btnTips = view.findViewById(R.id.btn_tips);
        btnWebsites = view.findViewById(R.id.btn_websites);
        //________________________________________________________
        recyclerView = view.findViewById(R.id.recycler_view_subjects_control);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        viewModelSubject = new ViewModelProvider(this).get(ViewModelSubject.class);
        adapterSubject = new AdapterSubject(getContext());
    }

    private void setEvents() {
        btnUnivs.setOnClickListener(v -> startActivity(new Intent(getContext(), ActivityUnversites.class)));
        btnWebsites.setOnClickListener(v -> startActivity(new Intent(getContext(), ActivityWebsites.class)));
        btnInstit.setOnClickListener(v -> startActivity(new Intent(getContext(), ActivityInstitutes.class)));
        btnTips.setOnClickListener(v -> startActivity(new Intent(getContext(), ActivityTips.class)));
        // Subjects events
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
        SharedPreferences preferences = requireContext().getSharedPreferences("main_pref", MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key.equals("id_level")) {
                viewModelSubject.setLevelId(sharedPreferences.getString("id_level", "-1"));
            }
        });
        //________________________________________________________

    }

    private void setAdapter() {
        String id_level = requireContext()
                .getSharedPreferences("main_pref", MODE_PRIVATE)
                .getString("id_level", "12");

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
