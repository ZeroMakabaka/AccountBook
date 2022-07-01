package com.example.accountbook.frag_record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountbook.R;
import com.example.accountbook.db.entity.RecordTypeBean;

import java.util.List;

public class RecordTypeAdapter extends BaseAdapter {
    private Context context;
    private List<RecordTypeBean> typeBeanList;
    private int checked = 0; //是否被选中

    public RecordTypeAdapter(Context context, List<RecordTypeBean> typeBeanList) {
        this.context = context;
        this.typeBeanList = typeBeanList;
    }

    @Override
    public int getCount() {
        return typeBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_record_frag_gv,viewGroup,false);
        ImageView iv = view.findViewById(R.id.item_record_frag_iv);
        TextView tv = view.findViewById(R.id.item_record_frag_tv);

        RecordTypeBean typeBean = typeBeanList.get(i);
        tv.setText(typeBean.getTypeName());
        // 判断是否选中该选项，未选中为灰色，选中为彩色
        if (checked==i){
            iv.setImageResource(typeBean.getCheckedImgId());
        }else {
            iv.setImageResource(typeBean.getImgId());
        }
        return view;
    }


    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
