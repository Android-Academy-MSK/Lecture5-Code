package ru.alexbykov.networksample.data.network.endpoints;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.alexbykov.networksample.data.network.DefaultResponse;
import ru.alexbykov.networksample.data.network.dto.GifDTO;

public interface GifEndpoint {
    @GET("gifs/search")
    Call<DefaultResponse<List<GifDTO>>> search(@Query("q") @NonNull String search);
}
