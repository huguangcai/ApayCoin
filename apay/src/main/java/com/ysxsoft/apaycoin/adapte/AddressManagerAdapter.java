package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpAddressDeleteService;
import com.ysxsoft.apaycoin.modle.AddressDeleteBean;
import com.ysxsoft.apaycoin.modle.AddressManagerBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.view.NewAddAddressActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 地址管理的列表适配器
 * 日期： 2018/11/15 0015 13:49
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class AddressManagerAdapter extends ListBaseAdapter<AddressManagerBean.DataBean> {
    private int positi = -1;
    private OnDeleteClickListener onDeleteClickListener;

    public AddressManagerAdapter(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.address_manager_item_layout;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        final AddressManagerBean.DataBean dataBean = mDataList.get(position);
        TextView ed_consignee_people = holder.getView(R.id.ed_consignee_people);
        final TextView ed_contact_way = holder.getView(R.id.ed_contact_way);
        TextView ed_detail_address = holder.getView(R.id.ed_detail_address);
        final LinearLayout ll_editor = holder.getView(R.id.ll_editor);
        LinearLayout ll_delete = holder.getView(R.id.ll_delete);
        TextView tv_normal = holder.getView(R.id.tv_normal);
        final CheckBox cb_box = holder.getView(R.id.cb_box);
        ed_detail_address.setText(dataBean.getAddress());
        ed_consignee_people.setText(dataBean.getLinkname());
        ed_contact_way.setText(dataBean.getPhone());
        if ("1".equals(dataBean.getIs_ture())) {//1  默认  0是非默认
            cb_box.setChecked(true);
        }else {
            cb_box.setChecked(false);
        }

//        cb_box.setChecked(positi == position ? true : false);
        ll_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewAddAddressActivity.class);
                intent.putExtra("editor", "editor");
                intent.putExtra("aid", dataBean.getAid());
                intent.putExtra("is_true", dataBean.getIs_ture());
                intent.putExtra("name",dataBean.getLinkname());
                intent.putExtra("phone",dataBean.getPhone());
                intent.putExtra("provice",dataBean.getProvice());
                intent.putExtra("city",dataBean.getCity());
                intent.putExtra("area",dataBean.getArea());
                intent.putExtra("adress",dataBean.getAddress());
                mContext.startActivity(intent);
            }
        });
        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteData(dataBean.getAid());
                if (onDeleteClickListener!=null){
                    onDeleteClickListener.deleteItem(position);
                }
            }
        });
    }

    public void setSelect(int position) {
        this.positi = position;
        notifyDataSetChanged();
    }

    //删除
    private void deleteData(String aid) {
        NetWork.getRetrofit()
                .create(ImpAddressDeleteService.class)
                .getCall(NetWork.getToken(), aid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressDeleteBean>() {
                    private AddressDeleteBean addressDeleteBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext, addressDeleteBean.getMsg(), Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        if ("2".equals(addressDeleteBean.getCode())){
                            SharedPreferences.Editor sp = mContext.getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = mContext.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            mContext.startActivity(new Intent(mContext,LoginActivity.class));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddressDeleteBean addressDeleteBean) {
                        this.addressDeleteBean = addressDeleteBean;
                    }
                });

    }

    public interface OnDeleteClickListener{
        void deleteItem(int position);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
