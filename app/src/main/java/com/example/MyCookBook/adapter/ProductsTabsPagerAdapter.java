package com.example.MyCookBook.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.MyCookBook.products.FProduct;
import com.example.MyCookBook.database.Queries;

/**
 * Created by Sonia on 2014-11-12.
 */
public class ProductsTabsPagerAdapter extends FragmentPagerAdapter {
    public ProductsTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private Queries queries = new Queries() ;

    public Fragment getFragment(String query){
        FProduct fProduct = new FProduct();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        fProduct.setArguments(bundle);
        return fProduct;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return getFragment(queries.getVegetable());
            case 1:
                return getFragment(queries.getFruit());
            case 2:
                return getFragment(queries.getCereal());
            case 3:
                return getFragment(queries.getDairy());
            case 4:
                return getFragment(queries.getMeat());
            case 5:
                return getFragment(queries.getSweets());
            case 6:
                return getFragment(queries.getOthers());
        }
            return null;
    }


    @Override
    public int getCount() {
        return 7;
    }
}
