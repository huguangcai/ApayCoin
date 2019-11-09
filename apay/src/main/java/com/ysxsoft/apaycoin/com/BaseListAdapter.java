package com.ysxsoft.apaycoin.com;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述： listview 的适配器的基类
 * 日期： 2018/10/23 0023 11:53
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> mDatas;
    protected Context mContext;

    public BaseListAdapter(Context context) {
        this.mDatas = new ArrayList<T>();
        this.mContext = context;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

//	public int getDataCount(){
//		return mDatas.size();
//	}

    @Override
    public Object getItem(int position) {
        if (mDatas.size() <= position)return null;
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }

    /**
     * 清除当前列表数据，并追加新数据
     * @param datas
     */
    public void clearAndAppendData(List<T> datas){
        this.mDatas.clear();
        appendDatas(datas);
    }

    /**
     * 删除一条数据
     * @param mData
     */
    public void removeData(T mData){
        this.mDatas.remove(mData);
        notifyDataSetChanged();
    }

    /**
     * 删除一条数据
     * @param position 位置索引
     */
    public void removeData(int position){
        T mData = mDatas.get(position);
        this.mDatas.remove(mData);
        notifyDataSetChanged();
    }

    /**
     * 追加一条数据
     * @param mData
     */
    public void appendData(T mData){
        if(mData != null){
            this.mDatas.add(mData);
            notifyDataSetChanged();
        }
    }

    /**
     * 追加列表数据集合
     * @param datas
     */
    public void appendDatas(List<T> datas){
        if(datas != null && datas.size() > 0){
            this.mDatas.addAll(datas);
            notifyDataSetChanged();
        }

    }

    /**
     * 清除列表数据
     */
    public void clearDatas(){
        if(mDatas.size() > 0){
            mDatas.clear();
            notifyDataSetChanged();
        }
    }
}
