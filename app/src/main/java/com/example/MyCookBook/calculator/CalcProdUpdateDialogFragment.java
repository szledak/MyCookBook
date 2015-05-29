package com.example.MyCookBook.calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.example.MyCookBook.CalculatorActivity;
import com.example.MyCookBook.calculator.CalcProduct;
import com.example.MyCookBook.database.CalcProductDAO;
import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2014-12-09.
 */
public class CalcProdUpdateDialogFragment extends DialogFragment {

    private EditText prodNameEtxt;
    private EditText prodAmountEtxt;
    private Spinner unitSpinner;
    private LinearLayout submitLayout;
    private CalcProduct calcProduct;
    private CalcProductDAO calcProductDAO;

    public static final String ARG_ITEM_ID = "prod_dialog_fragment";

    public interface CalcProdUpdateDialogFragmentListener {
        void onFinishDialog();
    }

    public CalcProdUpdateDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        calcProductDAO = new CalcProductDAO(getActivity());

        Bundle bundle = this.getArguments();

        calcProduct = (CalcProduct) bundle.getParcelable("selectedProduct");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customDialogView = inflater.inflate(R.layout.fragment_calc_edit,
                null);
        builder.setView(customDialogView);

        prodNameEtxt = (EditText) customDialogView.findViewById(R.id.editText2);
        prodAmountEtxt = (EditText) customDialogView.findViewById(R.id.editText4);

        submitLayout = (LinearLayout) customDialogView.findViewById(R.id.layout_submit);
        submitLayout.setVisibility(View.GONE);
        setValue();

        builder.setIcon(R.drawable.ic_action_edit);
        builder.setTitle(R.string.update_prod);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        calcProduct.setName(prodNameEtxt.getText().toString());
                        calcProduct.setAmount(Integer.parseInt(prodAmountEtxt
                                .getText().toString()));


                        long result = calcProductDAO.update(calcProduct);

                        if (result > 0) {
                            CalculatorActivity activity = (CalculatorActivity) getActivity();
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to update product",
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

        if (calcProduct != null) {
            prodNameEtxt.setText(calcProduct.getName());
            prodAmountEtxt.setText(calcProduct.getAmount() + "");
        }
    }
}
