package com.example.MyCookBook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.example.MyCookBook.recipes.Recipe;
import com.example.MyCookBook.database.RecipeDAO;
import info.androidhive.actionbar.R;

import java.util.List;

/**
 * Created by Sonia on 2014-11-30.
 */
public class RecListAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> recipes;

    public RecListAdapter(Context context, List<Recipe> recipes) {
        super(context, R.layout.list_rec_item, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    private class ViewHolder {
        TextView recNameTxt;
        CheckBox recCheckBox;
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

        if (convertView == null) {
           final  ViewHolder holder =  new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_rec_item, null);

            holder.recNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_rec_name);
            holder.recCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            holder.recCheckBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {

                            Recipe recipe = (Recipe) holder.recCheckBox.getTag();

                            if(buttonView.isChecked()){
                                recipe.setSelected(buttonView.isChecked());

                                System.out.println("Checked : " + recipe.getName());

                                recipe.setFavourite(1);
                                recipeDAO.update(recipe);
                            }
                            else{
                                recipe.setSelected(false);

                                System.out.println("unChecked : " + recipe.getName());

                                recipe.setFavourite(0);
                                recipeDAO.update(recipe);
                            }
                        }
                    });

            convertView.setTag(holder);
            holder.recCheckBox.setTag(recipes.get(position));
            convertView.setTag(holder);

        } else {
            //holder = (ViewHolder) convertView.getTag();
            ((ViewHolder) convertView.getTag()).recCheckBox.setTag(recipes.get(position));
        }

        Recipe recipe = (Recipe) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.recNameTxt.setText(recipe.getName() + "");
        holder.recCheckBox.setChecked(recipes.get(position).isSelected());

        return convertView;
    }
}
