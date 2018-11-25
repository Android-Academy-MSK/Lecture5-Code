package ru.alexbykov.networksample.giflist.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.alexbykov.networksample.common.presentation.BasePresenter;
import ru.alexbykov.networksample.data.network.DefaultResponse;
import ru.alexbykov.networksample.data.network.RestApi;
import ru.alexbykov.networksample.data.network.dto.GifDTO;
import ru.alexbykov.networksample.giflist.common.State;

@InjectViewState
public class GifListPresenter extends BasePresenter<GifListView> {

    private static final String DEFAULT_SEARCH_REQUEST = "Bojack Horseman ";


    private RestApi restApi;

    public GifListPresenter(@NonNull RestApi instance) {
        this.restApi = instance;
    }

    @Override
    protected void onFirstViewAttach() {
        requestGifs(DEFAULT_SEARCH_REQUEST);
    }

    private void requestGifs(@NonNull String querySearch) {
        getViewState().showState(State.Loading);

        final Disposable searchDisposable =
                restApi.gifs()
                        .search(querySearch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::checkResponseAndShowState, this::handleError);
        disposeOnDestroy(searchDisposable);
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            getViewState().showState(State.NetworkError);
            return;
        }
        getViewState().showState(State.ServerError);
    }

    private void checkResponseAndShowState(@NonNull Response<DefaultResponse<List<GifDTO>>> response) {
        //Here I use Guard Clauses. You can find more here:
        //https://refactoring.com/catalog/replaceNestedConditionalWithGuardClauses.html

        //Here we have 4 clauses:

        if (!response.isSuccessful()) {
            getViewState().showState(State.ServerError);
            return;
        }

        final DefaultResponse<List<GifDTO>> body = response.body();
        if (body == null) {
            getViewState().showState(State.HasNoData);
            return;
        }

        final List<GifDTO> data = body.getData();
        if (data == null) {
            getViewState().showState(State.HasNoData);
            return;
        }

        if (data.isEmpty()) {
            getViewState().showState(State.HasNoData);
            return;
        }

        getViewState().showData(data);
        getViewState().showState(State.HasData);
    }

    public void onQueryGifs(@Nullable String query) {
        if (!QueryValidator.isValid(query)) {
            return;
        }
        requestGifs(query);
    }

    public void onClickTryAgain(@Nullable String currentSearch) {
        if (QueryValidator.isValid(currentSearch)) {
            requestGifs(currentSearch);
        } else {
            requestGifs(DEFAULT_SEARCH_REQUEST);
        }
    }
}
