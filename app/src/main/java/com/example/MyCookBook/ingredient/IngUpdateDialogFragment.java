package com.example.MyCookBook.ingredient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.MyCookBook.ShowRecipeActivity;
import com.example.MyCookBook.adapter.AutoTextListAdapter;
import com.example.MyCookBook.adapter.ProdListAdapter;
import com.example.MyCookBook.database.IngredientDAO;
import com.example.MyCookBook.database.IngredientUnitDAO;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.products.Product;
import info.androidhive.actionbar.R;

import java.util.ArrayList;
import java.util.List;


public class IngUpdateDialogFragment  extends DialogFragment {

    private AutoCompleteTextView ingName;
    private EditText ingAmount;
    private Spinner spinner;
    private LinearLayout submitLayout;
    private Ingredient ingredient;
    private IngredientDAO ingredientDAO;
    private ArrayAdapter<IngredientUnit> adapter;
    private String selectionName;
    private int selectionID;
    private ProductDAO productDAO;
    private Queries query;
    private ArrayList<Product> productList;

    public static final String ARG_ITEM_ID = "prod_dialog_fragment";

    public interface IngUpdateDialogFragmentListener {
        void onFinishDialog();
    }

    public IngUpdateDialogFragment(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ingredientDAO = new IngredientDAO(getActivity());

        Bundle bundle = this.getArguments();

        ingredient= (Ingredient) bundle.getParcelable("selectedIngredient");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customDialogView = inflater.inflate(R.layout.fragment_add_ing,
                null);

        builder.setView(customDialogView);

        ingName = (AutoCompleteTextView) customDialogView.findViewById(R.id.autoCompleteTextView);
        ingAmount = (EditText) customDialogView.findViewById(R.id.editText2);

        spinner = (Spinner) customDialogView
                .findViewById(R.id.spinner);
        submitLayout = (LinearLayout) customDialogView.findViewById(R.id.layout_submit);
        submitLayout.setVisibility(View.GONE);
        
        setValue();

        productDAO = new ProductDAO(getActivity());
        query = new Queries();
        productList = productDAO.getProducts(query.getAllProducts());

        ingName.setThreshold(1);
        ingName.setAdapter(new AutoTextListAdapter(getActivity(), productList));

        ingName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Product selection = (Product)parent.getItemAtPosition(position);
                selectionName = selection.getName();
                selectionID = selection.getId();
            }
        });

        builder.setIcon(R.drawable.ic_action_edit_dark);

        builder.setTitle(R.string.update_ingredient);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        for(int i = 0; i < productList.size(); i++)
                        if(productList.get(i).getId() == selectionID)
                            ingredient.setIdProduct(productList.get(i).getId());

                        ingredient.setAmount(Integer.parseInt(ingAmount
                                .getText().toString()));

                        IngredientUnit unit = (IngredientUnit)adapter
                                .getItem(spinner.getSelectedItemPosition());

                        ingredient.setIngredientUnit(unit);
                        long result = ingredientDAO.update(ingredient);

                        if (result > 0) {
                            ShowRecipeActivity activity = (ShowRecipeActivity) getActivity();
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to update ingredient",
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

        IngredientUnitDAO ingredientUnitDAO = new IngredientUnitDAO(getActivity());

        List<IngredientUnit> types = ingredientUnitDAO.getIngredientUnits();

        adapter = new ArrayAdapter<IngredientUnit>(getActivity(),
                R.layout.my_spinner, types);

        spinner.setAdapter(adapter);

        productDAO = new ProductDAO(getActivity());
        query = new Queries();
        productList = productDAO.getProducts(query.getAllProducts());

        int pos = ingredient.getIngredientUnit().getId()-1;

        if (ingredient != null) {

        for(int i = 0; i < productList.size(); i++)
            if(productList.get(i).getId() == ingredient.getIdProduct())
                ingName.setText(productList.get(i).getName());

            ingAmount.setText(ingredient.getAmount() + "");
            spinner.setSelection(pos);
        }
    }
}
