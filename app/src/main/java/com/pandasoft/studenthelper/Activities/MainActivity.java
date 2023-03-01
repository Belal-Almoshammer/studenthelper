package com.pandasoft.studenthelper.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pandasoft.studenthelper.Activities.Main.FragmentMain;
import com.pandasoft.studenthelper.Activities.Notification.ActivityNotification;
import com.pandasoft.studenthelper.Activities.Settings.ActivitySettings;
import com.pandasoft.studenthelper.R;

public class MainActivity extends AppCompatActivity {

    TextView txtUsername, txtLevelName;
    String user_type;
    Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start Initialization
        setContentView(R.layout.activity_main);
        initialize();
        initializeNavigation();
        loadUserData();
        //set notifications topics
        FirebaseMessaging.getInstance().subscribeToTopic("STUDENT_HELPER_NOTIFICATIONS");
    }

    private void loadUserData() {
        SharedPreferences preferences = getSharedPreferences("main_pref", MODE_PRIVATE);
        String username = preferences.getString("user_name", "اسم المستخدم");
        String levelName = preferences.getString("level_name", "الثالث الثانوي");
        txtUsername.setText(username);
        txtLevelName.setText(levelName);
        user_type = preferences.getString("user_type", "guest");
    }

    private void showMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, FragmentMain.class, null)
                .commit();
    }

    void initialize() {
        txtUsername = findViewById(R.id.txt_username);
        txtLevelName = findViewById(R.id.txt_level_name);
        btnMenu = findViewById(R.id.btn_menu);
        //get android device id as user_id
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences sharedPreferences = getSharedPreferences("main_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", android_id);
        editor.apply();
        //button menu
        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.inflate(R.menu.main_context_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.option_info) {
                    Toast.makeText(this, "Student Helper", Toast.LENGTH_SHORT).show();
                }
                return false;
            });
            popup.show();

        });

        //________________________________________________________

        sharedPreferences.registerOnSharedPreferenceChangeListener((preferences, key) -> {
            if (key.equals("level_name")) {
                txtLevelName.setText(preferences.getString("level_name", " "));
            } else if (key.equals("user_name")) {
                txtUsername.setText(preferences.getString("user_name", " "));
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    void initializeNavigation() {

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_menu_item_home:
                    showMainFragment();
                    return true;
                case R.id.bottom_menu_item_options:
                    startActivity(new Intent(this, ActivitySettings.class));
                    return false;
                case R.id.bottom_menu_item_notifications:
                    startActivity(new Intent(this, ActivityNotification.class));
                    return false;
                case R.id.bottom_menu_item_chat:
                    Toast.makeText(this, "View chat", Toast.LENGTH_SHORT).show();
                    return false;
                default:
                    return false;
            }
        });
        navigationView.setSelectedItemId(R.id.bottom_menu_item_home);
    }
}