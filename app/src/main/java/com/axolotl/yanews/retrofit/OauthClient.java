package com.axolotl.yanews.retrofit;

import android.util.Base64;

import com.axolotl.yanews.retrofit.entity.AddCommentRes;
import com.axolotl.yanews.retrofit.entity.AuthToken;
import com.axolotl.yanews.retrofit.entity.CheckTokenResponse;
import com.axolotl.yanews.retrofit.entity.CheckVersionRes;
import com.axolotl.yanews.retrofit.entity.CommentRes;
import com.axolotl.yanews.retrofit.entity.DemoEndpointResponse;
import com.axolotl.yanews.retrofit.entity.OAuthToken;
import com.axolotl.yanews.utils.Prefs;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public class OauthClient {
    public static final String BASE_URL = "http://52.39.100.15/";

    public static Endpoints getApiService(final Prefs prefs) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                // TODO: probably prefs should be cached here.
                String credentials = String.format("%s:blank", prefs.getAuthToken());
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP))
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor).addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit.create(Endpoints.class);
    }

    public interface Endpoints {
        @POST("auth/google")
        Call<AuthToken> getAuthToken(@Body OAuthToken oauthToken);


        @GET("api/resource")
        Call<DemoEndpointResponse> getDemoEndpoint();

        @GET("api/comments")
        Call<CommentRes> getComments(@Query("link") String link);

        @FormUrlEncoded
        @POST("/api/addComment")
        Call<AddCommentRes> addComment(@Field("link") String link, @Field("body") String body);

        @GET("api/checkVersion")
        Call<CheckVersionRes> checkVersion(@Query("version") int version);


        @Streaming
        @GET
        Call<ResponseBody> downloadApk(@Url String fileUrl);

    }

}

