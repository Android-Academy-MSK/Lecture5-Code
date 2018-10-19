package ru.alexbykov.networksample.data.network.dto;

public class GifDTO {

    private ImagesDTO images;

    public String getUrl() {
        return images.getOriginal().getUrl();
    }
}
