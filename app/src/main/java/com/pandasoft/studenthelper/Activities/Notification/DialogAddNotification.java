package com.pandasoft.studenthelper.Activities.Notification;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.pandasoft.studenthelper.Entities.EntityNotifications;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.Notification.NotificationData;
import com.pandasoft.studenthelper.Tools.Notification.PushNotification;
import com.pandasoft.studenthelper.Tools.Notification.RetrofitInstance;
import com.pandasoft.studenthelper.ViewModels.ViewModelNotifications;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAddNotification extends Dialog {
    private static final String TAG = "DialogAddNotification";
    private final String TOPIC = "/topics/STUDENT_HELPER_NOTIFICATIONS";
    Button btnSend;
    ImageButton btnClose;
    EditText txtTitle, txtDescription;
    ViewModelNotifications viewModelNotifications;

    public DialogAddNotification(Context context, ViewModelNotifications viewModel) {
        super(context, R.style.Theme_StudentHelper_MyDialogTheme);
        viewModelNotifications = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_notification, null, false);
        setContentView(view);
        initialize(view);
        setEvents();
        setAdapter();
    }

    public void initialize(View view) {
        btnSend = view.findViewById(R.id.btn_send_notification);
        btnClose = view.findViewById(R.id.btn_close_add_notification);
        txtDescription = view.findViewById(R.id.input_notification_description);
        txtTitle = view.findViewById(R.id.input_notification_title);
    }


    public void setEvents() {
        EntityNotifications notification = new EntityNotifications();

        notification.setTitle(txtTitle.getText().toString());
        notification.setDescription(txtDescription.getText().toString());

        btnClose.setOnClickListener(v -> dismiss());

        btnSend.setOnClickListener(v -> {

            String title = txtTitle.getText().toString();
            String message = txtDescription.getText().toString();


            PushNotification data = new PushNotification(new NotificationData(title, message), TOPIC);

            Call<ResponseBody> call = RetrofitInstance.api().sendNotification(data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful())
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();


                    Log.d(TAG, "Response: " + response.toString());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            });
            //========================================================
        });//end button click
    }

    public void setAdapter() {

    }

    private void sendNotification(PushNotification notification) {
        try {
            Call<ResponseBody> response = RetrofitInstance.api().sendNotification(notification);
            if (response.isExecuted()) {
                Log.d(TAG, "Response: " + new Gson().toJson(response));
            } else {
                Log.e(TAG, response.toString());
            }
        } catch (Exception e) {
            Log.i(TAG, "sendNotification: " + e.getMessage());
        }
    }
}
