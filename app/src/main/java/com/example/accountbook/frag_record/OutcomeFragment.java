package com.example.accountbook.frag_record;

import android.util.Log;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.RecordTypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{
    @Override
    public void initDataToGv() {
        super.initDataToGv();
        // 加载数据
        List<RecordTypeBean> outList = DBManager.getTypeList(0);
        super.getTypeList().addAll(outList);
        super.getTypeAdapter().notifyDataSetChanged();

        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveRecordToDB() {
        super.getAccountBean().setKind(0);
       /* DBManager.insertRecordToAccounttb(super.getAccountBean());*/
        if (DBManager.getAIdFromAccounttb(super.getAccountBean().getaId())==-1) {
            DBManager.insertRecordToAccounttb(super.getAccountBean());
            Log.e("TAG", "saveRecordToDB: "+DBManager.getAccountList(2022,5,21));
        }else {
            Log.e("TAG", "正在修改数据:"+super.getAccountBean().getaId() );
            DBManager.updateRecordToAccounttb(super.getAccountBean());
        }
    }
}