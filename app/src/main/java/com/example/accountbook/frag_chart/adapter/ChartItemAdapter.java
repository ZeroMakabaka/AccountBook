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

        // ??????????????????
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            PieChart mPieChart = headerViewHolder.mPieChart;
           // headerViewHolder.showBarchart(year,month,kind);
            // ???????????????0,????????????
            if (mDatas.size()==0) {
                mPieChart.setVisibility(View.GONE);
                headerViewHolder.chartTv.setVisibility(View.VISIBLE);
            }else {
                mPieChart.setVisibility(View.VISIBLE);
                headerViewHolder.chartTv.setVisibility(View.GONE);
                // ??????
                headerViewHolder.showPieChart();
            }

        // ???????????? ??????position???-1
        }else {
            ViewHolder viewHolder = (ViewHolder) holder;
            TypeAccountBean bean = mDatas.get(position-1);
            viewHolder.typeIv.setImageResource(bean.getCheckedImgId());
            viewHolder.typeTv.setText(bean.getTypename());
            viewHolder.totalTv.setText("??? "+bean.getTotalMoney());
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

    // ?????????
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        //BarChart barChart; // ???????????????
        PieChart mPieChart; // ?????????
        TextView chartTv; // ????????????????????????

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  barChart = itemView.findViewById(R.id.item_chart_top_chart);*/
            chartTv = itemView.findViewById(R.id.item_chart_frag_top_tv);
            mPieChart = itemView.findViewById(R.id.item_chart_top_pieChart);
        }

        public void showPieChart() {
          //  PieChart mPieChart = headerViewHolder.mPieChart;
            /*???????????????*/
            //1.???????????????


            mPieChart.setUsePercentValues(true); //???????????????????????????,???????????????
            mPieChart.getDescription().setEnabled(false);
            mPieChart.setDragDecelerationFrictionCoef(0.95f);

            //????????????????????????
            mPieChart.setDrawCenterText(true);
            //??????????????????
            SpannableString sp = new SpannableString("??????????????????");
            mPieChart.setCenterText(sp);
            mPieChart.setCenterTextSize(20);
            mPieChart.setExtraOffsets(30.f, 0.f, 30.f, 0.f);

            //?????????????????????????????????????????? ???????????????
            mPieChart.setDrawHoleEnabled(true);
            mPieChart.setHoleColor(Color.WHITE);


            //??????????????????
            mPieChart.setTransparentCircleColor(Color.WHITE);//?????????????????????
            mPieChart.setTransparentCircleAlpha(110);//???????????????????????????
            mPieChart.setHoleRadius(45f);//?????????????????????
            mPieChart.setTransparentCircleRadius(50f);//????????????????????????

            mPieChart.setRotationAngle(0);
            // ????????????
            mPieChart.setRotationEnabled(true);
            //????????????
            mPieChart.setHighlightPerTapEnabled(true);

            //????????????
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            for (int i = 0; i < mDatas.size(); i++) {
                PieEntry pieEntry = new PieEntry((float) mDatas.get(i).getTotalMoney(),mDatas.get(i).getTypename());
                entries.add(pieEntry);
            }

            //????????????
            headerViewHolder.setData(entries);

            //????????????
            mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            //????????????
            Legend l = mPieChart.getLegend();
            //??????????????????????????????????????????setPosition();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            //????????????????????????
            l.setDrawInside(false);
            l.setEnabled(true);

            // ????????????????????????
            mPieChart.setEntryLabelColor(Color.BLACK);
            mPieChart.setEntryLabelTextSize(16f);

        }


        public void setData(ArrayList<PieEntry> entries) {
            PieDataSet dataSet = new PieDataSet(entries, "");
            // ?????????????????????????????????
            dataSet.setSliceSpace(0f);

            // ??????????????? ??????????????????????????????
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

            //????????????
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            //??????????????????
            dataSet.setValueLinePart1Length(0.3f);
            dataSet.setValueLinePart2Length(0.3f);

            //?????????????????????????????????
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            //???????????????
            PercentFormatter percentFormatter = new PercentFormatter();
            percentFormatter.getDecimalDigits();
            data.setValueFormatter(new PercentFormatter());
            //???????????????
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.BLACK);
            mPieChart.setData(data);
            // ?????????????????????
            mPieChart.highlightValues(null);
            // ??????
            mPieChart.invalidate();

        }
    }

    @Override
    public int getItemViewType(int position) {
        //???????????????????????????
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
