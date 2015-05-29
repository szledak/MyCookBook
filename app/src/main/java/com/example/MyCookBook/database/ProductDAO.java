package com.example.MyCookBook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.MyCookBook.products.*;
import com.example.MyCookBook.products.Product;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sonia on 2014-11-24.
 */
public class ProductDAO extends DatabaseDAO {

    public ProductDAO(Context context) {
        super(context);
    }
    private Context mContext;


    public long save(Product product) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", product.getName());
        values.put("kcal", product.getKcal());
        values.put("TypProduktu", product.getProductType().getName());

        return database.insert("PRODUKTY", null, values);
    }

    public long update(Product product) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", product.getName());
        values.put("kcal", product.getKcal());
        values.put("TypProduktu", product.getProductType().getName()); // ?

        long result = database.update("PRODUKTY", values,
                "_ID =?",
                new String[] { String.valueOf(product.getId()) });
        Log.d("Update Result:", "=" + result);

        return result;
    }

    public int deleteProduct(Product product) {
        return database.delete("PRODUKTY", "_ID =?",
                new String[] { product.getId() + "" });
    }

    public ArrayList<Product> getProducts(String mString) {
        ArrayList<Product> products = new ArrayList<Product>();

        DatabaseHelper  mDbHelper;
        mDbHelper = DatabaseHelper.getHelper(mContext);
        try {
            mDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.d("query", mString);
        Cursor cursor = database.rawQuery(mString, null);

        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));

            product.setKcal(cursor.getInt(2));
            product.setType(cursor.getString(3));

            ProductType productType = new ProductType();
            productType.setId(cursor.getInt(4));
            productType.setName(cursor.getString(5));

            product.setProductType(productType);
            products.add(product);
        }

        return products;
    }
}
