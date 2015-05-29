package com.example.MyCookBook.products;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-11-17.
 */
public class Product implements Parcelable {


     private ProductType productType;
     private String name;
     private String type;
     private int kcal;
     private int id;

    public Product(){
        super();
    }
    private Product(Parcel in) {
        super();

        String[] data= new String[4];

        in.readStringArray(data);
        this.id= Integer.parseInt(data[0]);
        this.name= data[1];
        this.type= data[2];
        this.kcal= Integer.parseInt(data[3]);
        this.productType = in.readParcelable(ProductType.class.getClassLoader());
    }

    // getter
    public String getType() {
        return type;
    }

    public ProductType getProductType() {
        return productType;
    }

    public int getKcal() {
        return kcal;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // setter


    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
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
        Product other = (Product) obj;
        if (id != other.id)
            return false;
        return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[]{ String.valueOf(this.id), this.name,this.type, String.valueOf(this.kcal)});
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
