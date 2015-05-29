package com.example.MyCookBook.recipes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-11-30.
 */
public class RecipeType implements Parcelable{

    private int id;
    private String name;
    public RecipeType() {super();}

    //parcel part
    public RecipeType(Parcel in){
        String[] data= new String[2];

        in.readStringArray(data);
        this.id= Integer.parseInt(data[0]);
        this.name= data[1];
    }

    @Override
    public String toString() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{ String.valueOf(this.id), this.name});
    }
    public static final Parcelable.Creator<RecipeType> CREATOR = new Parcelable.Creator<RecipeType>() {

        public RecipeType createFromParcel(Parcel in) {
            return new RecipeType(in);
        }

        public RecipeType[] newArray(int size) {
            return new RecipeType[size];
        }
    };
}
