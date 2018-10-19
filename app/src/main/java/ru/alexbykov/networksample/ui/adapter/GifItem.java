package ru.alexbykov.networksample.ui.adapter;

import android.support.annotation.NonNull;

import java.util.Objects;

public class GifItem {

    private final String gifUrl;
    private final String userTwitterNickName;
    private final String userName;
    private final String userPhotoUrl;
    private final String rating;


    public GifItem create(@NonNull String gifUrl,
                          @NonNull String userTwitterNickName,
                          @NonNull String userName,
                          @NonNull String userPhotoUrl,
                          @NonNull String rating) {
        return new GifItem(gifUrl, userTwitterNickName, userName, userPhotoUrl, rating);
    }

    public GifItem(String gifUrl,
                   String userTwitterNickName,
                   String userName,
                   String userPhotoUrl,
                   String rating) {
        this.gifUrl = gifUrl;
        this.userTwitterNickName = userTwitterNickName;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.rating = rating;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getUserTwitterNickName() {
        return userTwitterNickName;
    }

    public String getRating() {
        return rating;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GifItem gifItem = (GifItem) o;
        return Objects.equals(gifUrl, gifItem.gifUrl) &&
                Objects.equals(userTwitterNickName, gifItem.userTwitterNickName) &&
                Objects.equals(userName, gifItem.userName) &&
                Objects.equals(userPhotoUrl, gifItem.userPhotoUrl) &&
                Objects.equals(rating, gifItem.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gifUrl, userTwitterNickName, userName, userPhotoUrl, rating);
    }
}
