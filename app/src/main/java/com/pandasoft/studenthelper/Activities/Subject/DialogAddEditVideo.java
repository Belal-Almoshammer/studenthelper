package com.pandasoft.studenthelper.Activities.Subject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pandasoft.studenthelper.Entities.EntityVideo;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelVideo;

import java.util.Calendar;
import java.util.Date;

public class DialogAddEditVideo extends Dialog {
    Button btnSave;
    ImageButton btnClose;
    EditText txtTitle, txtPath;
    ViewModelVideo viewModelVideo;
    EntityVideo entityVideo;
    MyToolsCls.QUERY_TYPE query_type;
    TextView label;
    String id_sub;


    public DialogAddEditVideo(@NonNull Context context, ViewModelVideo viewModel, MyToolsCls.QUERY_TYPE q_type, EntityVideo entity, String id_sub) {
        super(context, R.style.Theme_StudentHelper_MyDialogTheme);
        viewModelVideo = viewModel;
        query_type = q_type;
        entityVideo = entity;
        this.id_sub = id_sub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_video, null, false);
        setContentView(view);
        initialize(view);
        setEvents();
        loadData();
    }

    void initialize(View view) {
        txtTitle = view.findViewById(R.id.input_video_title);
        txtPath = view.findViewById(R.id.input_video_path);
        btnSave = view.findViewById(R.id.btn_save_video);
        btnClose = view.findViewById(R.id.btn_close_add_edit_video);
        label = view.findViewById(R.id.label_video_add_edit_title);
    }

    void setEvents() {
        btnClose.setOnClickListener(v -> DialogAddEditVideo.this.dismiss());
        btnSave.setOnClickListener(v -> {
            Time time = new Time();
            time.setToNow();
            Date c = Calendar.getInstance().getTime();

            if (!test_inputs()) return;
            if (entityVideo == null) entityVideo = new EntityVideo();

            entityVideo.setTitle(txtTitle.getText().toString());
            entityVideo.setCloud_url(txtPath.getText().toString());
            entityVideo.setDate_create(c.toString());
            entityVideo.setTime_create(time.toString());
            entityVideo.setId_sub(id_sub);

            entityVideo.setIs_uploaded(false);


            if (query_type == MyToolsCls.QUERY_TYPE.INSERT)
            {
                entityVideo.setId(MyToolsCls.generateId());
                viewModelVideo.insert(entityVideo, msg -> dismiss());
            }
            else if (query_type == MyToolsCls.QUERY_TYPE.UPDATE)
                viewModelVideo.update(entityVideo, msg -> dismiss());
        });
    }

    boolean test_inputs() {
        boolean valid = true;
        if (txtTitle.getText() == null || txtTitle.getText().length() == 0) {
            txtTitle.setError("يجب كتابة اسم الفيديو");
            valid = false;
        }
        if (txtPath.getText() == null || txtPath.getText().length() == 0) {
            txtPath.setError("يجب تحديد رابط");
            valid = false;
        } else if (!URLUtil.isValidUrl(txtPath.getText().toString())) {
            txtPath.setError("رابط غير صحيح");
            valid = false;
        }
        return valid;
    }

    private void loadData() {
        if (entityVideo != null && query_type == MyToolsCls.QUERY_TYPE.UPDATE) {
            label.setText("تعديل الفيديو");
            txtTitle.setText(entityVideo.getTitle());
            txtPath.setText(entityVideo.getCloud_url());
        }
    }

}
