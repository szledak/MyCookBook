package com.example.MyCookBook.database;

import android.content.Context;
import android.database.Cursor;
import com.example.MyCookBook.ingredient.IngredientUnit;

import java.util.ArrayList;

/**
 * Created by Sonia on 2014-12-08.
 */
public class IngredientUnitDAO extends DatabaseDAO {

    private Context mContext;

    public IngredientUnitDAO(Context context) {
        super(context);
    }

    public ArrayList<IngredientUnit> getIngredientUnits() {

        ArrayList<IngredientUnit> ingredientUnits = new ArrayList<IngredientUnit>();

        Cursor cursor = database.query("JEDNOSTKI",
                new String[] {"_ID","Jednostka" }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {
            IngredientUnit ingredientUnit = new IngredientUnit();

            ingredientUnit.setId(cursor.getInt(0));
            ingredientUnit.setName(cursor.getString(1));

            ingredientUnits.add(ingredientUnit);
        }

        return ingredientUnits;
    }
}
