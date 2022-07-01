package com.example.accountbook.frag_chart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.BarChartItemBean;
import com.example.accountbook.db.entity.RecordTypeBean;
import com.example.accountbook.db.entity.TypeAccountBean;
import com.example.accountbook.utils.DoubleUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class ChartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TypeAccountBean> mDatas;
    private int kind = 1;
    private static final int TYPE_HEADER = 1;
    private HeaderViewHolder headerViewHolder;
    private int year;
    private int month;
    public ChartItemAdapter(Context context, List<TypeAccountBean> mDatas,int year,int month) {
        this.context = context;
        this.mDatas = mDatas;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chart_frag_top,parent,false);
            headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chart_frag_rv, parent,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // 头布局：图表
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            PieChart mPieChart = headerViewHolder.mPieChart;
           // headerViewHolder.showBarchart(year,month,kind);
            // 如果数据为0,隐藏图表
            if (mDatas.size()==0) {
                mPieChart.setVisibility(View.GONE);
                headerViewHolder.chartTv.setVisibility(View.VISIBLE);
            }else {
                mPieChart.setVisibility(View.VISIBLE);
                headerViewHolder.chartTv.setVisibility(View.GONE);
                // 绘制
                headerViewHolder.showPieChart();
            }

        // 列表布局 注意position要-1
        }else {
            ViewHolder viewHolder = (ViewHolder) holder;
            TypeAccountBean bean = mDatas.get(position-1);
            viewHolder.typeIv.setImageResource(bean.getCheckedImgId());
            viewHolder.typeTv.setText(bean.getTypename());
            viewHolder.totalTv.setText("￥ "+bean.getTotalMoney());
            double ratio = bean.getRatio();
            String percent = DoubleUtils.ratioToPercent(ratio);
            viewHolder.ratioTv.setText(percent);
        }

    }




    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeTv, ratioTv,totalTv;
        ImageView typeIv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeIv = itemView.findViewById(R.id.item_chart_frag_iv);
            typeTv = itemView.findViewById(R.id.item_chart_frag_tv_type);
            ratioTv = itemView.findViewById(R.id.item_chart_frag_tv_pert);
            totalTv = itemView.findViewById(R.id.item_chart_frag_tv_total);

        }


    }

    // 头布局
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        //BarChart barChart; // 柱状图控件
        PieChart mPieChart; // 饼状图
        TextView chartTv; // 无记录的提示文本

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  barChart = itemView.findViewById(R.id.item_chart_top_chart);*/
            chartTv = itemView.findViewById(R.id.item_chart_frag_top_tv);
            mPieChart = itemView.findViewById(R.id.item_chart_top_pieChart);
        }

        public void showPieChart() {
          //  PieChart mPieChart = headerViewHolder.mPieChart;
            /*折线饼状图*/
            //1.初始化组件


            mPieChart.setUsePercentValues(true); //设置是否使用百分值,默认不显示
            mPieChart.getDescription().setEnabled(false);
            mPieChart.setDragDecelerationFrictionCoef(0.95f);

            //是否设置中心文字
            mPieChart.setDrawCenterText(true);
            //绘制中间文字
            SpannableString sp = new SpannableString("本月收支统计");
            mPieChart.setCenterText(sp);
            mPieChart.setCenterTextSize(20);
            mPieChart.setExtraOffsets(30.f, 0.f, 30.f, 0.f);

            //设置是否为实心图，以及空心时 中间的颜色
            mPieChart.setDrawHoleEnabled(true);
            mPieChart.setHoleColor(Color.WHITE);


            //设置圆环信息
            mPieChart.setTransparentCircleColor(Color.WHITE);//设置透明环颜色
            mPieChart.setTransparentCircleAlpha(110);//设置透明环的透明度
            mPieChart.setHoleRadius(45f);//设置内圆的大小
            mPieChart.setTransparentCircleRadius(50f);//设置透明环的大小

            mPieChart.setRotationAngle(0);
            // 触摸旋转
            mPieChart.setRotationEnabled(true);
            //选中变大
            mPieChart.setHighlightPerTapEnabled(true);

            //转换数据
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            for (int i = 0; i < mDatas.size(); i++) {
                PieEntry pieEntry = new PieEntry((float) mDatas.get(i).getTotalMoney(),mDatas.get(i).getTypename());
                entries.add(pieEntry);
            }

            //设置数据
            headerViewHolder.setData(entries);

            //默认动画
            mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            //设置图例
            Legend l = mPieChart.getLegend();
            //设置显示的位置，低版本用的是setPosition();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            //设置是否显示图例
            l.setDrawInside(false);
            l.setEnabled(true);

            // 输入图例标签样式
            mPieChart.setEntryLabelColor(Color.BLACK);
            mPieChart.setEntryLabelTextSize(16f);

        }


        public void setData(ArrayList<PieEntry> entries) {
            PieDataSet dataSet = new PieDataSet(entries, "");
            // 设置个饼状图之间的距离
            dataSet.setSliceSpace(0f);

            // 该类封装有 预定义的颜色整数数组
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);

            //设置折线
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            //设置线的长度
            dataSet.setValueLinePart1Length(0.3f);
            dataSet.setValueLinePart2Length(0.3f);

            //设置文字和数据图外显示
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            //百分比设置
            PercentFormatter percentFormatter = new PercentFormatter();
            percentFormatter.getDecimalDigits();
            data.setValueFormatter(new PercentFormatter());
            //文字的颜色
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.BLACK);
            mPieChart.setData(data);
            // 撤销所有的亮点
            mPieChart.highlightValues(null);
            // 重绘
            mPieChart.invalidate();

        }
    }

    @Override
    public int getItemViewType(int position) {
        //在第一个位置添加头
        if (position==0){
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    public void setKind(int kind) {
        this.kind = kind;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
