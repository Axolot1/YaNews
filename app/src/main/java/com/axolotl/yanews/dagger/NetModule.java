package com.axolotl.yanews.dagger;

import android.app.Application;

import com.axolotl.yanews.BuildConfig;
import com.axolotl.yanews.model.NewsLoader;
import com.axolotl.yanews.retrofit.ContentPicDeserializer;
import com.axolotl.yanews.retrofit.NewsContentDeserializer;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.retrofit.NewsApi;
import com.axolotl.yanews.retrofit.entity.ContentPic;
import com.axolotl.yanews.retrofit.entity.NewsContent;
import com.axolotl.yanews.utils.Prefs;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmList;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by axolotl on 16/6/27.
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {

        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("showapi_appid", BuildConfig.SHOW_APPID)
                                .addQueryParameter("showapi_sign", BuildConfig.SHOW_SIGN)
                                .build();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    NewsApi provideNewsApi(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<RealmList<NewsContent>>() {
                }.getType()
                , new NewsContentDeserializer())
                .registerTypeAdapter(new TypeToken<RealmList<ContentPic>>() {
                }.getType(), new ContentPicDeserializer())
                .create();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NewsApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(NewsApi.class);
    }

    @Provides
    @Singleton
    NewsLoader provideDataLoader(NewsApi api){
        return new NewsLoader(api);
    }



    @Provides
    @Singleton
    public OauthClient.Endpoints provideApi(Prefs prefs) {
        return OauthClient.getApiService(prefs);
    }


}
