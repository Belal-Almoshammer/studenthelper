package com.pandasoft.studenthelper.Dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pandasoft.studenthelper.R;

public class BottomSheetOptions extends BottomSheetDialogFragment {
    public OnClickButtonOption mListener;
    private Button btnEdit, btnDelete;

    @SuppressLint("WrongConstant")
    public BottomSheetOptions setOnClickButtonOption(OnClickButtonOption listener) {
        setStyle(0, R.style.CustomBottomDialog);
        this.mListener = listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_options, container, false);
        btnDelete = view.findViewById(R.id.btn_delete_book);
        btnEdit = view.findViewById(R.id.btn_edit_book);
        btnDelete.setOnClickListener(bD -> {
            dismiss();
            if (mListener != null)
                mListener.onClickButtonDelete();
        });
        btnEdit.setOnClickListener(v -> {
            dismiss();
            if (mListener != null)
                mListener.onClickButtonEdit();
        });
        return view;
    }

    public interface OnClickButtonOption {
        void onClickButtonDelete();

        void onClickButtonEdit();
    }
}
