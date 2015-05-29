package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.MyCookBook.adapter.FavListAdapter;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.database.RecipeDAO;
import info.androidhive.actionbar.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Sonia on 2014-12-04.
 */
public class FavouritesActivity extends Activity  implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    private ListView recipesListView;
    private ArrayList<Recipe> recipes;
    private FavListAdapter favListAdapter;
    private RecipeDAO recipeDAO;
    private Queries query = new Queries();;
    private GetRecTask task;
    private ActionBar actionbar;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        actionbar = getActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable( getResources().getColor(R.color.action_bar_color)));

        recipeDAO = new RecipeDAO(this);
        recipesListView = (ListView)findViewById(R.id.list_prod);

        task = new GetRecTask(this);
        task.execute((Void) null);

        recipesListView.setOnItemLongClickListener(this);
        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Recipe recipe = (Recipe) adapter.getItemAtPosition(position);

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), ShowRecipeActivity.class);

                // sending data to new activity
                i.putExtra("recipe", recipe);
                startActivity(i);
            }
        });
    }


    public class GetRecTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetRecTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... arg0) {
            ArrayList<Recipe> recipeList = recipeDAO.getRecipes(query.getFavourites());
            return recipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                recipes = recList;
                if (recList != null) {
                    if (recList.size() != 0) {
                        favListAdapter = new FavListAdapter(FavouritesActivity.this,
                                recList);
                        recipesListView.setAdapter(favListAdapter);
                    } else {
                        Toast.makeText(FavouritesActivity.this, "No Recipe Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
    public void updateView() {
        task = new GetRecTask(this);
        task.execute((Void) null);
    }

    @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

}
