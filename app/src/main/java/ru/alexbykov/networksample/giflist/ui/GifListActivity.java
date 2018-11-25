package ru.alexbykov.networksample.giflist.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;

import java.util.List;

import ru.alexbykov.networksample.R;
import ru.alexbykov.networksample.data.network.RestApi;
import ru.alexbykov.networksample.data.network.dto.GifDTO;
import ru.alexbykov.networksample.giflist.common.State;
import ru.alexbykov.networksample.giflist.mvp.GifListPresenter;
import ru.alexbykov.networksample.giflist.mvp.GifListView;
import ru.alexbykov.networksample.giflist.ui.adapter.PhotosAdapter;

public final class GifListActivity extends MvpAppCompatActivity implements GifListView {

    private static final int LAYOUT = R.layout.activity_main;


    @InjectPresenter
    GifListPresenter gifListPresenter;


    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView rvPhotos;
    private PhotosAdapter photosAdapter;
    private Button btnTryAgain;
    private View viewError;
    private View viewLoading;
    private View viewNoData;
    private TextView tvError;


    @ProvidePresenter
    GifListPresenter provideGifListPresenter() {
        return new GifListPresenter(RestApi.getInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        setupUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUx();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindUx();
    }

    private void unbindUx() {
        searchView.setOnQueryTextListener(null);
        btnTryAgain.setOnClickListener(null);
    }

    private void setupUi() {
        findViews();
        toolbar.setTitle(R.string.toolbar_search_tittle);
        setupRecyclerViews();
        setupSearchView();
    }

    private void setupUx() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                gifListPresenter.onQueryGifs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //we can use this callback if we want to make request when text change
                return false;
            }
        });
        btnTryAgain.setOnClickListener(v -> gifListPresenter.onClickTryAgain(searchView.getQuery().toString()));
    }


    @Override
    public void showData(@NonNull List<GifDTO> gifs) {
        photosAdapter.replaceItems(gifs);
    }

    @Override
    public void showState(@NonNull State state) {

        switch (state) {
            case HasData:
                viewError.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                rvPhotos.setVisibility(View.VISIBLE);
                break;

            case HasNoData:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);

                viewError.setVisibility(View.VISIBLE);
                viewNoData.setVisibility(View.VISIBLE);
                break;
            case NetworkError:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_network));
                viewError.setVisibility(View.VISIBLE);
                break;

            case ServerError:
                rvPhotos.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                tvError.setText(getText(R.string.error_server));
                viewError.setVisibility(View.VISIBLE);
                break;
            case Loading:
                viewError.setVisibility(View.GONE);
                rvPhotos.setVisibility(View.GONE);
                viewNoData.setVisibility(View.GONE);

                viewLoading.setVisibility(View.VISIBLE);
                break;


            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }


    private void setupSearchView() {
        toolbar.inflateMenu(R.menu.menu_search);
        final MenuItem item = toolbar.getMenu().findItem(R.id.menu_item_search);
        searchView = (SearchView) item.getActionView();
    }

    private void setupRecyclerViews() {
        photosAdapter = new PhotosAdapter(Glide.with(this));
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));
        rvPhotos.setAdapter(photosAdapter);
    }

    private void findViews() {
        rvPhotos = findViewById(R.id.rv_photos);
        toolbar = findViewById(R.id.toolbar);
        btnTryAgain = findViewById(R.id.btn_try_again);
        viewError = findViewById(R.id.lt_error);
        viewLoading = findViewById(R.id.lt_loading);
        viewNoData = findViewById(R.id.lt_no_data);
        tvError = findViewById(R.id.tv_error);
    }
}
