package ru.alexbykov.networksample.giflist.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.alexbykov.networksample.data.network.dto.GifDTO;
import ru.alexbykov.networksample.giflist.common.State;


@StateStrategyType(AddToEndStrategy.class)
public interface GifListView extends MvpView {


    @StateStrategyType(OneExecutionStateStrategy.class)
    void showData(@NonNull List<GifDTO> gifs);

    void showState(@NonNull State state);

}
