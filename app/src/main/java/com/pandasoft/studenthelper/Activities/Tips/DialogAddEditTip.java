package com.pandasoft.studenthelper.Activities.Tips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pandasoft.studenthelper.Entities.EntityTipsAndAdvice;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelTips;

;

public class DialogAddEditTip extends BottomSheetDialogFragment {

    Button btnSave;
    ImageButton btnClose;
    EditText txtTitle, txtDescription;
    EntityTipsAndAdvice entityTipsAndAdvice;
    QUERY_TYPE query_type;
    ViewModelTips viewModelTips;

    public DialogAddEditTip(ViewModelTips vm, QUERY_TYPE q_type, EntityTipsAndAdvice entity) {
        query_type = q_type;
        viewModelTips = vm;
        entityTipsAndAdvice = entity;
    }

    @Nullable

    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_edit_tip, container, false);
        initialize(view);
        setEvents();
        loadData();
        return view;
    }

    void loadData() {
        if (query_type == QUERY_TYPE.UPDATE && null != entityTipsAndAdvice) {
            txtTitle.setText(entityTipsAndAdvice.getTitle());
            txtDescription.setText(entityTipsAndAdvice.getNote());
        }
    }

    void initialize(View view) {
        btnSave = view.findViewById(R.id.btn_save);
        btnClose = view.findViewById(R.id.btn_close);
    }

    void setEvents() {
        btnClose.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> test_inputs(() -> {
            if (entityTipsAndAdvice == null)
                entityTipsAndAdvice = new EntityTipsAndAdvice();

            entityTipsAndAdvice.setTitle(txtTitle.getText().toString());
            entityTipsAndAdvice.setNote(txtDescription.getText().toString());

            entityTipsAndAdvice.setIs_uploaded(false);


            if (query_type == QUERY_TYPE.INSERT)
            {
                entityTipsAndAdvice.setId(MyToolsCls.generateId());
                viewModelTips.insert(entityTipsAndAdvice, msg->dismiss());
            }
            else if (query_type == QUERY_TYPE.UPDATE)
                viewModelTips.update(entityTipsAndAdvice, msg->dismiss());
        }));
    }

    void test_inputs(OnSuccessListener listener) {
        boolean valid = true;
        if (txtTitle.getText() == null || txtTitle.getText().length() == 0) {
            valid = false;
            txtTitle.setError("يجب كتابة العنوان");
        }
        if (txtDescription.getText() == null || txtDescription.getText().length() == 0) {
            valid = false;
            txtDescription.setError("يجب كتابة التفاصيلٍ");
        }
        if (valid) listener.success();
    }

    enum QUERY_TYPE {INSERT, UPDATE, DELETE}

    interface OnSuccessListener {
        void success();
    }
}

