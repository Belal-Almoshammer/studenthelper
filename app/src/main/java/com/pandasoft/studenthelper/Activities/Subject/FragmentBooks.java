package com.pandasoft.studenthelper.Activities.Subject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandasoft.studenthelper.Adapters.AdapterBook;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityBook;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelBook;

import java.util.ArrayList;
import java.util.List;

public class FragmentBooks extends Fragment {

    final String TABLE_NAME = "book";
    final String id_sub;// subject id
    private List<EntityBook> mList;
    private ViewModelBook viewModelBooks;
    private RecyclerView recyclerView;
    private AdapterBook adapterBook;
    private FloatingActionButton fab;
    private boolean is_admin;

    public FragmentBooks(String subject_id) {
        this.id_sub = subject_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        initialize(view);
        setAdapter();
        setEvents();
        if (is_admin)
            uploadDeletedBooks();
        return view;
    }

    private void initialize(View view) {
        fab = view.findViewById(R.id.fab_add);
        is_admin = DataCls.getBoolean(requireContext(), "is_admin");
        if (!is_admin) {
            fab.setVisibility(View.GONE);
        }
        recyclerView = view.findViewById(R.id.recycler_view_list);
        viewModelBooks = new ViewModelProvider(this).get(ViewModelBook.class);
        adapterBook = new AdapterBook(getContext(), is_admin);
        adapterBook.setViewModelBook(viewModelBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setEvents() {
        String user_id = requireContext()
                .getSharedPreferences("main_pref", Context.MODE_PRIVATE)
                .getString("user_id", "Non").trim();
        // if is admin
        fab.setOnClickListener(v -> {
            DialogAddEditBook dialog = new DialogAddEditBook(MyToolsCls.QUERY_TYPE.INSERT, null, id_sub);

            dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_StudentHelper_MyDialogTheme);
            dialog.show(getParentFragmentManager(), "ADD_BOOK");
        });

        adapterBook.setOnItemOptionClick(position -> {
            BottomSheetOptions options = new BottomSheetOptions();
            options.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                @Override
                public void onClickButtonDelete() {
                    EntityBook entity = mList.get(position);
                    if (entity.getIs_uploaded() && entity.getUser_update_id() != null && entity.getUser_update_id().equals(user_id)) {
                        viewModelBooks.setDeleted(entity, (msg) -> {
                            if (msg == null) adapterBook.notifyItemRemoved(position);
                        });
                    } else {
                        viewModelBooks.delete(entity, (msg) -> {
                            if (msg == null) adapterBook.notifyItemRemoved(position);
                        });
                    }
                }

                @Override
                public void onClickButtonEdit() {
                    showEditDialog(position);
                }
            });
            options.show(getParentFragmentManager(), "BOOK_BOTTOM_ACTION");
        });
    }

    private void showEditDialog(int position) {
        EntityBook entity = mList.get(position);
        viewModelBooks.delete(entity, (msg) -> {
            if (msg == null) adapterBook.notifyItemRemoved(position);
        });
        DialogAddEditBook fragment = new DialogAddEditBook(MyToolsCls.QUERY_TYPE.UPDATE, entity, id_sub);
        fragment.show(getParentFragmentManager(), "EDIT_BOOK");
    }

    private void setAdapter() {
        viewModelBooks.getBooks(id_sub).observe(getViewLifecycleOwner(), list -> {
            if (mList == null) mList = new ArrayList<>();
            mList.clear();
            mList.addAll(list);
            adapterBook.setAdapter(mList);
        });
        recyclerView.setAdapter(adapterBook);
    }

    void uploadDeletedBooks() {
        if (!MyToolsCls.isNetworkConnected(requireContext())) return;
        viewModelBooks.getUploads().observe(getViewLifecycleOwner(), list -> {
            for (EntityBook entity : list) {
                if (!entity.getIs_uploaded()) {
                    new UploadCls.UploadEntityTask<EntityBook>(requireContext(), TABLE_NAME, entity, new MyToolsCls.OnUpload() {
                        @Override
                        public void onSuccess() {
                            viewModelBooks.delete(entity, msg -> {
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    }).execute();
                }
            }
        });
    }
}