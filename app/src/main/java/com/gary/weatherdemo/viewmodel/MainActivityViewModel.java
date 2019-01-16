package com.gary.weatherdemo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.gary.weatherdemo.model.CityBean;
import com.gary.weatherdemo.model.DayForecastBean;
import com.gary.weatherdemo.model.LiveWeatherBean;
import com.gary.weatherdemo.model.base.BaseItemBean;
import com.gary.weatherdemo.network.WeatherRequestClient;
import com.gary.weatherdemo.network.response.AllForecastResponseData;
import com.gary.weatherdemo.network.response.LiveWeatherResponseData;
import com.gary.weatherdemo.ui.adapter.ForecastRecyclerAdapter;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivityViewModel extends AndroidViewModel {
    public final ObservableField<ForecastRecyclerAdapter> weatherAdapter = new ObservableField<>();
    private final ForecastRecyclerAdapter adapter;
    private MutableLiveData<CityBean> curCityBean = new MutableLiveData<>();

    private ArrayList<BaseItemBean> ItemDataList = new ArrayList<>();
    private MutableLiveData<LiveWeatherBean> liveWeatherData = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        adapter = new ForecastRecyclerAdapter();
    }

    public void queryCityWeather(String adcode) {
        Observable<LiveWeatherResponseData> observable1 =
                WeatherRequestClient.getInstance().liveWeatherPost(adcode).subscribeOn(Schedulers.io());

        Observable<AllForecastResponseData> observable2 =
                WeatherRequestClient.getInstance().forecastWeatherPost(adcode).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2,
                new BiFunction<LiveWeatherResponseData, AllForecastResponseData, ArrayList<BaseItemBean>>() {
                    @Override
                    public ArrayList<BaseItemBean> apply(LiveWeatherResponseData livedata,
                                                         AllForecastResponseData allForecastdata) throws Exception {
                        List<DayForecastBean> dayForecastList = null;
                        ArrayList<BaseItemBean> dataList = new ArrayList<>();

                        if (livedata != null && livedata.isSuccessful()) {
                            liveWeatherData.postValue(livedata.getWeatherLiveResult());
                        }
                        if (allForecastdata != null
                                && allForecastdata.isSuccessful()
                                && allForecastdata.getWeatherAllResult() != null) {
                            dayForecastList = allForecastdata.getWeatherAllResult().dayForecastBeanList;
                        }

                        dataList.clear();
                        dataList.add(liveWeatherData.getValue());
                        for (DayForecastBean fdata : dayForecastList) {
                            dataList.add(fdata);
                        }

                        return dataList;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<BaseItemBean>>() {
                    @Override
                    public void accept(ArrayList<BaseItemBean> dataList) throws Exception {
                        ItemDataList = dataList;
                        adapter.setAdapterData(ItemDataList);
                        weatherAdapter.set(adapter);
                    }
                });
    }

    public MutableLiveData<LiveWeatherBean> getLiveWeatherData() {
        return liveWeatherData;
    }

    public void loadCurCityInfo() {
        curCityBean.setValue(new CityBean("深圳", "440300"));
    }

    public MutableLiveData<CityBean> getCurCityInfo() {
        return curCityBean;
    }
}
