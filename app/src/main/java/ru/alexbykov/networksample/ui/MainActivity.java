package ru.alexbykov.networksample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.alexbykov.networksample.R;
import ru.alexbykov.networksample.data.network.DefaultResponse;
import ru.alexbykov.networksample.data.network.RestApi;
import ru.alexbykov.networksample.data.network.dto.GifDTO;
import ru.alexbykov.networksample.ui.adapter.PhotosAdapter;

public final class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    public static final String DEFAULT_SEARCH_REQUEST = "Bojack Horseman ";

    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView rvPhotos;
    private PhotosAdapter photosAdapter;
    private Button btnTryAgain;
    private View viewError;
    private View viewLoading;
    private View viewNoData;
    private TextView tvError;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


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
        loadGifs(DEFAULT_SEARCH_REQUEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindUx();
        compositeDisposable.clear();
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
                validateAndLoadGifs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //we can use this callback if we want to make request when text change
                return false;
            }
        });
        btnTryAgain.setOnClickListener(v -> onClickTryAgain());
    }

    private void onClickTryAgain() {
        final String query = searchView.getQuery().toString();
        if (query.isEmpty()) {
            loadGifs(DEFAULT_SEARCH_REQUEST);
        } else {
            validateAndLoadGifs(query);
        }
    }

    private void validateAndLoadGifs(@Nullable String query) {
        if (QueryValidator.isValid(query)) {
            loadGifs(query);
        }
    }


    private void loadGifs(@NonNull String search) {
        showState(State.Loading);
        final Disposable searchDisposable = RestApi.getInstance()
                .gifs()
                .search(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::checkResponseAndShowState, this::handleError);
        compositeDisposable.add(searchDisposable);
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            showState(State.NetworkError);
            return;
        }
        showState(State.ServerError);
    }


    private void checkResponseAndShowState(@NonNull Response<DefaultResponse<List<GifDTO>>> response) {
        //Here I use Guard Clauses. You can find more here:
        //https://refactoring.com/catalog/replaceNestedConditionalWithGuardClauses.html

        //Here we have 4 clauses:

        if (!response.isSuccessful()) {
            showState(State.ServerError);
            return;
        }

        final DefaultResponse<List<GifDTO>> body = response.body();
        if (body == null) {
            showState(State.HasNoData);
            return;
        }

        final List<GifDTO> data = body.getData();
        if (data == null) {
            showState(State.HasNoData);
            return;
        }

        if (data.isEmpty()) {
            showState(State.HasNoData);
            return;
        }

        photosAdapter.replaceItems(data);
        showState(State.HasData);
    }


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
