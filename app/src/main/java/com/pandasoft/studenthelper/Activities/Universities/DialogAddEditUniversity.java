package com.pandasoft.studenthelper.Activities.Universities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pandasoft.studenthelper.Entities.EntityUniversities;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelUniversities;

public class DialogAddEditUniversity extends BottomSheetDialogFragment {
    ViewModelUniversities viewModelUniversities;
    EntityUniversities entity;
    MyToolsCls.QUERY_TYPE query_type;
    ActivityResultLauncher<Intent> launcher;
    private ImageView imgView;
    private ImageButton btnClose;
    private Button btnSelectImage, btnSave;
    private EditText txtName, txtBrief, txtAddress, txtPhone;
    private TextView label;

    public DialogAddEditUniversity(Context context, ViewModelUniversities viewModel, MyToolsCls.QUERY_TYPE Qtype, EntityUniversities entity) {
        this.viewModelUniversities = viewModel;
        this.query_type = Qtype;
        this.entity = entity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_edit_university, container, false);
        initialize(view);
        loadData();
        setEvents();
        return view;
    }

    private void initialize(View view) {
        txtName = view.findViewById(R.id.input_university_name);
        txtBrief = view.findViewById(R.id.input_university_brief);
        txtAddress = view.findViewById(R.id.input_university_address);
        txtPhone = view.findViewById(R.id.input_university_phone);
        //txtNote = view.findViewById(R.id.input_university_note);
        imgView = view.findViewById(R.id.img_university);
        btnClose = view.findViewById(R.id.btn_close_add_edit_university);
        btnSelectImage = view.findViewById(R.id.btn_select_university_img);
        btnSave = view.findViewById(R.id.btn_save_university);
        label = view.findViewById(R.id.label_university_title);
    }

    private void setEvents() {
        btnClose.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> { // when button save clicked
            // Test Inputs
            if (!testInputs()) return;
            // Set Values
            if (entity == null) entity = new EntityUniversities();
            entity.setName(txtName.getText().toString());
            entity.setBrief(txtBrief.getText().toString());
            entity.setAddress(txtAddress.getText().toString());
            entity.setPhone(txtPhone.getText().toString());

            entity.setIs_uploaded(false);

            //Do action
            if (query_type == MyToolsCls.QUERY_TYPE.INSERT) {
                entity.setId(MyToolsCls.generateId());
                viewModelUniversities.insert(entity, msg -> dismiss());
            } else if (query_type == MyToolsCls.QUERY_TYPE.UPDATE)
                viewModelUniversities.update(entity, msg -> dismiss());
            //Close View
            dismiss();
        });

        // get file from storage
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (entity == null) entity = new EntityUniversities();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            //#1 get data
                            Uri uri = result.getData().getData();
                            //#2 set data
                            entity.setLocal_img(uri.toString());
                            imgView.setImageURI(uri);
                            Glide.with(requireContext()).load(uri).centerCrop().into(imgView);
                        } else {
                            // clear data -if nothing
                            imgView.setImageURI(null);
                            entity.setLocal_img(null);
                        }
                    }
                });
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });
    }

    private void loadData() {
        if (this.query_type == MyToolsCls.QUERY_TYPE.UPDATE && entity != null) {
            label.setText("تعديل الجامعة");
            txtName.setText(entity.getName());
            txtBrief.setText(entity.getBrief());
            txtAddress.setText(entity.getAddress());
            txtPhone.setText(entity.getPhone());

            if (entity.getIs_uploaded()) {

                Glide.with(requireActivity())
                        .load(entity.getCloud_img())
                        .centerCrop()
                        .fitCenter()
                        .into(imgView);
            } else {
                Uri uri = Uri.parse(entity.getLocal_img());
                Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .fitCenter()
                        .into(imgView);
            }
        }
    }

    private boolean testInputs() {
        boolean valid = true;
        if (txtName.getText() == null || txtName.getText().length() == 0) {
            valid = false;
            txtName.setError("يجب كتابة الاسم");
        }
        if (txtBrief.getText() == null || txtBrief.getText().length() == 0) {
            valid = false;
            txtBrief.setError("يجب كتابة نبذة مختصرة");
        }
        if (entity == null || entity.getLocal_img() == null || entity.getLocal_img().length() == 0) {
            valid = false;
            Toast.makeText(getContext(), "يجب تحديد صورة", Toast.LENGTH_LONG).show();
        }
        return valid;
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
                CoordinatorLayout.Behavior behavior = params.getBehavior();
                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
            });
        }
    }
}
