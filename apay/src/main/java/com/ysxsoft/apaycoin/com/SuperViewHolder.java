package com.ysxsoft.apaycoin.com;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * 描述： TODO
 * 日期： 2018/11/13 0013 13:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class SuperViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;

    public SuperViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
