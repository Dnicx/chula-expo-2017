package cuexpo.cuexpo2017.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.IOException;

import cuexpo.cuexpo2017.manager.http.ApiService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by James on 12/25/2016.
 */
public class HttpManager {

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    private Context mContext;
    private ApiService service;

    private HttpManager()
    {
        mContext = Contextor.getInstance().getContext();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                //.setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.chulaexpo.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(ApiService.class);
    }

    public void setAPIKey(final String apiKey){
        mContext = Contextor.getInstance().getContext();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                //.setLenient()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "JWT " + apiKey); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.chulaexpo.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(ApiService.class);
    }

    public ApiService getService() {
        return service;
    }
}
