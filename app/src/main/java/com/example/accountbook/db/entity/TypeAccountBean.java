package com.example.accountbook.db.entity;

public class TypeAccountBean {
    private int checkedImgId;
    private String typename;
    private double totalMoney;
    private double ratio; // 所占比例

    public TypeAccountBean() {
    }

    public TypeAccountBean(int checkedImgId, String typename, double totalMoney, double ratio) {
        this.checkedImgId = checkedImgId;
        this.typename = typename;
        this.totalMoney = totalMoney;
        this.ratio = ratio;
    }

    public int getCheckedImgId() {
        return checkedImgId;
    }

    public void setCheckedImgId(int checkedImgId) {
        this.checkedImgId = checkedImgId;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "TypeAccountBean{" +
                "checkedImgId=" + checkedImgId +
                ", typename='" + typename + '\'' +
                ", totalMoney=" + totalMoney +
                ", ratio=" + ratio +
                '}';
    }
}
