package com.example.greeningapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private int pid;
    private String pimg;
    private String pdetailimg;
    private String pname;
    private int pprice;
    private int stock;

    public Product() {
    }

    public Product(int pid, String pimg, String pdetailimg, String pname, int pprice, int stock) {
        this.pid = pid;
        this.pimg = pimg;
        this.pdetailimg = pdetailimg;
        this.pname = pname;
        this.pprice = pprice;
        this.stock = stock;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getPdetailimg() {
        return pdetailimg;
    }

    public void setPdetailimg(String pdetailimg) {
        this.pdetailimg = pdetailimg;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    protected Product(Parcel in) {
        pimg = in.readString();
        pdetailimg = in.readString();
        pname = in.readString();
        pprice = in.readInt();
        stock = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pimg);
        dest.writeString(pdetailimg);
        dest.writeString(pname);
        dest.writeInt(pprice);
        dest.writeInt(stock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}