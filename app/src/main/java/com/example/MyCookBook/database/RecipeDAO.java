package com.example.MyCookBook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.recipes.RecipeType;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sonia on 2014-11-30.
 */
public class RecipeDAO extends DatabaseDAO {

    public RecipeDAO(Context context) {
        super(context);
    }

    private Context mContext;

    public long save(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", recipe.getName());
        values.put("Tresc", recipe.getRecipe());
        values.put("TypPrzepisu", recipe.getRecipeType().getName());
        values.put("Ulubione",recipe.getFavourite());
        return database.insert("PRZEPIS", null, values);
    }

    public long update(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", recipe.getName());
        values.put("Tresc", recipe.getRecipe());
        values.put("TypPrzepisu", recipe.getRecipeType().getName());
        values.put("Ulubione",recipe.getFavourite());

        long result = database.update("PRZEPIS", values,
                "_ID =?",
                new String[] { String.valueOf(recipe.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int deleteRecipe(Recipe recipe) {
        return database.delete("PRZEPIS", "_ID =?",
                new String[] { recipe.getId() + "" });
    }

    public ArrayList<Recipe> getRecipes(String mString) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        DatabaseHelper  mDbHelper;
        mDbHelper = DatabaseHelper.getHelper(mContext);
        try {
            mDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.d("query", mString);
        Cursor cursor = database.rawQuery(mString, null);

        while (cursor.moveToNext()) {

            Recipe recipe = new Recipe();
            recipe.setId(cursor.getInt(0));
            recipe.setName(cursor.getString(1));
            recipe.setRecipe(cursor.getString(2));
            recipe.setType(cursor.getString(3));
            recipe.setFavourite(cursor.getInt(4));

            // set checkbox
            if(recipe.getFavourite() == 1)
                recipe.setSelected(true) ;

            RecipeType recipeType = new RecipeType();
            recipeType.setId(cursor.getInt(5));
            recipeType.setName(cursor.getString(6));

            recipe.setRecipeType(recipeType);

            recipes.add(recipe);
        }
        return recipes;
    }
    public int countRecipes(String mString){

        Cursor cursor = database.rawQuery(mString, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        return count;
    }
}
