package ru.alexbykov.networksample.data.network;

import android.support.annotation.Nullable;

public class DefaultResponse<T> {

    private T data;

    @Nullable
    public T getData() {
        return data;
    }
}
