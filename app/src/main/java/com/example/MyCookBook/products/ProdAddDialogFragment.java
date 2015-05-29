package com.example.MyCookBook.products;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.MyCookBook.ProductsActivity;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.ProductTypeDAO;
import info.androidhive.actionbar.R;

import java.util.ArrayList;

/**
 * Created by Sonia on 2014-11-28.
 */
public class ProdAddDialogFragment extends DialogFragment {

    private EditText prodNameEtxt;
    private EditText prodKcalEtxt;
    private Spinner typeSpinner;
    private LinearLayout submitLayout;
    private Product product;
    private ProductDAO productDAO;
    private ArrayAdapter<ProductType> adapter;
    public static final String ARG_ITEM_ID = "add_prod_dialog_fragment";

    public interface ProdAddFragmentListener {
        void onFinishDialog();
    }

    public ProdAddDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        productDAO = new ProductDAO(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customDialogView = inflater.inflate(R.layout.fragment_add_prod,
                null);
        builder.setView(customDialogView);

        prodNameEtxt = (EditText) customDialogView.findViewById(R.id.editText2);
        prodKcalEtxt = (EditText) customDialogView.findViewById(R.id.editText);


        typeSpinner = (Spinner) customDialogView
                .findViewById(R.id.spinner);
        submitLayout = (LinearLayout) customDialogView.findViewById(R.id.layout_submit);
        submitLayout.setVisibility(View.GONE);
        setValue();

        builder.setTitle(R.string.add_prod);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_new1);
        builder.setPositiveButton(R.string.add,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        product = new Product();
                        product.setName(prodNameEtxt.getText().toString());
                        product.setKcal(Integer.parseInt(prodKcalEtxt
                                .getText().toString()));
                        ProductType ptype = (ProductType)adapter
                                .getItem(typeSpinner.getSelectedItemPosition());
                        product.setProductType(ptype);

                        long result = productDAO.save(product);
                        if (result > 0) {
                            ProductsActivity activity = (ProductsActivity) getActivity();
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to save product",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

    private void setValue() {

       // Queries query = new Queries();
        ProductTypeDAO productTypeDAO = new ProductTypeDAO(getActivity());

        ArrayList<ProductType> types = productTypeDAO.getProductTypes();

        adapter = new ArrayAdapter<ProductType>(getActivity(),
                R.layout.my_spinner, types);
        typeSpinner.setAdapter(adapter);

            prodNameEtxt.setText("");
            prodKcalEtxt.setText("");

            typeSpinner.setSelection(0);
    }
}
