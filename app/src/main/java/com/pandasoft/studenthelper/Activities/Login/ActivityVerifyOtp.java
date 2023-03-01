package com.pandasoft.studenthelper.Activities.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pandasoft.studenthelper.Activities.Settings.ActivitySettings;
import com.pandasoft.studenthelper.Entities.EntityUser;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;
import com.pandasoft.studenthelper.Tools.UploadCls;
import com.pandasoft.studenthelper.ViewModels.ViewModelUser;


public class ActivityVerifyOtp extends AppCompatActivity {
    Button btnResend;
    EditText txtOtp1, txtOtp2, txtOtp3, txtOtp4, txtOtp5, txtOtp6;
    TextView txtPhone;
    String phoneNumber, verifyCode;
    ViewModelUser viewModelUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
        setEvents();
        setUpCounter();
    }

    void initialize() {
        //get bundle data
        Bundle bundle = getIntent().getExtras();
        this.phoneNumber = bundle.getString("phone");
        this.verifyCode = bundle.getString("code");

        txtOtp1 = findViewById(R.id.input_otp_code_1);
        txtOtp2 = findViewById(R.id.input_otp_code_2);
        txtOtp3 = findViewById(R.id.input_otp_code_3);
        txtOtp4 = findViewById(R.id.input_otp_code_4);
        txtOtp5 = findViewById(R.id.input_otp_code_5);
        txtOtp6 = findViewById(R.id.input_otp_code_6);

        progressBar = findViewById(R.id.progress_bar);

        txtPhone = findViewById(R.id.txt_phone_number);
        txtPhone.setText(phoneNumber);

        btnResend = findViewById(R.id.btn_resend_otp);

        viewModelUser = new ViewModelProvider(this).get(ViewModelUser.class);
    }

    void setEvents() {
        setBoxesEvents();
        btnResend.setOnClickListener(v -> {
            Dialog dialog = new Dialog(ActivityVerifyOtp.this, R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen);
            dialog.setContentView(R.layout.progress_loading);
            dialog.show();
            MyToolsCls.sendOTP(this, this.phoneNumber, new MyToolsCls.OnFinishOTPListener() {
                @Override
                public void onSuccess(String code) {
                    dialog.dismiss();
                    Toast.makeText(ActivityVerifyOtp.this, "تم ارسال الرمز", Toast.LENGTH_SHORT).show();
                    ActivityVerifyOtp.this.verifyCode = code;
                }

                @Override
                public void onFailure(String msg) {
                    dialog.dismiss();
                    Toast.makeText(ActivityVerifyOtp.this, msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPre() {

                    Toast.makeText(ActivityVerifyOtp.this, "جاري ارسال الرمز", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPost() {

                }
            });
            setUpCounter();
        });
    }

    void setBoxesEvents() {
        txtOtp1.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp1.setText(String.valueOf(keyCode - 7));
                txtOtp2.requestFocus();
            } else if (keyCode == 67) {
                txtOtp1.setText("");
            }
            return false;
        });
        txtOtp2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp2.setText(String.valueOf(keyCode - 7));
                txtOtp3.requestFocus();

            } else if (keyCode == 67) {
                txtOtp2.setText("");
                txtOtp1.requestFocus();
            }
            return false;
        });
        txtOtp3.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp3.setText(String.valueOf(keyCode - 7));
                txtOtp4.requestFocus();
            } else if (keyCode == 67) {
                txtOtp3.setText("");
                txtOtp2.requestFocus();
            }
            return false;
        });
        txtOtp4.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp4.setText(String.valueOf(keyCode - 7));
                txtOtp5.requestFocus();
            } else if (keyCode == 67) {
                txtOtp4.setText("");
                txtOtp3.requestFocus();
            }
            return false;
        });

        txtOtp5.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp5.setText(String.valueOf(keyCode - 7));
                txtOtp6.requestFocus();
            } else if (keyCode == 67) {
                txtOtp5.setText("");

                txtOtp4.requestFocus();
            }
            return false;
        });
        txtOtp6.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode > 6 && keyCode < 17) {
                txtOtp6.setText(String.valueOf(keyCode - 7));
                // check inputs

                if (!testInputs()) return false;
                // get code from inputs
                String code
                        = txtOtp1.getText().toString().trim()
                        + txtOtp2.getText().toString().trim()
                        + txtOtp3.getText().toString().trim()
                        + txtOtp4.getText().toString().trim()
                        + txtOtp5.getText().toString().trim()
                        + txtOtp6.getText().toString().trim();
                // start verify
                verify(code);

            }
            if (keyCode == 67) {
                txtOtp6.setText("");
                txtOtp5.requestFocus();
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void setUpCounter() {
        btnResend.setEnabled(false);
        int time = 60 * 1000;
        new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long diff = time - millisUntilFinished;
                btnResend.setText(((time - diff) / 1000) + " ثانية");
            }

            @Override
            public void onFinish() {
                btnResend.setEnabled(true);
                btnResend.setText("اعادة ارسال الرمز");
            }
        }.start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean testInputs() {
        return !txtOtp1.getText().toString().trim().isEmpty()
                && !txtOtp2.getText().toString().trim().isEmpty()
                && !txtOtp3.getText().toString().trim().isEmpty()
                && !txtOtp4.getText().toString().trim().isEmpty()
                && !txtOtp5.getText().toString().trim().isEmpty()
                && !txtOtp6.getText().toString().trim().isEmpty();
    }

    private void verify(String code) {

        if (!code.trim().isEmpty()) {

            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthCredential phoneAuth = PhoneAuthProvider.getCredential(verifyCode.trim(), code.trim());
            FirebaseAuth.getInstance().signInWithCredential(phoneAuth)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            signIn();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ActivityVerifyOtp.this, "كود التحقق غير صحيح:"
                                    + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }


                    }).addOnFailureListener(e -> {
                Toast.makeText(ActivityVerifyOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ActivityVerifyOtp.this, "كود خاطئ:" + code, Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        //1# check if user is found
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query query = reference.equalTo(this.phoneNumber, "phone").limitToFirst(1);

        UploadCls.DownloadEntityTask<EntityUser> task = new UploadCls.DownloadEntityTask<>(this, "user", query, data -> {
            EntityUser entity = data.getValue(EntityUser.class);
            if (entity != null && entity.getPhone().equals(ActivityVerifyOtp.this.phoneNumber)) {
                //1.1# login with founded user
                login(ActivityVerifyOtp.this, entity);
            }
        });
        task.setOnFailure(() -> {
            EntityUser entity = new EntityUser();
            entity.setPhone(this.phoneNumber);
            new UploadCls.UploadEntityTask<>(this, "user", entity, new MyToolsCls.OnUpload() {
                @Override
                public void onSuccess() {
                    login(ActivityVerifyOtp.this, entity);
                }

                @Override
                public void onFailure(String error) {

                }
            }).execute();//end upload entity

        });
        task.execute();
        // start register user

        // end register user
    }

    private void login(@NonNull Context context, @NonNull EntityUser user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("main_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("phone", this.phoneNumber);
        //editor.putBoolean("is_admin", user.getIs_admin());
        editor.putString("user_name", user.getUser_name());
        editor.putString("user_id", user.getId());

        // this value used for remember the user if is logged in
        editor.putBoolean("is_logged", true);
        editor.putBoolean("is_setup", false);

        editor.apply();
        editor.commit();
        // open setup settings
        Intent intent = new Intent(context, ActivitySettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mode", "setup");
        context.startActivity(intent);
    }
}