package com.example.accountbook.db.entity;

/*
*
*   支出或收入类型
* * */
public class RecordTypeBean {
    private int tId;
    private int imgId;
    private int checkedImgId; // 被选中图片id
    private String typeName;
    private int kind; //支出或收入类型

    public RecordTypeBean(int tId, int imgId, int checkedImgId, String typeName, int kind) {
        this.tId = tId;
        this.imgId = imgId;
        this.checkedImgId = checkedImgId;
        this.typeName = typeName;
        this.kind = kind;
    }

    public RecordTypeBean() {
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getCheckedImgId() {
        return checkedImgId;
    }

    public void setCheckedImgId(int checkedImgId) {
        this.checkedImgId = checkedImgId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "RecordTypeBean{" +
                "tId=" + tId +
                ", imgId=" + imgId +
                ", checkedImgId=" + checkedImgId +
                ", typeName='" + typeName + '\'' +
                ", kind=" + kind +
                '}';
    }
}
