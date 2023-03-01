package com.pandasoft.studenthelper.Activities.Quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasoft.studenthelper.Adapters.AdapterQuestion;
import com.pandasoft.studenthelper.Dialog.BottomSheetOptions;
import com.pandasoft.studenthelper.Entities.EntityQuestion;
import com.pandasoft.studenthelper.Entities.EntityQuiz;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelQuestion;
import com.pandasoft.studenthelper.ViewModels.ViewModelQuiz;

import java.util.ArrayList;


public class ActivityAddEditQuiz extends AppCompatActivity {
    String id_sub;
    String mode;
    AdapterQuestion adapter;
    EntityQuiz entity;

    EditText txtTitle, txtDescription;
    RecyclerView recyclerViewQuestions;

    Button btnClose, btnSave, btnAdd;
    TextView txtLabel;

    ViewModelQuiz viewModelQuiz;
    ViewModelQuestion viewModelQuestion;
    private boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_quiz);
        getWindow().setStatusBarColor(Color.WHITE);
        initialize();
        setAdapter();
        setEvents();
        loadDate();
    }


    void initialize() {
        txtTitle = findViewById(R.id.input_title);
        txtDescription = findViewById(R.id.input_description);
        txtLabel = findViewById(R.id.title);

        btnSave = findViewById(R.id.btn_save);
        btnClose = findViewById(R.id.btn_close);

        btnAdd = findViewById(R.id.btn_add_question);

        recyclerViewQuestions = findViewById(R.id.questions_list);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));

        viewModelQuiz = new ViewModelProvider(this).get(ViewModelQuiz.class);
        viewModelQuestion = new ViewModelProvider(this).get(ViewModelQuestion.class);
    }

    private void setAdapter() {

        is_admin = DataCls.getBoolean(this, "is_admin");
        adapter = new AdapterQuestion(this, is_admin);
        adapter.setList(new ArrayList<>());
        recyclerViewQuestions.setAdapter(adapter);
    }

    private void setEvents() {
        btnClose.setOnClickListener(v -> {
            exitByBackKey();
        });
        btnSave.setOnClickListener(v -> {

            if (!testInputs()) return;
            if (entity == null) entity = new EntityQuiz();
            entity.setId_sub(id_sub);
            entity.setName(txtTitle.getText().toString());
            entity.setDescription(txtDescription.getText().toString());
            entity.setQuestions_count(adapter.getList().size());

            entity.setIs_uploaded(false);

            // insert quiz
            if (mode.equals("INSERT")) {
                entity.setId(MyToolsCls.generateId());
                viewModelQuiz.insert(entity, msg -> {
                    addQuestions(entity);
                    finish();
                });
            } else {
                viewModelQuiz.update(entity, msg -> {
                    addQuestions(entity);
                    finish();
                });
            }
        });
        btnAdd.setOnClickListener(v -> {
            DialogAddEditQuestion dialog = new DialogAddEditQuestion(this,
                    MyToolsCls.QUERY_TYPE.INSERT, adapter, -1);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.show();
        });
        adapter.setOnClickItemListener((handler, position) -> {
            BottomSheetOptions dialog = new BottomSheetOptions();
            dialog.setOnClickButtonOption(new BottomSheetOptions.OnClickButtonOption() {
                @Override
                public void onClickButtonDelete() {
                    adapter.remove(position);
                }

                @Override
                public void onClickButtonEdit() {
                    new DialogAddEditQuestion(ActivityAddEditQuiz.this,
                            MyToolsCls.QUERY_TYPE.UPDATE, adapter, position).show();
                }
            });
            dialog.show(getSupportFragmentManager(), "BOTTOM_SHEET_DIALOG");
        });
    }

    private void addQuestions(EntityQuiz entity) {
        viewModelQuestion.deleteByQuizId(entity.getId());
        for (EntityQuestion question : adapter.getList()) {
            question.setQuiz_id(entity.getId());
            viewModelQuestion.insert(question, msg -> {
            });
        }
    }

    private boolean testInputs() {
        boolean valid = MyToolsCls.checkError(txtTitle, "يجب كتابة العنوان");
        valid &= MyToolsCls.checkError(txtDescription, "يجب كتابة وصف مختصر");

        if (adapter.getList() == null || adapter.getList().size() == 0) {
            Toast.makeText(this, "يجب اضافة أسئلة", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    protected void exitByBackKey() {
        new AlertDialog.Builder(this)
                .setMessage("هل تريد الغاء العملية؟")
                .setPositiveButton("نعم", (dialog, which) -> finish())
                .setNegativeButton("لا", (dialog, which) -> {
                    // no things to do
                }).show();
    }

    private void loadDate() {
        Bundle bundle = getIntent().getExtras();
        id_sub = bundle.getString("id_sub");
        mode = bundle.getString("mode");
        if (mode.equals("INSERT")) {
            txtLabel.setText("اضافة تدريب");
        } else {
            txtLabel.setText("تعديل تدريب");
            int quiz_id = bundle.getInt("quiz_id");
            if (quiz_id <= 0) {
                Toast.makeText(this, "لم يتم تحميل البيانات", Toast.LENGTH_SHORT).show();
                finish();
            }
            viewModelQuiz.getQuiz(quiz_id).observe(this, entityQuiz -> {
                entity = entityQuiz;
                txtTitle.setText(entityQuiz.getName());
                txtDescription.setText(entityQuiz.getDescription());

                viewModelQuestion.getAllData(quiz_id).observe(this, list -> {
                    adapter.setList(list);
                });
            });
        }



    }

    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}