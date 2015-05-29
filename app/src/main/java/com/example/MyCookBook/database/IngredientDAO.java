package com.example.MyCookBook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.ingredient.IngredientUnit;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sonia on 2014-12-07.
 */
public class IngredientDAO extends DatabaseDAO {

    public IngredientDAO(Context context) {
        super(context);
    }
    private Context mContext;


    public long save(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put("IDProduktu", ingredient.getIdProduct());
        values.put("IDPrzepisu", ingredient.getIdRecipe());
        values.put("Ilosc", ingredient.getAmount());
        values.put("Jednostka", ingredient.getIngredientUnit().getName());

        return database.insert("SKLADNIKI", null, values);
    }

    public long update(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put("IDProduktu", ingredient.getIdProduct());
        values.put("IDPrzepisu", ingredient.getIdRecipe());
        values.put("Ilosc", ingredient.getAmount());
        values.put("Jednostka", ingredient.getIngredientUnit().getName());

        long result = database.update("SKLADNIKI", values,
                "_ID =?",
                new String[] { String.valueOf(ingredient.getId())});
        Log.d("Update Result:", "=" + result);

        return result;
    }

    public int deleteIngredient(Ingredient ingredient) {
        return database.delete("SKLADNIKI", "_ID =?",
                new String[] { ingredient.getId() + "" });
    }

    public ArrayList<Ingredient> getIngredients(String mString) {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

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
            Ingredient ingredient = new Ingredient();
            ingredient.setId(cursor.getInt(0));
            ingredient.setIdProduct(cursor.getInt(1));

            ingredient.setIdRecipe(cursor.getInt(2));
            ingredient.setAmount(cursor.getInt(3));
            ingredient.setUnit(cursor.getString(4));

            IngredientUnit ingredientUnit = new IngredientUnit();
            ingredientUnit.setId(cursor.getInt(5));
            ingredientUnit.setName(cursor.getString(6));

            ingredient.setIngredientUnit(ingredientUnit);

            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public int countIngredients(String mString){

        Cursor cursor = database.rawQuery(mString, null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        return count;
    }
}
