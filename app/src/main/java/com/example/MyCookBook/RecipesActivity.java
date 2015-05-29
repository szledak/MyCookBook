package com.example.MyCookBook;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import com.example.MyCookBook.adapter.RecipesTabsPagerAdapter;
import com.example.MyCookBook.recipes.*;
import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2014-11-03.
 */
public class RecipesActivity extends FragmentActivity implements ActionBar.TabListener {

    final int[] ICONS = new int[] {
            R.drawable.ic_main_course,
            R.drawable.ic_soup,
            R.drawable.ic_dessert,
            R.drawable.ic_salad,
            R.drawable.ic_starter,
    };

    private ViewPager viewPager;
    private RecipesTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Fragment contentFragment;

    private FRecipe fRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_products);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable( getResources().getColor(R.color.action_bar_color)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.act_bar_tab_color)));

        mAdapter = new RecipesTabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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

        return super.onCreateOptionsMenu(menu);
    }

    private void addRecipeOnClick() {
        startActivity(new Intent("com.example.MyCookBook.AddRecipeActivity"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                addRecipeOnClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setFragmentTitle(int resourseId) {
        setTitle(resourseId);
        getActionBar().setTitle(resourseId);
    }

    //@Override
    public void onFinishDialog() {
        if(fRecipe!= null)
            fRecipe.updateView();
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
