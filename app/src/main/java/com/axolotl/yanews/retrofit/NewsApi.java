package com.axolotl.yanews.retrofit;

import com.axolotl.yanews.retrofit.entity.CateResEntity;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by axolotl on 16/9/3.
 */
public interface NewsApi {

    String BASE_URL = "http://route.showapi.com/";


    @GET("109-35")
    Observable<CateResEntity> listNews(
            @Query("channelId") String channelId,
            @Query("page") int page);


}
