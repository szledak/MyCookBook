package com.example.MyCookBook.database;

import android.content.Context;
import android.database.Cursor;
import com.example.MyCookBook.products.ProductType;

import java.util.ArrayList;

/**
 * Created by Sonia on 2014-11-26.
 */
public class ProductTypeDAO extends DatabaseDAO {

    private Context mContext;

    public ProductTypeDAO(Context context) {
        super(context);
    }

    public ArrayList<ProductType> getProductTypes() {

        ArrayList<ProductType> productTypes = new ArrayList<ProductType>();

        Cursor cursor = database.query("TYP_PRODUKTU",
                new String[] {"_ID","TypProduktu" }, null, null, null, null,
                null);

        while (cursor.moveToNext()) {
            ProductType productType = new ProductType();

            productType.setId(cursor.getInt(0));
            productType.setName(cursor.getString(1));

            productTypes.add(productType);
        }

        return productTypes;
    }
}
