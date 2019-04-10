package me.firdaus1453.crudmakanan.data.remote;

import me.firdaus1453.crudmakanan.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by firdaus1453 on 2/28/2019.
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        // membuat object logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // set log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Membuat object httpClient
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Menambahkan logging interceptor ke dalam httpClient
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

}
