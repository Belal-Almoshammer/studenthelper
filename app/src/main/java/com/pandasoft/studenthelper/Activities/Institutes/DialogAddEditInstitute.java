package com.pandasoft.studenthelper.Activities.Institutes;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pandasoft.studenthelper.Entities.EntityInstitutes;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelInstitutes;

public class DialogAddEditInstitute extends BottomSheetDialogFragment {
    ViewModelInstitutes viewModelInstitutes;
    EntityInstitutes entityInstitutes;
    MyToolsCls.QUERY_TYPE query_type;
    ActivityResultLauncher<Intent> launcher;
    ImageView insImage;
    ImageButton btnClose;
    Button btnSelectImage, btnSave;
    EditText txtName, txtBrief, txtAddress, txtPhone, txtNote;

    public DialogAddEditInstitute(ViewModelInstitutes viewModel, MyToolsCls.QUERY_TYPE Qtype, EntityInstitutes entity) {
        this.viewModelInstitutes = viewModel;
        this.query_type = Qtype;
        this.entityInstitutes = entity;
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_edit_institute, container, false);
        initialize(view);
        loadData();
        setEvents();
        return view;
    }

    private void initialize(View view) {
        // ---------------------------------------- inputs
        txtName = view.findViewById(R.id.input_institute_name);
        txtBrief = view.findViewById(R.id.input_institute_brief);
        txtAddress = view.findViewById(R.id.input_institute_address);
        txtPhone = view.findViewById(R.id.input_institute_phone);
        txtNote = view.findViewById(R.id.input_institute_note);
        // ---------------------------------------- image
        insImage = view.findViewById(R.id.img_institute);
        // ---------------------------------------- buttons
        btnSelectImage = view.findViewById(R.id.btn_select_institute_img);
        btnClose = view.findViewById(R.id.btn_close_add_edit_institute);
        btnSave = view.findViewById(R.id.btn_save_institute);
    }

    private void setEvents() {
        btnClose.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> {
            // when button save clicked
            // Test Inputs
            if (!testInputs()) return;
            // Set Values
            if (entityInstitutes == null) entityInstitutes = new EntityInstitutes();
            entityInstitutes.setName(txtName.getText().toString());
            entityInstitutes.setBrief(txtBrief.getText().toString());
            entityInstitutes.setAddress(txtAddress.getText().toString());
            entityInstitutes.setPhone(txtPhone.getText().toString());
            entityInstitutes.setNote(txtNote.getText().toString());

            entityInstitutes.setIs_uploaded(false);
            // -- Do action --
            if (query_type == MyToolsCls.QUERY_TYPE.INSERT)// set mode
            {
                entityInstitutes.setId(MyToolsCls.generateId());
                viewModelInstitutes.insert(entityInstitutes, msg -> dismiss());// insert
            } else if (query_type == MyToolsCls.QUERY_TYPE.UPDATE)
                viewModelInstitutes.update(entityInstitutes, msg -> dismiss());// update
            //Start close View -----
            dismiss();
            //----end close view

        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri uri = result.getData().getData();
                            // set image content to entity
                            if (entityInstitutes == null) entityInstitutes = new EntityInstitutes();
                            entityInstitutes.setLocal_img(uri.toString());
                            // set image to view
                            insImage.setImageURI(uri);
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
        if (this.query_type == MyToolsCls.QUERY_TYPE.UPDATE && entityInstitutes != null) {
            txtName.setText(entityInstitutes.getName());
            txtBrief.setText(entityInstitutes.getBrief());
            txtAddress.setText(entityInstitutes.getAddress());
            txtPhone.setText(entityInstitutes.getPhone());
            txtNote.setText(entityInstitutes.getNote());
            if (entityInstitutes.getIs_uploaded()) {

                Glide.with(requireActivity())
                        .load(entityInstitutes.getCloud_img())
                        .centerCrop()
                        .fitCenter()
                        .into(insImage);
            } else {
                Uri uri = Uri.parse(entityInstitutes.getLocal_img());
                Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .fitCenter()
                        .into(insImage);
            }
        }
    }

    private boolean testInputs() {
        boolean valid = true;
        if (txtName.getText() == null
                || txtName.getText().length() == 0) {
            valid = false;
            txtName.setError("يجب كتابة الاسم");
        }
        if (txtBrief.getText() == null
                || txtBrief.getText().length() == 0) {
            valid = false;
            txtBrief.setError("يجب كتابة نبذة مختصرة");
        }
        if (entityInstitutes == null || entityInstitutes.getLocal_img() == null || entityInstitutes.getLocal_img().length() == 0) {
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
