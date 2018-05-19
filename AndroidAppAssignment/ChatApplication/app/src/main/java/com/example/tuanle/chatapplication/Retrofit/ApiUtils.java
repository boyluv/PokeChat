package com.example.tuanle.chatapplication.Retrofit;

/**
 * Created by tuanle on 3/4/18.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://radiant-mountain-55866.herokuapp.com/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
