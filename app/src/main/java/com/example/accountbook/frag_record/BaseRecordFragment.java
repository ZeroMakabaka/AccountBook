package com.example.accountbook.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.db.entity.RecordTypeBean;
import com.example.accountbook.frag_record.adapter.RecordTypeAdapter;
import com.example.accountbook.utils.Description;
import com.example.accountbook.utils.KeyBoardUtils;
import com.example.accountbook.utils.SelectTimeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
*   记录页面：支出模块
*
* */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{
    private List<RecordTypeBean> typeList;
    private AccountBean accountBean; // 记录数据
    private RecordTypeAdapter typeAdapter;

    KeyboardView keyboardView; // 键盘
    EditText editMoney;
    ImageView typeIv;
    TextView typeTv,descTv,timeTv;
    GridView typesGv;
    private int year,month,day;
    private int checked = -1;




    public BaseRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        initDataToGv();
       // updateRecords();
        Bundle bundle = getArguments();
        if (bundle!=null) {
            updateRecords();
        }


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAccountBean();
    }

    /* 初始化view */
    public void initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        editMoney = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        descTv = view.findViewById(R.id.frag_record_tv_desc);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typesGv = view.findViewById(R.id.frag_record_gv);

        // 初始化时间
        setTime();

        // 显示自定义的软键盘
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,editMoney);
        boardUtils.showKeyboard();
        // 备注
        descTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        // 设置键盘监听
        boardUtils.setOnEnterListener(new KeyBoardUtils.OnEnterListener() {
            @Override
            public void onEnter() {
                // 点击按键
                String moneyStr = editMoney.getText().toString();

                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")||moneyStr.equals("0.00")) {
                    getActivity().finish();
                    return;
                }
                double money = Double.parseDouble(moneyStr);
                accountBean.setMoney(money);

                // 保存记录信息到数据库中
                saveRecordToDB();

                // 返回上一级页面
                getActivity().finish();
            }
        });

        // 设置GridView监听
        setGvItemListener();

    }

   /*  加载记录类型数据   */
    public void initDataToGv(){
        typeList = new ArrayList<>();
        // 加载布局
        typeAdapter = new RecordTypeAdapter(getContext(), typeList);
        typesGv.setAdapter(typeAdapter);

    }

    /* GridView控件监听*/
   public void setGvItemListener(){
        typesGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                typeAdapter.setChecked(i);

                typeAdapter.notifyDataSetInvalidated();
                RecordTypeBean typeBean = typeList.get(i);
                // 类型
                String typename = typeBean.getTypeName();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int checked = typeBean.getCheckedImgId();
                typeIv.setImageResource(checked);
                accountBean.setCheckedImgId(checked);
            }
        });
    }

    /*初始化记录数据*/
    public void initAccountBean(){
        accountBean = new AccountBean();
        accountBean.setTypename("其他");
        accountBean.setCheckedImgId(R.mipmap.ic_qita_fs);
    }

    /*初始化时间*/
    public void setTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setTime(time);

        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    /*保存记录数据*/
   public abstract void saveRecordToDB();

    /** 修改，根据有无bundle对象来判断是修改还是添加功能 **/
    public void updateRecords() {
        Bundle bundle = getArguments();
        if (bundle!=null) {
            AccountBean bean = (AccountBean) bundle.get(AccountBean.ACCOUNT_BEAN_CODE);
            accountBean = bean;
            editMoney.setText(bean.getMoney()+"");
            typeTv.setText(bean.getTypename());
            if (!TextUtils.isEmpty(bean.getDesc())) {
                descTv.setText(bean.getDesc());
            }
            timeTv.setText(bean.getTime());
            typeIv.setImageResource(bean.getCheckedImgId());
            checked = DBManager.getTidFromRecordTypeFromTypeTb(bean.getCheckedImgId());
            if (checked!=-1) {
                typeAdapter.setChecked(checked-1);
                typeAdapter.notifyDataSetInvalidated();
            }

        }
    }

    public List<RecordTypeBean> getTypeList() {
        return typeList;
    }

    public RecordTypeAdapter getTypeAdapter() {
        return typeAdapter;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_record_tv_time:
                showTimeView();
                break;
            case R.id.frag_record_tv_desc:
                showDescView();
                break;
        }
    }

    /*弹出备注框*/
    public void showDescView() {
        Description description = new Description(getContext());
        description.show();
        description.setViewHeight();

        description.setOnEnsureListener(new Description.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String desc = description.getDesc();
                if (!TextUtils.isEmpty(desc)) {
                    descTv.setText(desc);
                    accountBean.setDesc(desc);
                }
                description.cancel();

            }
        });
    }

    /* 弹出时间框 */
    private void showTimeView(){
        SelectTimeView timeView = new SelectTimeView(getContext());
        timeView.show();
        timeView.setOnEnsureListener(new SelectTimeView.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }


}