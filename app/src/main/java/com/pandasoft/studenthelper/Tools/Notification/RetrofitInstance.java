package com.pandasoft.studenthelper.Tools.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit = null;

    public static Retrofit retrofit() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }

    public static NotificationAPI api() {
        return retrofit().create(NotificationAPI.class);
    }
}

