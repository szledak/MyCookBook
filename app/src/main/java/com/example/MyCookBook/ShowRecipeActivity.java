package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.*;
import android.widget.*;

import com.example.MyCookBook.adapter.IngListAdapter;
import com.example.MyCookBook.database.*;
import com.example.MyCookBook.ingredient.IngAddDialogFragment;
import com.example.MyCookBook.ingredient.IngUpdateDialogFragment;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.products.Product;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.recipes.SaveRecipeDialogFragment;

import info.androidhive.actionbar.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ShowRecipeActivity extends FragmentActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView txtRecipe;
    private TextView txtIngredients;
    private ActionBar actionbar;
    private IngredientDAO ingredientDAO;
    private Activity activity;
    private ProductDAO productDAO;
    private Queries query;
    private Product product;
    private ArrayList<Product> products;
    private RecipeDAO recipeDAO;
    private Recipe recipe;
    private IngListAdapter ingListAdapter;
    private IngAddDialogFragment ingredientAddFragment;
    private MenuItem favouriteMenuItem;
    private Editable txt;
    private EditText editText;
    private ArrayList<Ingredient> ingredients;
    private GetIngTask task;
    private Ingredient ingredient;
    private ExpandableHeightListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_recipe);

        initElements();

        Intent i = getIntent();
        recipe = i.getParcelableExtra("recipe");

        setActionBar();

        products = productDAO.getProducts(query.getAllProducts());

        task = new GetIngTask(this);
        task.execute((Void) null);

        editText.setText(recipe.getRecipe());

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void setActionBar(){
        actionbar = getActionBar();
        actionbar.setTitle(recipe.getName());
        actionbar.setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.action_bar_color)));
    }

    public void initElements() {
        recipeDAO = new RecipeDAO(this);
        ingredientDAO = new IngredientDAO(activity);
        productDAO = new ProductDAO(activity);
        query = new Queries();

        editText = (EditText) findViewById(R.id.editText2);
        listView = new ExpandableHeightListView(this);
        listView = (ExpandableHeightListView) findViewById(R.id.listView);
    }


    public void onItemClick(AdapterView<?> list, View view, int position,
                            long id) {
        Ingredient ingredient = (Ingredient) list.getItemAtPosition(position);

        if (ingredient != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedIngredient", ingredient);
            IngUpdateDialogFragment ingUpdateDialogFragment = new IngUpdateDialogFragment();
            ingUpdateDialogFragment.setArguments(arguments);
            ingUpdateDialogFragment.show(getFragmentManager(),
                    ingUpdateDialogFragment.ARG_ITEM_ID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_show_recipe, container, false);

        return rootView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Ingredient ingredient = (Ingredient) parent.getItemAtPosition(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Usuwanie");
        alertDialog.setMessage("Usunąć zaznaczony składnik?");

        alertDialog.setIcon(R.drawable.ic_action_discard);
        alertDialog.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        long result = ingredientDAO.deleteIngredient(ingredient);
                        ingListAdapter.remove(ingredient);

                        if (result > 0) {
                            ShowRecipeActivity activity = ShowRecipeActivity.this;
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(ShowRecipeActivity.this,
                                    "Unable to delete ingredient",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(ShowRecipeActivity.this,
                                "Usunięto",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialog.setNegativeButton("Nie", null);
        alertDialog.show();

        return true;
    }


    public class GetIngTask extends AsyncTask<Void, Void, ArrayList<Ingredient>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetIngTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Ingredient> doInBackground(Void... arg0) {
            ArrayList<Ingredient> ingList = ingredientDAO.getIngredients(query.getIngredients(String.valueOf(recipe.getId())));
            return ingList;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                ingredients = ingList;
                if (ingList != null) {
                    if (ingList.size() != 0) {
                        ingListAdapter = new IngListAdapter(ShowRecipeActivity.this,
                                ingList);
                        listView.setAdapter(ingListAdapter);

                        listView.setExpanded(true);
                    }
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_fav:{
                actionAddToFavourite(item);
                return true;
            }
            case R.id.action_edit:{
                actionEdit(item);
                return true;
            }
            case R.id.action_new:{
                actionNewRecipe(item);
                return true;
            }
            case R.id.action_save:{
                actionSave(item);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //TODO create function's body
    private void actionSave(MenuItem item) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("selectedRecipe", recipe);
        SaveRecipeDialogFragment saveRecipeDialogFragment = new SaveRecipeDialogFragment();
        saveRecipeDialogFragment.setArguments(arguments);
        saveRecipeDialogFragment.show(getFragmentManager(),
                saveRecipeDialogFragment.ARG_ITEM_ID);
    }

    public void actionAddToFavourite(MenuItem item){
        favouriteMenuItem = item;

        favouriteMenuItem.setChecked(!item.isChecked());

        if (item.isChecked()) {
            favouriteMenuItem.setIcon(R.drawable.ic_action_favorite);
            recipe.setFavourite(1);
            recipeDAO.update(recipe);
        } else {
            favouriteMenuItem.setIcon(R.drawable.ic_action_favorite_dark);
            recipe.setFavourite(0);
            recipeDAO.update(recipe);
        }

    }

    public void actionEdit(MenuItem item){
        item.setChecked(!item.isChecked());

        if (item.isChecked()) {
            editText.setEnabled(true);
        } else {
            txt = editText.getText();
            recipe.setRecipe(txt.toString());
            recipeDAO.update(recipe);
            editText.setEnabled(false);
        }
    }

    public void actionNewRecipe(MenuItem item){
        Intent i = getIntent();
        recipe = i.getParcelableExtra("recipe");
        if (recipe != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedRecipe", recipe);
            IngAddDialogFragment ingAddDialogFragment = new IngAddDialogFragment();
            ingAddDialogFragment.setArguments(arguments);
            ingAddDialogFragment.show(getFragmentManager(),
                    ingAddDialogFragment.ARG_ITEM_ID);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_show_recipe_actions, menu);

        if (recipe.getFavourite() == 1) {
            menu.findItem(R.id.action_add_to_fav).setIcon(R.drawable.ic_action_favorite);
        } else {
            menu.findItem(R.id.action_add_to_fav).setIcon(R.drawable.ic_action_favorite_dark);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public void updateView() {
        task = new GetIngTask(this);
        task.execute((Void) null);
    }

    //@Override
    public void onFinishDialog() {
        updateView();
    }
}
