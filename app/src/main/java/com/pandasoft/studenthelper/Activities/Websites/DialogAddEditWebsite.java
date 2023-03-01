package com.pandasoft.studenthelper.Activities.Websites;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.pandasoft.studenthelper.Entities.EntityWebsites;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelWebsite;

public class DialogAddEditWebsite extends Dialog {

    private final ViewModelWebsite viewModelWebsite;
    private final MyToolsCls.QUERY_TYPE queryType;
    private Button btnSave;
    private ImageButton btnClose;
    private EditText txtName;
    private EditText txtLink;
    private EntityWebsites entityWebsites;

    public DialogAddEditWebsite(Context context, ViewModelWebsite viewModel, MyToolsCls.QUERY_TYPE query_type, EntityWebsites entity) {
        super(context, R.style.Theme_StudentHelper_MyDialogTheme);
        viewModelWebsite = viewModel;
        queryType = query_type;
        entityWebsites = entity;
    }

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_website, null, false);
        setContentView(view);
        initialize(view);
        setEvents();
        setUp();
    }

    private void initialize(View view) {
        txtName = view.findViewById(R.id.input_website_name);
        txtLink = view.findViewById(R.id.input_website_link);
        btnClose = view.findViewById(R.id.btn_close_add_edit_website);
        btnSave = view.findViewById(R.id.btn_save_website);
    }

    private void setEvents() {
        btnClose.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> {
            if (!testInputs()) return;
            if (entityWebsites == null) entityWebsites = new EntityWebsites();
            entityWebsites.setTitle(txtName.getText().toString());
            entityWebsites.setLink_web(txtLink.getText().toString());

            entityWebsites.setIs_uploaded(false);

            if (queryType == MyToolsCls.QUERY_TYPE.INSERT)
            {
                entityWebsites.setId(MyToolsCls.generateId());
                viewModelWebsite.insert(entityWebsites, msg -> dismiss());
            }
            else if (queryType == MyToolsCls.QUERY_TYPE.UPDATE)
                viewModelWebsite.update(entityWebsites, msg -> dismiss());
        });
    }

    private void setUp() {
        if (queryType == MyToolsCls.QUERY_TYPE.UPDATE && entityWebsites != null) {
            txtName.setText(entityWebsites.getTitle());
            txtLink.setText(entityWebsites.getLink_web());
        }
    }

    private boolean testInputs() {
        boolean valid = true;
        if (txtName.getText() == null || txtName.getText().length() == 0) {
            valid = false;
            txtName.setError("يجب كتابة اسم الموقع");
        }
        if (txtLink.getText() == null || txtLink.getText().length() == 0) {
            valid = false;
            txtLink.setError("يجب كتابة الرابط");
        }

        return valid;
    }
}
