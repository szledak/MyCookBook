package com.example.MyCookBook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.MyCookBook.database.IngredientDAO;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.products.Product;
import info.androidhive.actionbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonia on 2014-12-14.
 */
public class IngListAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> ingredients;
    private ProductDAO productDAO;
    private Queries query;
    private ArrayList<Product> products;

    public IngListAdapter(Context context, List<Ingredient> recipes) {
        super(context, R.layout.list_ing_item, recipes);
        this.context = context;
        this.ingredients = recipes;
    }

    private class ViewHolder {
        TextView ingName;
        TextView ingAmount;
        TextView ingUnit;
    }

    //RecipeDAO recipeDAO = new RecipeDAO(context);
    IngredientDAO ingredientDAO = new IngredientDAO(context);

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =  new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_ing_item, null);

            holder.ingName = (TextView) convertView
                    .findViewById(R.id.txt_ing_name);
            holder.ingAmount = (TextView) convertView
                    .findViewById(R.id.txt_ing_amount);
            holder.ingUnit= (TextView) convertView
                    .findViewById(R.id.txt_ing_unit);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Ingredient ingredient = (Ingredient) getItem(position);
        productDAO = new ProductDAO(context);
        query = new Queries();
        products = productDAO.getProducts(query.getAllProducts());

        for(int i = 0; i < products.size(); i++)
            if(products.get(i).getId() == ingredients.get(position).getIdProduct()){
                holder.ingName.setText("- " + products.get(i).getName() + "");
            }

        holder.ingAmount.setText(ingredients.get(position).getAmount() + " ");
        holder.ingUnit.setText(ingredients.get(position).getUnit() + "");

        return convertView;
    }
}
