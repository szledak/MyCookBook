package com.example.MyCookBook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.MyCookBook.calculator.CalcProduct;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.products.Product;
import info.androidhive.actionbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonia on 2014-12-08.
 */
public class CalcListAdapter extends ArrayAdapter<CalcProduct> {
    private Context context;
    List<CalcProduct> calcProducts;
    private ProductDAO productDAO;
    private Queries query;
    private ArrayList<Product> products;
    Activity activity;


    public CalcListAdapter(Context context, List<CalcProduct> calcProducts) {
        super(context, R.layout.list_prod_item, calcProducts);
        this.context = context;
        this.calcProducts = calcProducts;
    }
    private class ViewHolder {
        TextView prodNameTxt;
        TextView prodAmountTxt;
        ImageView imageView;
    }

    @Override
    public int getCount() {
        return calcProducts.size();
    }

    @Override
    public CalcProduct getItem(int position) {
        return calcProducts.get(position);
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
            holder.prodNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_prod_name);
            holder.prodAmountTxt = (TextView) convertView
                    .findViewById(R.id.txt_prod_kcal);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        productDAO = new ProductDAO(activity);
        query = new Queries();
        CalcProduct calcProduct = (CalcProduct) getItem(position);
        products = productDAO.getProducts(query.getAllProducts());

        holder.prodNameTxt.setText(calcProducts.get(position).getName()+"");
        holder.prodAmountTxt.setText(calcProducts.get(position).getAmount()+" [g]");

          for(int i = 0; i < products.size(); i++){
             if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Warzywo") )
                    holder.imageView.setImageResource(R.drawable.vegetable);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Owoc") )
                 holder.imageView.setImageResource(R.drawable.fruit);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Produkt Zbożowy") )
                 holder.imageView.setImageResource(R.drawable.cereal);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Nabiał") )
                 holder.imageView.setImageResource(R.drawable.dairy);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Mięso") )
                 holder.imageView.setImageResource(R.drawable.meat);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Inne") )
                 holder.imageView.setImageResource(R.drawable.other);
             else if((products.get(i).getName()).equals(calcProducts.get(position).getName())  && (products.get(i).getType()).equals("Słodycze") )
                 holder.imageView.setImageResource(R.drawable.sweets);
            }
        return convertView;
    }
}
