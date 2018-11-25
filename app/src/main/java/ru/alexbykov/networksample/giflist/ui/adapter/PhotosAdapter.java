package ru.alexbykov.networksample.giflist.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.networksample.data.network.dto.GifDTO;

public final class PhotosAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private RequestManager glideRequestManager;
    private final List<GifDTO> items = new ArrayList<>();

    public PhotosAdapter(RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return PhotoViewHolder.create(viewGroup, glideRequestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int position) {
        final GifDTO gif = items.get(position);
        photoViewHolder.bindItem(gif);
    }

    public void replaceItems(@NonNull List<GifDTO> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
