package com.example.accountbook.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.db.entity.RecordTypeBean;
import com.example.accountbook.frag_record.adapter.RecordTypeAdapter;

import java.util.List;


public class IncomeFragment extends BaseRecordFragment{
    @Override
    public void initDataToGv() {
        super.initDataToGv();
        // 加载数据
        List<RecordTypeBean> inList = DBManager.getTypeList(1);
        super.getTypeList().addAll(inList);
        super.getTypeAdapter().notifyDataSetChanged();

        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveRecordToDB() {
        super.getAccountBean().setKind(1);
        if (DBManager.getAIdFromAccounttb(super.getAccountBean().getaId())==-1) {
            DBManager.insertRecordToAccounttb(super.getAccountBean());
        }else {
            DBManager.updateRecordToAccounttb(super.getAccountBean());
        }

    }
}