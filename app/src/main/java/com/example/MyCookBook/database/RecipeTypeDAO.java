package com.example.MyCookBook.database;

import android.content.Context;
import android.database.Cursor;
import com.example.MyCookBook.recipes.RecipeType;

import java.util.ArrayList;

/**
 * Created by Sonia on 2014-11-30.
 */
public class RecipeTypeDAO extends DatabaseDAO {

    private Context mContext;

    public RecipeTypeDAO(Context context) {
        super(context);
    }

    public ArrayList<RecipeType> getRecipeTypes() {

        ArrayList<RecipeType> recipeTypes = new ArrayList<RecipeType>();

        Cursor cursor = database.query("TYP_PRZEPISU",
                new String[] {"_ID","TypPrzepisu" }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {
            RecipeType recipeType = new RecipeType();

            recipeType.setId(cursor.getInt(0));
            recipeType.setName(cursor.getString(1));

            recipeTypes.add(recipeType);
        }

        return recipeTypes;
    }
}
