package com.kquicho.interestingphotos.network;


import com.kquicho.interestingphotos.models.InterestingnessResponse;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kquicho on 16-05-13.
 */
public class FlickrAPIClient {
    public static final String BASE_URL = "https://api.flickr.com/services/";
    private static FlickrApiInterface sFlickrService;

    public static FlickrApiInterface getFlickrApiClient(final String api) {
        if (sFlickrService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder()
                                    .addQueryParameter("api_key", api)
                                    .addQueryParameter("format","json")
                                    .addQueryParameter("nojsoncallback","1")
                                    .build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);
                        }
                    })
                    //.addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            sFlickrService = retrofit.create(FlickrApiInterface.class);
        }


        return sFlickrService;
    }

    public interface FlickrApiInterface {
        @GET("rest/?method=flickr.interestingness.getList")
        Call<InterestingnessResponse> getListOfInterestingness
                (@Query("page") String page, @Query("per_page") String per_page);
    }

}
