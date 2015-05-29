package com.example.MyCookBook.ingredient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-12-07.
 */
public class Ingredient implements Parcelable {

    private IngredientUnit ingredientUnit;
    private int id;
    private int idProduct;
    private int idRecipe;
    private int amount;
    private String unit;

    public Ingredient(){
        super();
    }
    private Ingredient(Parcel in) {
        super();
        String[] data= new String[5];

        in.readStringArray(data);
        this.id= Integer.parseInt(data[0]);
        this.idProduct= Integer.parseInt(data[1]);
        this.idRecipe= Integer.parseInt(data[2]);
        this.amount= Integer.parseInt(data[3]);
        this.unit= data[4];
        this.ingredientUnit = in.readParcelable(IngredientUnit.class.getClassLoader());
    }

    public void setIngredientUnit(IngredientUnit ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    public IngredientUnit getIngredientUnit() {

        return ingredientUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public int getIdProduct() {

        return idProduct;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {

        return amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
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
        Ingredient other = (Ingredient) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[]{ String.valueOf(this.id), String.valueOf(this.idProduct), String.valueOf(this.idRecipe), String.valueOf(this.amount), this.unit});
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }
    };
}
