package com.example.MyCookBook.ingredient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-12-08.
 */
public class IngredientUnit  implements Parcelable {
    int id;
    String name;

    public IngredientUnit(){
        super();
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

    }
}
