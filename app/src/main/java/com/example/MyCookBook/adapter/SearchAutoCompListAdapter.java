package com.example.MyCookBook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.MyCookBook.products.Product;
import info.androidhive.actionbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonia on 2014-12-14.
 */
public class SearchAutoCompListAdapter extends ArrayAdapter<Product> implements Filterable {
    private Context context;

    List<Product> original;
    List<Product> fitems;
    List<Product> array;
    private Filter filter;

    public SearchAutoCompListAdapter(Context context, List<Product> products) {
        super(context, R.layout.list_autocom_search_item, products);
        this.context = context;

        this.original = new ArrayList(products);
        this.fitems = new ArrayList(products);
        this.array = new ArrayList(products);
    }

    private class ViewHolder {
        TextView prodNameTxt;
    }

    @Override
    public int getCount() {
        return original.size();
    }

    @Override
    public Product getItem(int position) {
        return original.get(position);
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
            convertView = inflater.inflate(R.layout.list_autocom_search_item, null);

            holder = new ViewHolder();
            holder.prodNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_prod_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(position);

        holder.prodNameTxt.setText(product.getName() + "");

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new myFilter();

        return filter;
    }

    private class myFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                String prefix = constraint.toString();

                final ArrayList<Product> list = new ArrayList<Product>(original);
                final ArrayList<Product> nlist = new ArrayList<Product>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    final Product pkmn = list.get(i);
                    final String value = pkmn.getName();
                    if (value.startsWith(prefix)) {
                        nlist.add(pkmn);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            } else {
                int count = array.size();
                for (int i = 0; i < count; i++) {
                    Product pkmn = (Product) array.get(i);
                    original.add(pkmn);
                }
                ArrayList<Product> list = new ArrayList<Product>(original);
                results.values = list;
                results.count = list.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();

            fitems = (ArrayList<Product>) results.values;

            if (results != null && results.count > 0) {
                original.clear();
                int count = fitems.size();
                for (int i = 0; i < count; i++) {
                    Product pkmn = (Product) fitems.get(i);
                    original.add(pkmn);

                }
            } else {
                notifyDataSetInvalidated();
            }
            notifyDataSetChanged();
        }
    }
}