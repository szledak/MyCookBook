package com.example.MyCookBook.recipes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonia on 2014-11-17.
 */
public class Recipe implements Parcelable{

    private int id;
    private String name;
    private String recipe;
    private String type;
    private RecipeType recipeType;
    private int favourite;
    private Boolean selected = false;

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getFavourite() {
        return favourite;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public Recipe() { super();}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeStringArray(new String[]{ String.valueOf(this.id), this.name,this.recipe,this.type,String.valueOf(this.favourite),String.valueOf(this.selected)});
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(recipe);
        parcel.writeString(type);
        parcel.writeInt(favourite);
        //selected
        parcel.writeParcelable(recipeType,i);
    }

    //parcel part
    public Recipe(Parcel in){
//        String[] data= new String[6];
//
//        in.readStringArray(data);
//        this.id= Integer.parseInt(data[0]);
//        this.name= data[1];
//        this.recipe= data[2];
//        this.type = data[3];
//        this.favourite= Integer.parseInt(data[4]);
//        this.selected= Boolean.parseBoolean(data[5]);
//        this.recipeType = in.readParcelable(RecipeType.class.getClassLoader());
        id = in.readInt();
        name = in.readString();
        recipe = in.readString();
        type = in.readString();
        favourite = in.readInt();
        //selected
        recipeType = (RecipeType) in.readParcelable(RecipeType.class.getClassLoader());
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
        Recipe other = (Recipe) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}


