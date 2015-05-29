package com.example.MyCookBook.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.products.FProduct;
import com.example.MyCookBook.recipes.*;


/**
 * Created by Sonia on 2014-10-08.
 */
public class RecipesTabsPagerAdapter extends FragmentPagerAdapter {
    public RecipesTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private Queries queries = new Queries() ;

    public Fragment getFragment(String query){
        FRecipe fRecipe = new FRecipe();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fRecipe.setArguments(bundle);
        return fRecipe;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return getFragment(queries.getMainCourse());
            case 1:
                return getFragment(queries.getSoup());
            case 2:
                return getFragment(queries.getDessert());
            case 3:
                return getFragment(queries.getSalad());
            case 4:
                return getFragment(queries.getStarter());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
