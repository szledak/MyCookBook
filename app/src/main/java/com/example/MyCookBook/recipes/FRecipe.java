package com.example.MyCookBook.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.MyCookBook.ShowRecipeActivity;
import com.example.MyCookBook.adapter.RecListAdapter;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.database.RecipeDAO;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2015-05-13.
 */
public class FRecipe extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private Activity activity;
    private ListView recipesListView;
    private ArrayList<Recipe> recipes;
    private RecListAdapter recListAdapter;
    private RecipeDAO recipeDAO;
    private String strQuery;
    private GetRecTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        strQuery = getArguments().getString("query");

        recipeDAO = new RecipeDAO(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container,
                false);
        findViewsById(view);

        task = new GetRecTask(activity);
        task.execute((Void) null);

        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Recipe recipe = (Recipe) adapter.getItemAtPosition(position);
                // Launching new Activity on selecting single List Item

                Intent i = new Intent(getActivity().getApplicationContext(), ShowRecipeActivity.class);

                // sending data to new activity
                i.putExtra("recipe", recipe);
                startActivity(i);
            }
        });
        recipesListView.setOnItemLongClickListener(this);
        return view;
    }

    private void findViewsById(View view) {
        recipesListView = (ListView) view.findViewById(R.id.list_prod);
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position,
                            long id) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        final Recipe recipe = (Recipe) parent.getItemAtPosition(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Usuwanie");
        alertDialog.setMessage("Usunąć przepis: \n'" + recipe.getName() + "' ?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_action_discard);
        alertDialog.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        // Use AsyncTask to delete from database
                        recipeDAO.deleteRecipe(recipe);
                        recListAdapter.remove(recipe);
                        Toast.makeText(activity, "Usunięto", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setNegativeButton("Nie", null);
        alertDialog.show();

        return true;
    }

    public class GetRecTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetRecTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... arg0) {
            ArrayList<Recipe> recipeList = recipeDAO.getRecipes(strQuery);
            return recipeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                recipes = recList;
                if (recList != null) {
                    if (recList.size() != 0) {
                        recListAdapter = new RecListAdapter(activity,
                                recList);
                        recipesListView.setAdapter(recListAdapter);
                    } else {
//                        Toast.makeText(activity, "No Recipe Records",
//                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new GetRecTask(activity);
        task.execute((Void) null);
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.recipes);
        getActivity().getActionBar().setTitle(R.string.recipes);
        super.onResume();
    }
}
