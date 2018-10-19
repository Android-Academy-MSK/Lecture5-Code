package ru.alexbykov.networksample.data.network.endpoints;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ru.alexbykov.networksample.data.network.DefaultResponse;
import ru.alexbykov.networksample.data.network.dto.GifDTO;

public interface GifEndpoint {
    @GET("gifs/search")
    Single<Response<DefaultResponse<List<GifDTO>>>> search(@Query("q") @NonNull String search);
}
