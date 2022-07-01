package com.example.accountbook.activity_adater;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.UpdateRecordActivity;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.utils.MainBudgetDialog;

import java.util.Calendar;
import java.util.List;

/****
 *  第一次修改：recyclerview添加头部
 *  第二次修改：移除头部，直接将头部布局放在activity中，长按事件需要定义接口
 *
 * */

public class MainAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    List<AccountBean> accountBeans;
    private static final int TYPE_HEADER = 1;
    private int headViewCount = 1;
    int year,month,day;
    public  boolean isShow = true;
    private OnItemLongClickListener onItemLongClickListener;


    /***
     *  这里用sharedPreferences存储预算数据：
     *  由于其配置信息并不多，如果采用数据库来存放并不划算，因为数据库连接跟操作等耗时大大影响了程序的效率，
     * **/
    //public static SharedPreferences preferences;
    // 长按监听接口
    public interface OnItemLongClickListener{
        boolean onLongClick(View view, AccountBean delBean, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }



    public MainAccountAdapter(Context context, List<AccountBean> accountBeans,boolean isShow) {
        this.context = context;
        this.accountBeans = accountBeans;
        this.isShow = isShow;

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     /*   if (viewType==TYPE_HEADER){
            View view = LayoutInflater.from(context).inflate(R.layout.item_mainlv_top,parent,false);
            return new HeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_mainlv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }*/
        View view = LayoutInflater.from(context).inflate(R.layout.item_mainlv,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

       /* holder.itemView.setTag(position);
        // headerView
        if (holder instanceof HeaderViewHolder) {
            // 获取今日支出和收入
            Double outcomeDay = DBManager.getAllMoneyOneDayByKind(year,month,day,0);
            Double incomeDay = DBManager.getAllMoneyOneDayByKind(year,month,day,1);
            String oneDayMoney = "今日支出：￥ "+outcomeDay +" 收入：￥ "+incomeDay;
            Log.e("TAG", "今日支出: "+outcomeDay );
            ((HeaderViewHolder) holder).topConTv.setText(oneDayMoney);

            // 获取本月支出和收入
            Double outcomeMonth = DBManager.getAllMoneyOneMonthByKind(year,month,0);
            Double incomeMonth = DBManager.getAllMoneyOneMonthByKind(year,month,1);
            ((HeaderViewHolder) holder).topOutTv.setText("￥ "+outcomeMonth);
            ((HeaderViewHolder) holder).topInTv.setText("￥ "+incomeMonth);
            ((HeaderViewHolder) holder).topShowOrHideIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HeaderViewHolder) holder).showMoney();
                }
            });

            // 设置显示预算
            ((HeaderViewHolder) holder).topBudgetTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 显示对话框
                    ((HeaderViewHolder) holder).showBudgetDialog();
                }
            });
        }
*/
        // itemList
/*        if (holder instanceof ViewHolder){
            *//**这里需要注意，因为recyclerview添加了头布局，所以position=0的位置应该为头布局的位置
             * 数据元accountBeans正真的位置应该是position-1
             * **//*
        }*/

        AccountBean bean = accountBeans.get(position);
        ((ViewHolder) holder).typeIv.setImageResource(bean.getCheckedImgId());
        ((ViewHolder) holder).typeTv.setText(bean.getTypename());
        ((ViewHolder) holder).descTv.setText(bean.getDesc());
        if (bean.getYear()==year&&bean.getMonth()==month&&bean.getDay()==day) {
            String time = bean.getTime().split(" ")[1];
            ((ViewHolder) holder).timeTv.setText("今天 "+time);
        }else {
            ((ViewHolder) holder).timeTv.setText(bean.getTime());
        }

        ((ViewHolder) holder).moneyTv.setText("￥ "+bean.getMoney());

        ((ViewHolder) holder).isShow(isShow);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AccountBean.ACCOUNT_BEAN_CODE,bean);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        if (onItemLongClickListener!=null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onItemLongClickListener.onLongClick(holder.itemView,bean,position);
                }
            });
        }

/*        // 设置长按监听
        ((ViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((ViewHolder) holder).showDeleteDialog(bean);
                notifyDataSetChanged();
                MainActivity mainActivity = (MainActivity) context;
                ((MainActivity) context).showHeader();
                return false;
            }
        });*/


    }



    @Override
    public int getItemCount() {
        // 如果添加头部，itemCount再加1
        /*return accountBeans.size() + headViewCount;*/
        return accountBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


/*    @Override
    public int getItemViewType(int position) {
        //在第一个位置添加头
        if (position==0){
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }*/


    // list-item
    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView typeIv;
        TextView typeTv,descTv,timeTv,moneyTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeIv = itemView.findViewById(R.id.item_mainlv_iv);
            typeTv = itemView.findViewById(R.id.item_mainlv_tv_title);
            descTv = itemView.findViewById(R.id.item_mainlv_tv_desc);
            timeTv = itemView.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = itemView.findViewById(R.id.item_mainlv_tv_money);
        }

        public void isShow(Boolean isShow){
            if (isShow) {
                PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
                moneyTv.setTransformationMethod(passwordMethod);
                isShow = false;
            }else {
                HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
                moneyTv.setTransformationMethod(hideMethod);
                isShow = true;
            }
        }

        public void showDeleteDialog(final AccountBean delBean){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示信息").setMessage("确定要删除这条记录吗？")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 执行从数据库中删除记录
                            DBManager.deleteItemRecordById(delBean.getaId());
                            accountBeans.remove(delBean);
                            //notifyDataSetChanged();
                        }
                    });

            // 显示对话框
            builder.create().show();
        }
    }

    // header部
    class HeaderViewHolder extends RecyclerView.ViewHolder{
        ImageView topShowOrHideIv;  // 显示或隐藏按钮
        TextView topOutTv,topInTv,topBudgetTv,topConTv;


        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            topShowOrHideIv = itemView.findViewById(R.id.item_mainlv_top_iv_hide);
            topOutTv = itemView.findViewById(R.id.item_mainlv_top_tv_out);
            topInTv = itemView.findViewById(R.id.item_mainlv_top_tv_in);
            topBudgetTv = itemView.findViewById(R.id.item_mainlv_top_tv_budget);
            topConTv = itemView.findViewById(R.id.item_mainlv_top_tv_day_out);
        }

        /*
         *   显示或隐藏money
         * */
        public void showMoney(){
            if (isShow) {
                PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
                topOutTv.setTransformationMethod(passwordMethod);
                topInTv.setTransformationMethod(passwordMethod);
                topBudgetTv.setTransformationMethod(passwordMethod);
                topShowOrHideIv.setImageResource(R.mipmap.ih_hide);
                isShow = false;
            }else {
                HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
                topOutTv.setTransformationMethod(hideMethod);
                topInTv.setTransformationMethod(hideMethod);
                topBudgetTv.setTransformationMethod(hideMethod);
                topShowOrHideIv.setImageResource(R.mipmap.ih_show);
                isShow = true;
            }
        }

        /*
        *   显示预算设置对话框
        * */
       /* public void showBudgetDialog(){
            MainBudgetDialog budgetDialog = new MainBudgetDialog(context);
            budgetDialog.show();
            budgetDialog.setViewHeight();

            budgetDialog.setOnEnsureListener(new MainBudgetDialog.OnEnsureListener() {
                @Override
                public void onEnsure(Double budget) {
                    // 写入预算金额
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("bMoney",Double.doubleToRawLongBits(budget));
                    editor.commit();

                    // 计算本月剩余金额
                    double outcomeMonth = DBManager.getAllMoneyOneMonthByKind(year, month, 0);
                    Double remainMonth = budget - outcomeMonth;
                    topBudgetTv.setText("￥ "+remainMonth);
                }
            });
        }*/

    }


    // 删除对应item
    public void removeData(int position) {
        accountBeans.remove(position);
        notifyItemRemoved(position);
    }

    public void setIsShow(boolean isShow) {
       this.isShow = isShow;
    }


}
