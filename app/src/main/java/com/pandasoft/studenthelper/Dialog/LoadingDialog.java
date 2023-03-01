package com.pandasoft.studenthelper.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pandasoft.studenthelper.R;

public class LoadingDialog {
    Context mContext;
    AlertDialog dialog;
    ProgressBar progressBar;
    TextView textView;
    Button btnCancel;
    OnClickListener mListener;

    public LoadingDialog(Context m) {
        mContext = m;
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_loading, null);

        builder.setView(view);
        builder.setCancelable(false);
        //progressBar = view.findViewById(R.id.loading_progress);
       // textView = view.findViewById(R.id.txt_loading_progress);
        //btnCancel = view.findViewById(R.id.btn_loading_progress_cancel);
        //btnCancel.setOnClickListener(v -> {
          //  if (mListener != null) mListener.onClick(dialog);
        //});
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setProgress(int value) {
        progressBar.setProgress(value);
        textView.setText(value + "%");
    }

    public interface OnClickListener {
        void onClick(AlertDialog dialog);
    }
}
