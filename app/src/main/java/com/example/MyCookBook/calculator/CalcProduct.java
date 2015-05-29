package com.example.MyCookBook.calculator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-12-08.
 */
public class CalcProduct implements Parcelable {

    private int id;
    private String name;
    private int amount;

    public CalcProduct(){
        super();
    }
    private CalcProduct(Parcel in) {
        super();
        String[] data= new String[3];

        in.readStringArray(data);
        this.id= Integer.parseInt(data[0]);
        this.name= data[1];
        this.amount= Integer.parseInt(data[2]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {

        return amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CalcProduct other = (CalcProduct) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[]{ String.valueOf(this.id), this.name, String.valueOf(this.amount)});
    }

    public static final Parcelable.Creator<CalcProduct> CREATOR = new Parcelable.Creator<CalcProduct>() {
        public CalcProduct createFromParcel(Parcel in) {
            return new CalcProduct(in);
        }

        @Override
        public CalcProduct[] newArray(int i) {
            return new CalcProduct[i];
        }
    };
}
