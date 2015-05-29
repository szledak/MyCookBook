package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.example.MyCookBook.adapter.IngListAdapter;
import com.example.MyCookBook.database.*;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.recipes.RecipeType;
import info.androidhive.actionbar.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonia on 2014-12-17.
 */
public class AddRecipeActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ActionBar actionBar;

    private Recipe recipe;
    private RecipeDAO recipeDAO;
    private RecipeType recipeType;
    private RecipeTypeDAO recipeTypeDAO;
    private IngredientDAO ingredientDAO;
    private IngListAdapter ingListAdapter;
    private MenuItem newMenuItem;
    private Queries query;

    //containers
    private ArrayAdapter<RecipeType> adapterRecipe;
    private ArrayList<Ingredient> ingredients;

    //ui
    private EditText editText2, editText;
    private AutoCompleteTextView ingName;
    private EditText ingAmount;
    private Spinner spinner;
    private LinearLayout submitLayout;

    //dialog
    private int selectionID;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        this.setContentView(R.layout.activity_add_recipe);

        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.action_bar_color)));

        recipeDAO = new RecipeDAO(this);
        query = new Queries();
        ingredientDAO = new IngredientDAO(this);
        recipe = new Recipe();

        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText2.setEnabled(true);
        spinner = (Spinner)findViewById(R.id.spinner);

        setValue();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    private void setValue(){
        editText.setHint("Podaj nazwę przepisu...");
        editText2.setHint("Podaj treść przepisu...");

        RecipeTypeDAO recipeTypeDAO = new RecipeTypeDAO(this);

        List<RecipeType> types = recipeTypeDAO.getRecipeTypes();
        adapterRecipe = new ArrayAdapter<RecipeType>(this,
                R.layout.my_spinner, types);

        spinner.setAdapter(adapterRecipe);
    }
    public void saveButton(){
        String strRecipe, strName;

        recipeType = (RecipeType)adapterRecipe
                .getItem(spinner.getSelectedItemPosition());
        strName = String.valueOf(editText.getText());
        strRecipe = String.valueOf(editText2.getText());

        if(strRecipe.equals("")){
            Toast.makeText(AddRecipeActivity.this,
                    "Brak przepisu! Dodaj przepis!",
                    Toast.LENGTH_SHORT).show();
        }else if(strName.equals("")){
            Toast.makeText(AddRecipeActivity.this,
                    "Podaj nazwę przepisu!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            recipe.setName(strName);
            recipe.setRecipe(strRecipe);
            recipe.setType(recipeType.getName());
            recipe.setRecipeType(recipeType);
            recipe.setFavourite(0);
            long result = recipeDAO.save(recipe);

            if (result > 0) {

                Toast.makeText(AddRecipeActivity.this,
                        "Zapisano",
                        Toast.LENGTH_SHORT).show();
                newMenuItem.setIcon(R.drawable.ic_action_new);
                newMenuItem.setEnabled(true);
            } else {
                Toast.makeText(this,
                        "Unable to save recipe",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                if (recipe != null) {
                    this.finish();
                    Intent i = new Intent(this.getApplicationContext(), AddIngredientActivity.class);
                    i.putExtra("recipe", recipe);
                    startActivity(i);
                }
                return true;
            case R.id.action_save:
                saveButton();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_recipe_actions, menu);

        newMenuItem = menu.findItem(R.id.action_new);
        return super.onCreateOptionsMenu(menu);
    }

}
