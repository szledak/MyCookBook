package com.example.MyCookBook.recipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.MyCookBook.database.IngredientDAO;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.database.Queries;
import com.example.MyCookBook.database.RecipeDAO;
import com.example.MyCookBook.ingredient.Ingredient;
import com.example.MyCookBook.products.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2015-05-28.
 */
public class SaveRecipeDialogFragment extends DialogFragment {
    public static final String ARG_ITEM_ID = "save_dialog_fragment";
    private Recipe recipe;
    private IngredientDAO ingredientDAO;
    private Queries queries;
    private ProductDAO productDAO;
    public SaveRecipeDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = this.getArguments();
        recipe = (Recipe) bundle.getParcelable("selectedRecipe");
        ingredientDAO = new IngredientDAO(getActivity());
        productDAO = new ProductDAO(getActivity());
        queries = new Queries();

        final ArrayList<Ingredient> ingList = ingredientDAO.getIngredients(queries.getIngredients(String.valueOf(recipe.getId())));
        final ArrayList<Product> prodList = productDAO.getProducts(queries.getAllProducts());

        builder.setMessage("Zapisać przepis do pliku?")
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            saveFile(ingList, prodList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SaveRecipeDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void saveFile(ArrayList<Ingredient> ingList, ArrayList<Product> prodList) throws IOException {
        try {
            File sdcard = Environment.getExternalStorageDirectory(); //Ścieżka dla karty SD
            String fileName = recipe.getName() + ".txt";
            String newLine = "\r\n";
            File file = new File(sdcard,fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-16"));

            bw.write(recipe.getName() + newLine);
            bw.newLine();
            bw.write("Składniki: " + newLine);



            for(int i = 0; i < prodList.size(); i++)
                for(int k = 0; k < ingList.size(); k++)
                if(prodList.get(i).getId() == ingList.get(k).getIdProduct()){
                   bw.write(" - " + prodList.get(i).getName() + " " + ingList.get(k).getAmount() + " [" + ingList.get(k).getUnit() + "]" + newLine);
                }


            String recipeTxt = newLine + recipe.getRecipe();

            bw.write(recipeTxt.replace(". ", "." + newLine));
            bw.close();

            Toast.makeText(getActivity(), "Zapisano " + fileName, Toast.LENGTH_SHORT).show();
            Log.d("Suceess", "Sucess");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface SaveRecipeDialogFragmentListener {
        void onFinishDialog();
    }
}
