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
import com.example.MyCookBook.database.IngredientDAO;
import com.example.MyCookBook.database.IngredientUnitDAO;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.products.Product;
import com.example.MyCookBook.recipes.Recipe;
import info.androidhive.actionbar.R;

import java.util.ArrayList;
import java.util.List;


public class IngAddDialogFragment extends DialogFragment {
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
    private Recipe recipe;
    private Queries query;
    private ArrayList<Product> productList;

    public static final String ARG_ITEM_ID = "add_ing_dialog_fragment";

    public interface IngAddDialogFragmentListener {
        void onFinishDialog();
    }

    public IngAddDialogFragment(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ingredientDAO = new IngredientDAO(getActivity());

        Bundle bundle = this.getArguments();

        recipe = new Recipe();

        recipe = (Recipe) bundle.getParcelable("selectedRecipe");

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

        builder.setIcon(R.drawable.ic_action_new1);
        builder.setTitle(R.string.add_ingredient);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.add,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ingredient = new Ingredient();

                        for(int i = 0; i < productList.size(); i++)
                            if(productList.get(i).getId() == selectionID)
                                ingredient.setIdProduct(productList.get(i).getId());

                        ingredient.setIdRecipe(recipe.getId());
                        ingredient.setAmount(Integer.parseInt(ingAmount
                                .getText().toString()));

                        IngredientUnit unit = (IngredientUnit)adapter
                                .getItem(spinner.getSelectedItemPosition());

                        ingredient.setIngredientUnit(unit);
                        long result = ingredientDAO.save(ingredient);

                        if (result > 0) {
                            ShowRecipeActivity activity = (ShowRecipeActivity) getActivity();
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Unable to save ingredient",
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

        ingName.setText("");
        ingAmount.setText("");
        spinner.setSelection(0);
    }
}
