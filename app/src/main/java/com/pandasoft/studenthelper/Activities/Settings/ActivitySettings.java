package com.pandasoft.studenthelper.Activities.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.pandasoft.studenthelper.Activities.MainActivity;
import com.pandasoft.studenthelper.Entities.EntityEducationalLevel;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelEducationLevel;

import java.util.ArrayList;
import java.util.List;

public class ActivitySettings extends AppCompatActivity {
    List<EntityEducationalLevel> mLevels;
    String id_level;

    Button btnSave, btnClose;
    EditText txtUsername;
    Spinner spinner;//spinner educational levels
    int level_position = -1;
    String mode;
    TextView txtPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        loadSettings();
        setAdapter();
        setEvents();
    }

    private void initialize() {
        btnSave = findViewById(R.id.btn_save);
        txtUsername = findViewById(R.id.input_username);
        spinner = findViewById(R.id.spinner_levels);

        if (getIntent().getExtras() != null)
            mode = getIntent().getExtras().getString("mode");

        btnClose = findViewById(R.id.btn_close);
        txtPhone = findViewById(R.id.txt_phone);
    }


    void setEvents() {
        txtUsername.setOnKeyListener((v, keyCode, event) -> {
            btnSave.setEnabled(true);
            return false;
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnSave.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(v -> {
            if (MyToolsCls.isNetworkConnected(this)) {
                int position = spinner.getSelectedItemPosition();
                SharedPreferences sharedPreferences = getSharedPreferences("main_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (position > -1 && mLevels != null && mLevels.size() > 0) {
                    editor.putString("id_level", mLevels.get(position).getId_level());
                    editor.putString("level_name", mLevels.get(position).getName());
                }
                editor.putString("user_name", txtUsername.getText().toString());
                editor.apply();
                // get user data
                if (mode != null && mode.equals("setup")) {
                    DataCls.putBoolean(this,"is_admin",false);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                } else finish();

            } else {
                Toast.makeText(this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        });
        btnClose.setOnClickListener(v -> onBackPressed());
    }

    void setAdapter() {
        // fill spinner
        new ViewModelProvider(this)
                .get(ViewModelEducationLevel.class).getEducationLevels()
                .observe(this, list -> {
                    if (mLevels == null) mLevels = new ArrayList<>();
                    mLevels.clear();
                    mLevels.addAll(list);
                    List<String> slist = new ArrayList<>();
                    int i = 0;
                    for (EntityEducationalLevel et : list) {
                        slist.add(et.getName());
                        if (et.getId_level().equals(id_level)) {
                            level_position = i;
                        }
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, slist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(level_position);
                });
    }

    private void loadSettings() {
        SharedPreferences preferences = getSharedPreferences("main_pref", MODE_PRIVATE);
        String username = preferences.getString("user_name", "");
        id_level = preferences.getString("id_level", "-1");

        txtUsername.setText(username);
    }

    protected void exitByBackKey() {
        if (mode != null && mode.equals("setup")) {
            new AlertDialog.Builder(this)
                    .setMessage("هل تريد الغاء العملية؟")
                    .setPositiveButton("نعم", (dialog, which) -> finish())
                    .setNegativeButton("لا", (dialog, which) -> { }).show();
        } else finish();

    }

    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}