package com.pandasoft.studenthelper.Activities.Login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pandasoft.studenthelper.Activities.MainActivity;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.DataCls;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.List;

public class ActivityLogin extends AppCompatActivity {

    FloatingActionButton fabSendSmd;
    EditText txtKey, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        setEvents();
        Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(!multiplePermissionsReport.areAllPermissionsGranted()){
                    Toast.makeText(ActivityLogin.this, "يجب اعطاء الصلاحيات المطلوبة", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(err->{
            Toast.makeText(this, err.name(), Toast.LENGTH_SHORT).show();
        }).check();

        // فحص اذا كان المستخدم مسجل مسبقاً
        boolean is_logged = DataCls.getBoolean(this,"is_logged");
        if(is_logged){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void initialize() {
        fabSendSmd = findViewById(R.id.fab_send_sms);
        txtKey = findViewById(R.id.txt_phone_key);
        txtPhone = findViewById(R.id.txt_phone_number);
    }

    private String getPhone() {
        return ("+" + txtKey.getText().toString() + txtPhone.getText().toString()).trim();
    }

    private void setEvents() {
        fabSendSmd.setOnClickListener(v -> {

            if (getPhone().equals("+99915971937406")) {// the user is an admin
                SharedPreferences preferences = getSharedPreferences("main_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user_name", "كنترول");
                editor.putBoolean("is_admin", true);
                editor.putBoolean("is_logged", true);
                editor.apply();

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }

            if (!test()) return;
            MyToolsCls.sendOTP(this, getPhone(), new MyToolsCls.OnFinishOTPListener() {
                @Override
                public void onSuccess(String code) {
                    startVerifyActivity(code, getPhone());
                }

                @Override
                public void onFailure(String msg) {
                    // show the error message
                    Toast.makeText(ActivityLogin.this, msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPre() {
                    // start loading on fab
                    fabSendSmd.setVisibility(View.GONE);
                }

                @Override
                public void onPost() {
                    // end loading on fab
                    fabSendSmd.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    private boolean test() {

        boolean valid = true;

        if (txtKey.length() == 0) {
            txtKey.setError("يجب كتابة رمز الدولة");
            valid = false;
        }
        if (txtPhone.length() == 0) {
            txtPhone.setError("يجب كتابة  رقم الهاتف");
            valid = false;
        }
        if (!MyToolsCls.isNetworkConnected(this)) {
            Toast.makeText(this, "لايوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void startVerifyActivity(String code, String phone) {
        // go to ActivityVerifyOtp
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("phone", phone);

        Intent intent = new Intent(ActivityLogin.this, ActivityVerifyOtp.class);
        intent.putExtras(bundle);
        // start activity
        startActivity(intent);
        // set slide animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}