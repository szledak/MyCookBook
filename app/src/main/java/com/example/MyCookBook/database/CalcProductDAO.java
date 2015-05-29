package com.example.MyCookBook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.MyCookBook.calculator.CalcProduct;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sonia on 2014-12-08.
 */
public class CalcProductDAO extends DatabaseDAO {

    public CalcProductDAO(Context context) {
        super(context);
    }

    private Context mContext;

    public long save(CalcProduct calcProduct) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", calcProduct.getName());
        values.put("Ilosc", calcProduct.getAmount());

        return database.insert("KALKULATOR", null, values);
    }

    public long update(CalcProduct calcProduct) {
        ContentValues values = new ContentValues();
        values.put("Nazwa", calcProduct.getName());
        values.put("Ilosc", calcProduct.getAmount());

        long result = database.update("KALKULATOR", values,
                "_ID =?",
                new String[] { String.valueOf(calcProduct.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;
    }

    public int deleteCalcProduct(CalcProduct calcProduct) {
        return database.delete("KALKULATOR", "_ID =?",
                new String[] { calcProduct.getId() + "" });
    }

    public long deleteAll(){
        long result = database.delete("KALKULATOR",null, null);
        return result;
    }
    public ArrayList<CalcProduct> getCalcProduct(String mString) {
        ArrayList<CalcProduct> calcProducts = new ArrayList<CalcProduct>();

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

            CalcProduct calcProduct = new CalcProduct();
            calcProduct.setId(cursor.getInt(0));
            calcProduct.setName(cursor.getString(1));
            calcProduct.setAmount(cursor.getInt(2));

            calcProducts.add(calcProduct);
        }
        return calcProducts;
    }
}