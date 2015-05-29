package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import com.example.MyCookBook.database.DatabaseHelper;
import info.androidhive.actionbar.R;

import java.io.IOException;

public class MyActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */
    private ActionBar actionBar;
    private ImageView viewRecipes;
    private ImageView viewProducts;
    private ImageView viewCalculator;
    private ImageView viewFavourites;
    private ImageView viewSearchRecipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable( getResources().getColor(R.color.action_bar_color)));

        viewRecipes = (ImageView)findViewById(R.id.imageView1);
        viewFavourites = (ImageView)findViewById(R.id.imageView2);
        viewCalculator = (ImageView)findViewById(R.id.imageView3);
        viewSearchRecipes = (ImageView)findViewById(R.id.imageView4);
        viewProducts = (ImageView)findViewById(R.id.imageView5);

        viewRecipes.setOnClickListener(this);
        viewFavourites.setOnClickListener(this);
        viewCalculator.setOnClickListener(this);
        viewSearchRecipes.setOnClickListener(this);
        viewProducts.setOnClickListener(this);

        // create db
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
//                // search action
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    private void viewRecipesOnClick(){
        startActivity(new Intent("com.example.MyCookBook.RecipesActivity"));
    }
    private void viewProductsOnClick(){
        startActivity(new Intent("com.example.MyCookBook.ProductsActivity"));
    }
    private void viewCalculatorOnClick(){
        startActivity(new Intent("com.example.MyCookBook.CalculatorActivity"));
    }
    private void viewFavouritesOnClick(){
        startActivity(new Intent("com.example.MyCookBook.FavouritesActivity"));
    }
    private void viewSearchRecipeOnClick(){
        startActivity(new Intent("com.example.MyCookBook.SearchRecipeActivity"));
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.imageView1:
                viewRecipesOnClick();
                break;
            case R.id.imageView2:
                viewFavouritesOnClick();
                break;
            case R.id.imageView3:
                viewCalculatorOnClick();
                break;
            case R.id.imageView4:
                viewSearchRecipeOnClick();
                break;
            case R.id.imageView5:
                viewProductsOnClick();
                break;
        }
    }
}
