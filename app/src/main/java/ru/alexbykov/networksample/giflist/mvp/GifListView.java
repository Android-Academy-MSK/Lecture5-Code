package ru.alexbykov.networksample.giflist.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.alexbykov.networksample.data.network.dto.GifDTO;
import ru.alexbykov.networksample.giflist.common.State;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface GifListView extends MvpView {


    void showData(@NonNull List<GifDTO> gifs);

    void showState(@NonNull State state);

}
