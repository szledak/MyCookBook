package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.MyCookBook.adapter.AutoTextListAdapter;
import com.example.MyCookBook.adapter.IngListAdapter;
import com.example.MyCookBook.database.*;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.ingredient.IngredientUnit;
import com.example.MyCookBook.products.Product;
import com.example.MyCookBook.recipes.Recipe;
import info.androidhive.actionbar.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AddIngredientActivity extends Activity implements View.OnClickListener {

    private Recipe recipe;
    private ActionBar actionBar;
    private AutoCompleteTextView textView;
    private EditText editText;
    private ImageView imageView;
    private ListView listView;
    private Spinner spinner;
    private ProductDAO productDAO;
    private ArrayList<Product> productList;
    private ArrayList<Recipe> recipeList;
    private Queries query;
    private int selectionID;
    private GetIngTask task;
    private IngredientDAO ingredientDAO;
    private ArrayList<Ingredient> ingredients;
    private IngListAdapter ingListAdapter;
    private Ingredient ingredient;
    private ArrayAdapter<IngredientUnit> adapterUnit;
    private IngredientUnit ingredientUnit;
    private RecipeDAO recipeDAO;
    private int recID;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.setContentView(R.layout.activity_add_ingredient);

        Intent i = getIntent();
        recipe = i.getParcelableExtra("recipe");

        actionBar = getActionBar();
        actionBar.setTitle(recipe.getName());
        actionBar.setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.action_bar_color)));

        textView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView4);
        editText = (EditText)findViewById(R.id.editText3);
        imageView = (ImageView)findViewById(R.id.im_view_add);
        listView = (ListView)findViewById(R.id.listView2);
        spinner = (Spinner)findViewById(R.id.spinner2);

        imageView.setOnClickListener(AddIngredientActivity.this);

        productDAO = new ProductDAO(this);
        query = new Queries();
        ingredientDAO = new IngredientDAO(this);
        ingredientUnit = new IngredientUnit();
        recipeDAO = new RecipeDAO(this);

        recipeList = recipeDAO.getRecipes(query.getAllRecipes());
        productList = productDAO.getProducts(query.getAllProducts());

        for(int k = 0; k < recipeList.size(); k++)
            if(recipeList.get(k).getName().equals(recipe.getName()))
                recID = recipeList.get(k).getId();

        textView.setThreshold(1);
        textView.setAdapter(new AutoTextListAdapter(this, productList));

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
        Product selection = (Product)parent.getItemAtPosition(position);
        selectionID = selection.getId();
    }
});

        task = new GetIngTask(this);
        task.execute((Void) null);

        IngredientUnitDAO ingredientUnitDAO = new  IngredientUnitDAO(this);

        List< IngredientUnit> types = ingredientUnitDAO.getIngredientUnits();
        adapterUnit = new ArrayAdapter<IngredientUnit>(this,
                R.layout.my_spinner, types);

        spinner.setAdapter(adapterUnit);
    }

    public void imageViewOnClick(){

        ingredient = new Ingredient();

        int edit = editText.getText().length();
        int amount = Integer.parseInt(String.valueOf(editText.getText()));

        if(selectionID <=0 || edit <=0){
            Toast.makeText(AddIngredientActivity.this,
                    "Uzupełnij puste pola",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            ingredient.setIdProduct(selectionID);
            ingredient.setIdRecipe(recID);
            ingredient.setAmount(amount);

            ingredientUnit = (IngredientUnit)adapterUnit
                    .getItem(spinner.getSelectedItemPosition());

            ingredient.setIngredientUnit(ingredientUnit);
            ingredient.setUnit(ingredientUnit.getName());

            long result = ingredientDAO.save(ingredient);

            if(result > 0){

                textView.setText("");
                editText.setText("");

                AddIngredientActivity activity= AddIngredientActivity.this;
                activity.onFinishDialog();

                Toast.makeText(AddIngredientActivity.this,
                        "Dodano składnik",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.im_view_add:
                imageViewOnClick();
                break;
        }
    }

    public class GetIngTask extends AsyncTask<Void, Void, ArrayList<Ingredient>> {


        private final WeakReference<Activity> activityWeakRef;

        public GetIngTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Ingredient> doInBackground(Void... arg0) {

            ArrayList<Ingredient> ingList = ingredientDAO.getIngredients(query.getIngredients(String.valueOf(recID)));
            return ingList;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                ingredients = ingList;
                if (ingList != null) {
                    if (ingList.size() != 0) {
                        ingListAdapter = new IngListAdapter(AddIngredientActivity.this,
                                ingList);
                        listView.setAdapter(ingListAdapter);
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new GetIngTask(this);
        task.execute((Void) null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //@Override
    public void onFinishDialog() {
        updateView();
    }
}
