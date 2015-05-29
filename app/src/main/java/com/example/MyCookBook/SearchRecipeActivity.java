package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.MyCookBook.adapter.SearchAutoCompListAdapter;
import com.example.MyCookBook.adapter.SearchListAdapter;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.database.RecipeDAO;
import com.example.MyCookBook.database.RecipeTypeDAO;
import com.example.MyCookBook.products.Product;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.recipes.RecipeType;
import info.androidhive.actionbar.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class SearchRecipeActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {

    private Activity activity;
    private ActionBar actionBar;
    private GetRecSearchTask task;

    private Spinner spinner;
    private AutoCompleteTextView textView, textView2, textView3;
    private RadioGroup radioGroup;
    private RadioButton radioVeg, radioMeat, radioAll;
    private ImageView imageView;
    private ListView listView;

    private Queries query;
    private ProductDAO productDAO;
    private RecipeDAO recipeDAO;
    private RecipeTypeDAO recipeTypeDAO;
    private SearchListAdapter searchListAdapter;

    private ArrayAdapter<RecipeType> adapter;
    private ArrayList<Product> productList;
    private ArrayList<Recipe> recipes;

    private String sel_1, sel_2, sel_3, str1, str2, str3, selStr, strRadio, rType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_recipe);

        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_color)));

        initObjects();

        productList = productDAO.getProducts(query.getAllProducts());

        initUIElements();

        setSpinner();

        setAutcomleteTextView();
        checkedAutocompleteTextView();
        setRadioButtons();

        setUIElementListeners();
    }

    public void initObjects(){
        recipeTypeDAO = new RecipeTypeDAO(this);
        productDAO = new ProductDAO(this);
        recipeDAO = new RecipeDAO(this);
        query = new Queries();
    }

    public void initUIElements(){
        spinner = (Spinner)findViewById(R.id.spinner);
        textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        textView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        textView3 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioVeg = (RadioButton) findViewById(R.id.radioButton);
        radioMeat = (RadioButton) findViewById(R.id.radioButton2);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView2);
    }

    public void checkedAutocompleteTextView(){
        if(sel_1.equals("")){
            textView2.setEnabled(false);
            textView3.setEnabled(false);
        }

        setAutcomleteTextViewListeners();
    }

    private void setAutcomleteTextViewListeners() {
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Product selection = (Product)parent.getItemAtPosition(position);
                sel_1 = selection.getName();

                if(!sel_1.equals("")){
                    textView2.setEnabled(true);
                }
            }

        });

        textView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Product selection2 = (Product)parent.getItemAtPosition(position);
                sel_2 = selection2.getName();

                if(!sel_2.equals("")){
                    textView3.setEnabled(true);
                }

            }

        });

        textView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Product selection2 = (Product)parent.getItemAtPosition(position);
                sel_3 = selection2.getName();
            }

        });
    }

    private void setUIElementListeners(){
        spinner.setOnItemSelectedListener(SearchRecipeActivity.this);
        imageView.setOnClickListener(SearchRecipeActivity.this);

        listView.setOnItemClickListener(SearchRecipeActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Recipe recipe = (Recipe) adapter.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(), ShowRecipeActivity.class);

                i.putExtra("recipe", recipe);
                startActivity(i);
            }
        });
    }
    public void setSpinner(){
        ArrayList<RecipeType> types = recipeTypeDAO.getRecipeTypes();
        adapter = new ArrayAdapter<RecipeType>(this,
                R.layout.my_spinner, types);
        spinner.setAdapter(adapter);
    }

    public void setRadioButtons(){
        radioVeg.setChecked(true);
        strRadio = "(prod.TypProduktu != 'Mięso')";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton) {
                    strRadio = "(prod.TypProduktu != 'Mięso')";
                } else if(checkedId == R.id.radioButton2) {
                    strRadio = "(prod.TypProduktu = 'Mięso')";
                }
            }
        });
    }
    private void setAutcomleteTextView() {
        textView.setThreshold(1);
        textView.setAdapter(new SearchAutoCompListAdapter(this, productList));
        textView2.setThreshold(1);
        textView2.setAdapter(new SearchAutoCompListAdapter(this, productList));
        textView3.setThreshold(1);
        textView3.setAdapter(new SearchAutoCompListAdapter(this, productList));
        sel_1 = "";
        sel_2 = "";
        sel_3 = "";
    }

    public void buttonOnClick(){

        RecipeType recType = (RecipeType)adapter.getItem(spinner.getSelectedItemPosition());
        rType = recType.getName();

        if(!sel_1.equals(""))
            str1 = " AND prod.Nazwa = '"+ sel_1 +"' ";
        else
             str1 = " ";

        if(!sel_2.equals(""))
            str2 = " OR prod.Nazwa = '"+ sel_2 +"' ";
        else if(!sel_2.equals("") && sel_1.equals(""))
            str2 = " AND prod.Nazwa = '"+ sel_2 +"' ";
        else
            str2 = " ";

        if(!sel_3.equals(""))
            str3 = " OR prod.Nazwa = '"+ sel_3 +"' ";
        else if(!sel_3.equals("") && sel_1.equals("") && sel_2.equals(""))
            str3 = " AND prod.Nazwa = '"+ sel_3 +"' ";
        else
            str3 = " ";

        task = new GetRecSearchTask(this);
        task.execute((Void) null);

        updateView();
    }

    public void clearAllFields(){
        textView.setText("");
        textView2.setText("");
        textView3.setText("");

        textView2.setEnabled(false);
        textView3.setEnabled(false);

        sel_1 = "";
        sel_2 = "";
        sel_3 = "";

        str1 = " ";
        str2 = " ";
        str3 = " ";

     if(searchListAdapter != null){
         searchListAdapter.clear();
         searchListAdapter.notifyDataSetChanged();
     }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                clearAllFields();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_calc_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public class GetRecSearchTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetRecSearchTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... arg0) {
            ArrayList<Recipe> recipeList = recipeDAO.getRecipes(query.searchRecipes(strRadio, rType, str1, str2, str3));
            return recipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                recipes = recipeList;
                if (recipeList != null) {
                    if (recipeList.size() != 0) {
                        searchListAdapter = new SearchListAdapter(SearchRecipeActivity.this,
                                recipeList);
                        listView.setAdapter(searchListAdapter);
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new  GetRecSearchTask(this);
        task.execute((Void) null);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.imageView2:
                buttonOnClick();
                break;
        }
    }
}
