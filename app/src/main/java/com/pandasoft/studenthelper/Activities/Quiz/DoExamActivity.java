package com.pandasoft.studenthelper.Activities.Quiz;

import android.os.Bundle;
import android.text.format.Time;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.ViewModels.ViewModelQuestion;

import java.util.ArrayList;
import java.util.List;

public class DoExamActivity extends AppCompatActivity {

    private Button btnSelect1, btnSelect2, btnSelect3, btnSelect4, btnNext, btnCancel;
    private List<EntityQuestion> mList;
    private TextView txtCorrectCount, txtQuestionCount, txtCurrentQuestion, txtContent;
    private ViewModelQuestion viewModelQuestion;
    private int correctCount, question_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exam);
        initialize();
        initEvents();
        initData();
    }

    private void initialize() {

        btnSelect1 = findViewById(R.id.btn_selection_1);
        btnSelect2 = findViewById(R.id.btn_selection_2);
        btnSelect3 = findViewById(R.id.btn_selection_3);
        btnSelect4 = findViewById(R.id.btn_selection_4);
        btnNext = findViewById(R.id.btn_next_question);
        btnCancel = findViewById(R.id.btn_end_exam);

        txtCorrectCount = findViewById(R.id.txt_questions_correct_count);
        txtQuestionCount = findViewById(R.id.txt_view_questions_count);
        txtCurrentQuestion = findViewById(R.id.txt_view_question_no);
        txtContent = findViewById(R.id.txt_view_question_content);
        viewModelQuestion = new ViewModelProvider(this).get(ViewModelQuestion.class);
        //set values
        btnSelect1.setText("");
        btnSelect2.setText("");
        btnSelect3.setText("");
        btnSelect4.setText("");
        txtCorrectCount.setText("0");
        txtQuestionCount.setText("0");
        txtCurrentQuestion.setText("0");
        //btnSelect1.setEnabled(false);
        //btnSelect2.setEnabled(false);
        //btnSelect3.setEnabled(false);
        //btnSelect4.setEnabled(false);
    }

    private void initEvents() {
        btnCancel.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> {
            if (mList != null && question_position >= mList.size()) {
                return;
            }
            if (!btnSelect1.isEnabled()) {
                getNextQuiz();
            } else {
                Toast.makeText(this, "يجب اختيار سؤال!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSelect1.setOnClickListener(v -> isCorrectSelection(btnSelect1));
        btnSelect2.setOnClickListener(v -> isCorrectSelection(btnSelect2));
        btnSelect3.setOnClickListener(v -> isCorrectSelection(btnSelect3));
        btnSelect4.setOnClickListener(v -> isCorrectSelection(btnSelect4));
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        long quiz_id = bundle.getLong("quiz_id");
        if (quiz_id == 0) {
            Toast.makeText(this, "لايوجد أسئلة", Toast.LENGTH_SHORT).show();
            finish();
        }
        viewModelQuestion.getAllData(quiz_id).observe(this, list -> {
            if (list != null && list.size() > 0) {
                if (mList == null) mList = new ArrayList<>();
                mList.addAll(list);
                BeginQuiz();
            }
        });
    }

    private void getNextQuiz() {
        btnNext.setEnabled(false);
        txtCurrentQuestion.setText(String.valueOf((question_position + 1)));
        EntityQuestion entity = mList.get(question_position);
        txtContent.setText(entity.getQuestion());
        // Set Selection
        Time time = new Time();
        time.setToNow();
        int random = time.second % 4;
        if (random == 0) {
            btnSelect1.setText(entity.getAnswer());
            btnSelect1.setTag("correct");
            btnSelect2.setText(entity.getSelection_1());
            btnSelect2.setTag("");
            btnSelect3.setText(entity.getSelection_2());
            btnSelect3.setTag("");
            btnSelect4.setText(entity.getSelection_3());
            btnSelect4.setTag("");
        } else if (random == 1) {
            btnSelect1.setText(entity.getSelection_1());
            btnSelect1.setTag("");
            btnSelect2.setText(entity.getAnswer());
            btnSelect2.setTag("correct");
            btnSelect3.setText(entity.getSelection_2());
            btnSelect3.setTag("");
            btnSelect4.setText(entity.getSelection_3());
            btnSelect4.setTag("");

        } else if (random == 2) {
            btnSelect1.setText(entity.getSelection_1());
            btnSelect1.setTag("");
            btnSelect2.setText(entity.getSelection_2());
            btnSelect2.setTag("");
            btnSelect3.setText(entity.getAnswer());
            btnSelect3.setTag("correct");
            btnSelect4.setText(entity.getSelection_3());
            btnSelect4.setTag("");

        } else {
            btnSelect1.setText(entity.getSelection_1());
            btnSelect1.setTag("");
            btnSelect2.setText(entity.getSelection_2());
            btnSelect2.setTag("");
            btnSelect3.setText(entity.getSelection_3());
            btnSelect3.setTag("");
            btnSelect4.setText(entity.getAnswer());
            btnSelect4.setTag("correct");
        }
        btnSelect1.setEnabled(true);
        btnSelect2.setEnabled(true);
        btnSelect3.setEnabled(true);
        btnSelect4.setEnabled(true);
    }

    private void BeginQuiz() {
        btnNext.setEnabled(false);
       // btnCancel.setEnabled(false);
        btnSelect1.setEnabled(true);
        btnSelect2.setEnabled(true);
        btnSelect3.setEnabled(true);
        btnSelect4.setEnabled(true);
        btnSelect1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        btnSelect2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        btnSelect3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        btnSelect4.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        txtCorrectCount.setText("0");
        correctCount = 0;
        question_position = 0;
        if (mList != null && mList.size() > 0) {
            txtQuestionCount.setText(String.valueOf(mList.size()));
            txtCurrentQuestion.setText(String.valueOf((question_position + 1)));
            getNextQuiz();
        }
    }

    private void isCorrectSelection(Button btn) {
        if (btnSelect1.getTag() != "correct")
            btnSelect1.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        else
            btnSelect1.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

        if (btnSelect2.getTag() != "correct")
            btnSelect2.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        else
            btnSelect2.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

        if (btnSelect3.getTag() != "correct")
            btnSelect3.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        else
            btnSelect3.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

        if (btnSelect4.getTag() != "correct")
            btnSelect4.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        else
            btnSelect4.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

        if (btn.getTag() == "correct") {
            correctCount += 1;
            txtCorrectCount.setText(String.valueOf(correctCount));
        }
        btnSelect1.setEnabled(false);
        btnSelect2.setEnabled(false);
        btnSelect3.setEnabled(false);
        btnSelect4.setEnabled(false);

        question_position += 1;// increase position
        if (mList != null && question_position >= mList.size()) {
            Toast.makeText(this, "انتهت الاسئلة", Toast.LENGTH_SHORT).show();
            endQuiz();
        } else btnNext.setEnabled(true);
    }

    private void endQuiz() {
        btnNext.setEnabled(false);
        btnCancel.setEnabled(true);
        // Show dagre dialog
        DialogFinishQuiz dialog = new DialogFinishQuiz(this, getApplication(), mList != null ? mList.size() : 0, correctCount);
        dialog.setCloseActivity(new CloseActivityListener() {
            @Override
            public void closeActivity() {
                DoExamActivity.this.finish();
            }

            @Override
            public void tryAgain() {
                DoExamActivity.this.BeginQuiz();
            }
        }).show();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public interface CloseActivityListener {
        void closeActivity();

        void tryAgain();
    }
}
