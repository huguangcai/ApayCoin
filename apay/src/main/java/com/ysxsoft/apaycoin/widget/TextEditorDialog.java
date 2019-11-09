package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;

public class TextEditorDialog extends ABSDialog {
    public TextEditorDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
       final EditText ed_input_content= getViewById(R.id.ed_input_content);
       final LinearLayout ll_max_num= getViewById(R.id.ll_max_num);
       final TextView tv_num= getViewById(R.id.tv_num);
        ed_input_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(ed_input_content.getText().toString().trim())){
                    tv_num.setText(String.valueOf(s.length()));
                    if (150 == Integer.valueOf(s.length())) {
                        ll_max_num.setVisibility(View.GONE);
                    } else {
                        ll_max_num.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.text_editor_dialog_layout;
    }
}
