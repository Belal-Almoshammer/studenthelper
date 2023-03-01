package com.pandasoft.studenthelper.Activities.Quiz;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pandasoft.studenthelper.R;

public class DialogFinishQuiz extends Dialog {
    private final Application application;
    private final int questionsCount;
    private final int questionsTrueCount;
    DoExamActivity.CloseActivityListener mListener;
    Button btnTryAgain, btnCancel;

    public DialogFinishQuiz(@NonNull Context context, Application app, int count, int true_count) {
        super(context, R.style.Theme_StudentHelper_BoxDialog);
        application = app;
        questionsCount = count;
        questionsTrueCount = true_count;
    }

    public DialogFinishQuiz setCloseActivity(DoExamActivity.CloseActivityListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_finish_quiz, null, false);
        setContentView(view);
        initialize(view);
        setEvents();
    }

    private void initialize(View view) {
        TextView txtQuestionsCount = view.findViewById(R.id.result_questions_count);
        TextView txtCountTrue = view.findViewById(R.id.result_questions_true_count);
        TextView txtAverage = view.findViewById(R.id.result_questions_average);
        TextView txtRate = view.findViewById(R.id.result_questions_rate);
        btnCancel = view.findViewById(R.id.question_result_cancel);
        btnTryAgain = view.findViewById(R.id.question_result_try_again);
        txtQuestionsCount.setText("عدد الاسئلة: " + questionsCount);
        txtCountTrue.setText("الاجابات الصحيحة: " + questionsTrueCount);

        double average = (questionsTrueCount * 100 / questionsCount);
        txtAverage.setText("النسبة: " + (average));
        txtRate.setText("التقدير: " + getRate(average));
    }

    private String getRate(double average) {
        if (average >= 90)
            return "ممتاز";
        else if (average >= 80)
            return "جيد جدأ";
        else if (average >= 70)
            return "جيد";
        else if (average >= 60)
            return "مقبول";
        else
            return "ضعيف";
    }

    private void setEvents() {
        btnCancel.setOnClickListener(v -> {
            if (mListener != null)
                mListener.closeActivity();
            dismiss();
        });
        btnTryAgain.setOnClickListener(v -> {
            dismiss();
            if (mListener != null)
                mListener.tryAgain();
        });
    }
}
