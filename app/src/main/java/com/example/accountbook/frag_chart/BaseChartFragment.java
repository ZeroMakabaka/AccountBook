package com.example.accountbook.frag_chart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.TypeAccountBean;
import com.example.accountbook.frag_chart.adapter.ChartItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseChartFragment extends Fragment {
    RecyclerView chartRv;
    ChartItemAdapter itemAdapter;
    private int year,month;
    private List<TypeAccountBean> mDatas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatas = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartRv = view.findViewById(R.id.chart_frag_rv);
        initView();
        return view;
    }

    public void initView() {

        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");


        // 设置适配器
        itemAdapter = new ChartItemAdapter(getContext(),mDatas,year,month);
        chartRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        chartRv.setAdapter(itemAdapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    public void loadData(int year, int month,int kind) {
        List<TypeAccountBean> list = DBManager.getListByOneTypeYearMonth(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

   public void setDate(int year, int month) {
        this.year = year;
        this.month = month;

   };

    public ChartItemAdapter getItemAdapter() {
        return itemAdapter;
    }




}
