package com.gary.weatherdemo.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gary.weatherdemo.model.base.BaseItemBean;
import com.gary.weatherdemo.ui.viewholder.BaseItemViewHolder;
import com.gary.weatherdemo.ui.viewholder.ItemViewHolderFactory;

import java.util.ArrayList;

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<BaseItemViewHolder> {
    private ArrayList<BaseItemBean> ItemDataList = new ArrayList<>();

    public ForecastRecyclerAdapter() {
    }

    public void setAdapterData(ArrayList<BaseItemBean> datas) {
        ItemDataList.clear();
        ItemDataList = datas;
        notifyDataSetChanged();
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ItemViewHolderFactory.getViewHolderByType(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseItemViewHolder holder, int position) {
        holder.getIViewItem().bindView(ItemDataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return ItemDataList == null ? 0 : ItemDataList.get(position).getViewItemType();
    }

    @Override
    public int getItemCount() {
        return ItemDataList.size();
    }
}
