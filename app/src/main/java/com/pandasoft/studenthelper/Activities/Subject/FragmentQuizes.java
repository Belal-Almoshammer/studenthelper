package com.pandasoft.studenthelper.Activities.Subject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandasoft.studenthelper.Activities.Quiz.ActivityAddEditQuiz;
import com.pandasoft.studenthelper.Activities.Quiz.DoExamActivity;
import com.pandasoft.studenthelper.Adapters.AdapterQuiz;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.Entities.EntityQuiz;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelQuestion;
import com.pandasoft.studenthelper.ViewModels.ViewModelQuiz;

import java.util.ArrayList;
import java.util.List;

public class FragmentQuizes extends Fragment {

    private static final String TABLE_NAME = "quiz";

    final String id_sub;
    RecyclerView recyclerView;
    AdapterQuiz adapter;
    List<EntityQuiz> mList;
    ViewModelQuiz viewModelQuiz;
    ViewModelQuestion viewModelQuestion;
    FloatingActionButton fab;
    private boolean is_admin;

    public FragmentQuizes(String id_sub) {
        this.id_sub = id_sub;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizes, container, false);
        initialize(view);
        setEvents();
        setAdapter();
        if (is_admin)
            getUploads();
        return view;
    }

    void initialize(View view) {
        is_admin = DataCls.getBoolean(requireContext(), "is_admin");
        adapter = new AdapterQuiz(getContext(), is_admin);
        recyclerView = view.findViewById(R.id.recycler_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModelQuiz = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ViewModelQuiz.class);
        viewModelQuestion = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ViewModelQuestion.class);

        fab = view.findViewById(R.id.fab_add);

        if(!is_admin)fab.setVisibility(View.GONE);
    }

    void setEvents() {

        if(is_admin)
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ActivityAddEditQuiz.class);
            intent.putExtra("id_sub", id_sub);
            intent.putExtra("mode", "INSERT");
            startActivity(intent);
        });
        adapter.setActionListener(new AdapterQuiz.OnClickCardListener() {
            @Override
            public void onClick(int position, AdapterQuiz.Holder holder) {

                Intent intent = new Intent(getContext(), DoExamActivity.class);
                intent.putExtra("quiz_id", mList.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

            @Override
            public void onActionClick(int position) {
                BottomSheetOptions dialog = new BottomSheetOptions();
                dialog.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                    @Override
                    public void onClickButtonDelete() {
                        viewModelQuiz.setDeleted(mList.get(position), msg -> {
                            adapter.notifyItemRemoved(position);
                            viewModelQuestion.setDeleted(mList.get(position).getId(), msg1 -> {

                            });
                        });
                    }

                    @Override
                    public void onClickButtonEdit() {
                        Intent intent = new Intent(getContext(), ActivityAddEditQuiz.class);
                        intent.putExtra("quiz_id", mList.get(position).getId());
                        intent.putExtra("id_sub", id_sub);
                        intent.putExtra("mode", "UPDATE");
                        startActivity(intent);
                    }
                });
                dialog.show(getParentFragmentManager(), "EDIT");
            }
        });


    }


    private void getUploads() {

        Context ctx = requireContext();
        if (!MyToolsCls.isNetworkConnected(ctx)) return;
        //upload list of questions
        viewModelQuiz.getUploads().observe(getViewLifecycleOwner(), list -> {
            for (EntityQuiz e : list) {
                if (!e.getIs_uploaded()) {
                    new UploadCls.UploadEntityTask<>(ctx, TABLE_NAME, e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            e.setIs_uploaded(true);
                            if (e.getUpdate_type() == 2) {
                                viewModelQuiz.delete(e, msg -> {
                                });
                            } else {
                                viewModelQuiz.update(e, msg -> {
                                });
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    }).execute();
                }
            }
        });

        //upload list of questions
        viewModelQuestion.getUploads().observe(getViewLifecycleOwner(), list -> {
            for (EntityQuestion e : list) {
                if (!e.getIs_uploaded()) {
                    new UploadCls.UploadEntityTask<>(requireContext(), "questions", e, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            e.setIs_uploaded(true);
                            if (e.getUpdate_type() == 2) {
                                viewModelQuestion.delete(e, msg -> {
                                });
                            } else {
                                viewModelQuestion.update(e, msg -> {
                                });
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    }).execute();
                }
            }
        });
    }

    void setAdapter() {
        viewModelQuiz.getList(id_sub).observe(getViewLifecycleOwner(), list -> {
            if (mList == null) mList = new ArrayList<>();
            mList.clear();
            mList.addAll(list);
            adapter.setAdapter(mList);
        });
        recyclerView.setAdapter(adapter);
    }

}