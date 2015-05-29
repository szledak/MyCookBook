package com.example.MyCookBook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.MyCookBook.database.RecipeDAO;
import com.example.MyCookBook.recipes.Recipe;
import info.androidhive.actionbar.R;

import java.util.List;

/**
 * Created by Sonia on 2014-12-13.
 */
public class SearchListAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> recipes;

    public SearchListAdapter(Context context, List<Recipe> recipes) {
        super(context, R.layout.list_calc_item, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    private class ViewHolder {
        TextView recNameTxt;
        ImageView imageView;
    }

    RecipeDAO recipeDAO = new RecipeDAO(context);

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_calc_item, null);

            holder = new ViewHolder();

            holder.recNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_prod_name);

            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe recipe = (Recipe) getItem(position);

        holder.recNameTxt.setText(recipe.getName() + "");
        if((recipes.get(position).getType().equals("Danie Główne")))
            holder.imageView.setImageResource(R.drawable.ic_main_course);
        else if((recipes.get(position).getType().equals("Zupa")))
            holder.imageView.setImageResource(R.drawable.ic_soup);
        else if((recipes.get(position).getType().equals("Deser")))
            holder.imageView.setImageResource(R.drawable.ic_dessert);
        else if((recipes.get(position).getType().equals("Sałatka")))
            holder.imageView.setImageResource(R.drawable.ic_salad);
        else if((recipes.get(position).getType().equals("Przystawka")))
            holder.imageView.setImageResource(R.drawable.ic_starter);

        return convertView;
    }
}
