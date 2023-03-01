package com.pandasoft.studenthelper.Activities.Subject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pandasoft.studenthelper.Entities.EntityBook;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelBook;

;

public class DialogAddEditBook extends BottomSheetDialogFragment {
    private final MyToolsCls.QUERY_TYPE queryType;
    private final String id_sub;
    private TextView txtTitle;
    private Button btnSave;
    private ImageButton btn;
    private Button btnSelectBook;
    private ActivityResultLauncher<Intent> launcher;
    private ImageView bookImage;
    private ViewModelBook viewModelBook;
    private EditText txtBookName;
    private EntityBook entityBook;

    public DialogAddEditBook(MyToolsCls.QUERY_TYPE q, EntityBook entityBook, String id_sub) {
        this.entityBook = entityBook;
        this.queryType = q;
        this.id_sub = id_sub;
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_edit_book, container);
        initialize(view);
        loadData();
        setEvents();
        return view;
    }

    private void initialize(View view) {

        txtBookName = view.findViewById(R.id.txt_book_name);
        //textLink = view.findViewById(R.id.txt_book_link);
        txtTitle = view.findViewById(R.id.label_add_edit_book);
        btnSave = view.findViewById(R.id.btn_save_book);
        btn = view.findViewById(R.id.btn_close_add_edit_book);
        btnSelectBook = view.findViewById(R.id.btn_select_book);
        bookImage = view.findViewById(R.id.sheet_book_image);

        // get new instance from ViewModelBook using ViewModelProvider class
        viewModelBook = new ViewModelProvider(this).get(ViewModelBook.class);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        if (result.getData() != null) {
                            // #1 get data
                            Uri uri = result.getData().getData();
                            // #2 generate image
                            byte[] image_data = MyToolsCls.generatePdfThumpNail(uri, getContext());
                            Glide.with(this).load(image_data).into(bookImage);
                            // #3 set path to entity
                            if (entityBook == null) entityBook = new EntityBook();
                            entityBook.setLocal_url(uri.toString());
                        }
                    }
                });
    }

    private void setEvents() {

        btn.setOnClickListener(v -> dismiss());
        // Start save button
        btnSave.setOnClickListener(b -> {
            btnSave.setEnabled(false);
            saveBook();
            btnSave.setEnabled(true);
        });
        // End save button
        btnSelectBook.setOnClickListener(btn_s -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            launcher.launch(intent);
        });
    }

    private void saveBook() {
        // test inputs
        if (!test_inputs()) return;
        if (this.entityBook == null) entityBook = new EntityBook();

        entityBook.setName(txtBookName.getText().toString());
        entityBook.setId_sub(id_sub);

        entityBook.setIs_uploaded(false);

        if (this.queryType == MyToolsCls.QUERY_TYPE.INSERT) {
            entityBook.setUpdate_type(0);//set upload type to insert
            entityBook.setId(MyToolsCls.generateId());
            this.viewModelBook.insert(entityBook, (msg) -> {
                if (msg == null) dismiss();
                else Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            });
        } else if (this.queryType == MyToolsCls.QUERY_TYPE.UPDATE) {
            entityBook.setUpdate_type(1);//set upload to update
            this.viewModelBook.update(entityBook, (msg) -> {
                if (msg == null) dismiss();
                else Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            });
        }
    }

    public boolean test_inputs() {
        boolean valid = true;
        // test book name
        txtBookName.setError(null);
        if (entityBook != null) {
            // check file name if correct
            String file_name = URLUtil.guessFileName(entityBook.getLocal_url(), null, null);
            if (file_name != null && file_name.lastIndexOf(".") > 0) {
                if (!file_name.substring(file_name.lastIndexOf(".")).contains("pdf")) {
                    valid = false;
                    Toast.makeText(requireContext(), "ملف غير صالح", Toast.LENGTH_SHORT).show();
                }
            } else
            {
                valid = false;
                Toast.makeText(requireContext(), "ملف غير صالح", Toast.LENGTH_SHORT).show();
            }
        }
        if (txtBookName.getText() == null || txtBookName.getText().length() == 0) {
            valid = false;
            txtBookName.setError("يجب كتابة اسم الكتاب");
        }
        if (!valid)
            btnSave.setEnabled(true);
        return valid;
    }

    private void loadData() {
        if (queryType == MyToolsCls.QUERY_TYPE.UPDATE && this.entityBook != null) {
            txtTitle.setText("تعديل كتاب");
            txtBookName.setText(entityBook.getName());
            // load image if exists
            Glide.with(this).load(Uri.parse(entityBook.getLocal_url())).into(bookImage);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        View view = getView();
        if (view != null) {
            view.post(() -> {
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) params.getBehavior();
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
                }
            });
        }
    }
}
