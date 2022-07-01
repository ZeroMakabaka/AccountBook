package com.example.accountbook.db;

import static com.example.accountbook.utils.DoubleUtils.div;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.db.entity.BarChartItemBean;
import com.example.accountbook.db.entity.RecordTypeBean;
import com.example.accountbook.db.entity.TypeAccountBean;
import com.example.accountbook.utils.DoubleUtils;
import com.github.mikephil.charting.charts.BarChart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
*   数据库管理
* */
public class DBManager {
    private static SQLiteDatabase db;

    // 初始化数据库
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
     *   读取数据库的数据，方便后面展示类型
     * */
    public static List<RecordTypeBean> getTypeList(int kind){
        List<RecordTypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind = "+ kind;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int tId = cursor.getInt(cursor.getColumnIndexOrThrow("tId"));
            int imgId = cursor.getInt(cursor.getColumnIndexOrThrow("imgId"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            RecordTypeBean typeBean = new RecordTypeBean(tId, imgId, checkedImgId, typename, kind);
            list.add(typeBean);
        }

        if (!cursor.moveToNext()) {
            cursor.close();
        }
        return list;
    }

    /*根据类型checkedImgId,查找tId*/
    public static int getTidFromRecordTypeFromTypeTb(int checkedImgId){
        int tId = -1;
        String sql = "select tId from typetb where checkedImgId = "+checkedImgId;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            tId = cursor.getInt(cursor.getColumnIndexOrThrow("tId"));
        }
        return tId;
    }

    /*
    *   插入一条记录
    * */
    public static void insertRecordToAccounttb(AccountBean accountBean){
        ContentValues values = new ContentValues();
        values.put("typename",accountBean.getTypename());
        values.put("checkedImgId",accountBean.getCheckedImgId());
        values.put("description",accountBean.getDesc());
        values.put("money",accountBean.getMoney());
        values.put("time",accountBean.getTime());
        values.put("year",accountBean.getYear());
        values.put("month",accountBean.getMonth());
        values.put("day",accountBean.getDay());
        values.put("kind",accountBean.getKind());

        db.insert("accounttb",null,values);
        Log.e("TAG", "insertRecordToAccounttb:"+accountBean);
    }


    /*修改一条记录*/
    public static int updateRecordToAccounttb(AccountBean accountBean) {
        ContentValues values = new ContentValues();
        values.put("typename",accountBean.getTypename());
        values.put("checkedImgId",accountBean.getCheckedImgId());
        values.put("description",accountBean.getDesc());
        values.put("money",accountBean.getMoney());
        values.put("time",accountBean.getTime());
        values.put("year",accountBean.getYear());
        values.put("month",accountBean.getMonth());
        values.put("day",accountBean.getDay());
        values.put("kind",accountBean.getKind());
        String whereClause = "id=?";
        int accounttb = db.update("accounttb", values, whereClause, new String[]{accountBean.getaId() + ""});
        return accounttb;
    }

    /*查询是否存在id相同的记录*/
    public static int getAIdFromAccounttb(int id){
        int aId = -1;
        String sql = "select * from accounttb where id="+id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            aId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
       return aId;
    }
    /*
    *   读取记账某一天的记录
    * */
    public static List<AccountBean> getAccountList(int year,int month,int day){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",day+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            Double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));

            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
     *   读取记账某一天的记录
     * */
    public static List<AccountBean> getAccountListByDayKind(int year,int month,int day,int kind){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? and kind = ? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",day+"",kind+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            Double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
    *   读取一个月的记录
    * */
    public static List<AccountBean> getAccountListByMonth(int year,int month,int kind){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and kind=?  order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
    *   读取一年的记录
    * */

    public static List<AccountBean> getAccountListByYearWithoutKind(int year) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }
    public static List<AccountBean> getAccountListByYear(int year, int kind) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and kind=? order by id desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",kind+""});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
    *   获取一天的支出或收入的总金额， kind：支出=0  收入=1
    * */
    public static double getAllMoneyOneDayByKind(int year, int month, int day,int kind){
        double total = 0.00d;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",day+"",kind+""});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
        }
        cursor.close();
        return total;

    }

    /*
     *   获取一个月的支出或收入的总金额， kind：支出=0  收入=1
     * */
    public static double getAllMoneyOneMonthByKind(int year, int month, int kind){
        double total = 0.0d;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",month+"",kind+""});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
        }
        cursor.close();
        return total;
    }

    /*
     *   获取一年的支出或收入的总金额， kind：支出=0  收入=1
     * */

    public static double getAllMoneyOneYearByKind(int year, int kind){
        double total = 0.00d;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql,new String[]{year+"",kind+""});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
        }
        cursor.close();
        return total;
    }

    /*
    *  根据传入的id删除一条记录
    * */
    public static int deleteItemRecordById(int id){
        int delId =db.delete("accounttb","id=?",new String[]{id+""});
        return delId;
    }

    /*
    *   根据备注或类型名进行搜索
    * */
    public static List<AccountBean> getAccountListByDesc(String description) {
        List<AccountBean> list = new ArrayList<>();
        // 模糊查询
        String sql = "select * from accounttb where description like '%" + description +
                "%'"+"or typename like '%"+description+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id,typename,checkedImgId,desc,money,time,
                    year,month,day,kind);

            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /*
    *   删库跑路
    * */
    public static boolean clearAllRecords() {
        String sql = "delete from accounttb ";
        db.execSQL(sql);
        return true;
    }

    /*
    *   查询某一年某一月某一类型的总钱数
    * */
    public static List<TypeAccountBean> getListByOneTypeYearMonth(int year,int month,int kind){
        List<TypeAccountBean> list = new ArrayList<>();
        double totalMoney = getAllMoneyOneMonthByKind(year, month, kind);
        String sql = "select typename,checkedImgId,sum(money) as total from accounttb where year=? " +
                "and month=? and kind=? group by typename order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int checkedImgId = cursor.getInt(cursor.getColumnIndexOrThrow("checkedImgId"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            // 计算比例
            double ratio = DoubleUtils.div(total,totalMoney);
            TypeAccountBean bean = new TypeAccountBean(checkedImgId, typename, total, ratio);
            list.add(bean);
        }
        cursor.close();
        Log.e("TAG", "getListByOneTypeYearMonth: "+list );
        return list;
    }

    /*
    *   查询一种类型里面的最大金额
    * */
    public static double getMaxMoneyByOneDayOfMonth(int year,int month,int kind) {
        String sql = "select sum(money) from accounttb  where year=? " +
                "and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            double money = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
            return money;
        }
        cursor.close();
        return 0.00;
    }

    // 每一天总钱数集合
    public static List<BarChartItemBean> getSumMoneyOneDayOfMonth(int year,int month,int kind) {
        List<BarChartItemBean> list = new ArrayList<>();
        String sql = "select day, sum(money) from accounttb  where year=?" +
                "and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql,new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            double sumMoney = cursor.getDouble(cursor.getColumnIndexOrThrow("sum(money)"));
            BarChartItemBean bean = new BarChartItemBean(year,month,day,sumMoney);
            list.add(bean);
        }
        cursor.close();
        return list;
    }




}
