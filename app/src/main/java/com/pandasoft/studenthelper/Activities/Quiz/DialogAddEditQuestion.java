package com.pandasoft.studenthelper.Activities.Quiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pandasoft.studenthelper.Adapters.AdapterQuestion;
import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;


public class DialogAddEditQuestion extends Dialog {
    private final MyToolsCls.QUERY_TYPE query_type;
    private final AdapterQuestion adapter;
    private final int position;
    TextView txtViewWindowTitle;
    EntityQuestion entity;
    private EditText txtTitle, txtSelection1, txtSelection2, txtSelection3, txtAnswer;
    private Button btnSave, btnClose, btnCancel;

    public DialogAddEditQuestion(Context context, MyToolsCls.QUERY_TYPE qType, AdapterQuestion adapter, int position) {
        super(context, R.style.Theme_StudentHelper_MyDialogTheme);
        query_type = qType;
        this.adapter = adapter;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_question, null, false);
        setContentView(view);
        initialize(view);
        initEvents();
        loadData();
    }

    private void initialize(View view) {
        txtViewWindowTitle = view.findViewById(R.id.dialog_title);
        txtTitle = view.findViewById(R.id.input_question_title);
        txtSelection1 = view.findViewById(R.id.input_question_selection1);
        txtSelection2 = view.findViewById(R.id.input_question_selection2);
        txtSelection3 = view.findViewById(R.id.input_question_selection3);
        txtAnswer = view.findViewById(R.id.input_question_answer);

        btnClose = view.findViewById(R.id.btn_close);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

    }

    private void initEvents() {
        btnClose.setOnClickListener(v -> dismiss());
        btnCancel.setOnClickListener(v -> dismiss());

        // Set Event Click for button save
        btnSave.setOnClickListener(v -> {
            if (!testInputs()) return;
            if (query_type == MyToolsCls.QUERY_TYPE.INSERT && entity == null)
                entity = new EntityQuestion();
            entity.setQuestion(txtTitle.getText().toString());
            entity.setSelection_1(txtSelection1.getText().toString());
            entity.setSelection_2(txtSelection2.getText().toString());
            entity.setSelection_3(txtSelection3.getText().toString());
            entity.setAnswer(txtAnswer.getText().toString());

            entity.setIs_uploaded(false);


            if (query_type == MyToolsCls.QUERY_TYPE.INSERT)
                adapter.add(entity);
            else if (query_type == MyToolsCls.QUERY_TYPE.UPDATE)
                adapter.set(position, entity);
            dismiss();
        });

    }

    private boolean testInputs() {

        boolean valid = MyToolsCls.checkError(txtTitle, "يجب كتابة السؤال بوضوح");
        valid &= MyToolsCls.checkError(txtSelection1, "يجب تحديد اجابة");
        valid &= MyToolsCls.checkError(txtSelection2, "يجب تحديد اجابة");
        valid &= MyToolsCls.checkError(txtSelection3, "يجب تحديد اجابة");
        valid &= MyToolsCls.checkError(txtAnswer, "يجب تحديد اجابة");

        return valid;
    }

    private void loadData() {
        if (query_type == MyToolsCls.QUERY_TYPE.UPDATE) {
            entity = adapter.get(position);
            txtTitle.setText(entity.getQuestion());
            txtSelection1.setText(entity.getQuestion());
            txtSelection2.setText(entity.getQuestion());
            txtSelection3.setText(entity.getQuestion());
            txtAnswer.setText(entity.getQuestion());
        }
    }

}
