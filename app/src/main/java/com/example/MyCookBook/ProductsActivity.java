package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import com.example.MyCookBook.adapter.ProductsTabsPagerAdapter;
import com.example.MyCookBook.products.ProdAddDialogFragment;
import com.example.MyCookBook.products.ProdListFragment;
import com.example.MyCookBook.products.*;
import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2014-11-12.
 */
public class ProductsActivity extends FragmentActivity implements ActionBar.TabListener {

    // Tab titles
    //private String[] tabs = {"Warzywa", "Owoce", "Produkty zbożowe", "Nabiał","Mięso", "Słodycze", "Inne"};

    final int[] ICONS = new int[] {
            R.drawable.vegetable,
            R.drawable.fruit,
            R.drawable.cereal,
            R.drawable.dairy,
            R.drawable.meat,
            R.drawable.sweets,
            R.drawable.other
    };

    private ViewPager viewPager;
    private ProductsTabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private Fragment contentFragment;
    private ProdListFragment productListFragment;
    private ProdAddDialogFragment productAddFragment;

    private FProduct fProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_products);
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable( getResources().getColor(R.color.action_bar_color)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.act_bar_tab_color)));


        mAdapter = new ProductsTabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

     //   actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (int i = 0; i < ICONS.length; i++) {
            actionBar.addTab(actionBar.newTab().setIcon(getResources().getDrawable(ICONS[i]))
                    .setTabListener(this));
        }

         viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_prod_actions, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                // add new action
                FragmentManager fm = this.getSupportFragmentManager();
                ProdAddDialogFragment dialog = new ProdAddDialogFragment(); // creating new object
                dialog.show(fm, productAddFragment.ARG_ITEM_ID);
                return true;
//            case R.id.action_search:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate());

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);

            if (!(fragment instanceof FProduct)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof ProdAddDialogFragment) {
            outState.putString("content", ProdAddDialogFragment.ARG_ITEM_ID);
        } else {
            outState.putString("content", ProdListFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    protected void setFragmentTitle(int resourseId) {
        setTitle(resourseId);
        getActionBar().setTitle(resourseId);
    }

    //@Override
    public void onFinishDialog() {
        if(fProduct != null) {
            fProduct.updateView();
        }

        Intent refresh = new Intent(this, ProductsActivity.class);
        startActivity(refresh);//Start the same Activity
        finish(); //finish Activity.
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }
}
