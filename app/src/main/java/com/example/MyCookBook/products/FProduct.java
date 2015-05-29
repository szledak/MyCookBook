package com.example.MyCookBook.products;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.MyCookBook.adapter.ProdListAdapter;
import com.example.MyCookBook.database.ProductDAO;
import com.example.MyCookBook.products.ProdUpdateDialogFragment;
import com.example.MyCookBook.products.Product;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;

import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2015-05-13.
 */
public class FProduct  extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private Activity activity;
    private ListView productListView;
    private ArrayList<Product> products;
    private ProdListAdapter productListAdapter;
    private ProductDAO productDAO;
    private GetProdTask task;
    private String strQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        strQuery = getArguments().getString("query");
        productDAO = new ProductDAO(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container,
                false);
        findViewsById(view);

        task = new GetProdTask(activity);
        task.execute((Void) null);

        productListView.setOnItemClickListener(this);
        productListView.setOnItemLongClickListener(this);

        return view;
    }

    private void findViewsById(View view) {
        productListView = (ListView) view.findViewById(R.id.list_prod);
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position,
                            long id) {
        Product product = (Product) list.getItemAtPosition(position);

        if (product != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedProduct", product);
            ProdUpdateDialogFragment prodUpdateDialogFragment = new ProdUpdateDialogFragment();
            prodUpdateDialogFragment.setArguments(arguments);
            prodUpdateDialogFragment.show(getFragmentManager(),
                    ProdUpdateDialogFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        final  Product product = (Product) parent.getItemAtPosition(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Usuwanie");
        alertDialog.setMessage("Usunąć produkt: '" + product.getName() + "' ?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_action_discard);
        alertDialog.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        // Use AsyncTask to delete from database
                        productDAO.deleteProduct(product);
                        productListAdapter.remove(product);
                        Toast.makeText(activity, "Usunięto", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialog.setNegativeButton("Nie", null);
        alertDialog.show();

        return true;
    }

    public class GetProdTask extends AsyncTask<Void, Void, ArrayList<Product>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetProdTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<Product> doInBackground(Void... arg0) {
            ArrayList<Product> productList = productDAO.getProducts(strQuery);
            return productList;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> prodList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                products = prodList;
                if (prodList != null) {
                    if (prodList.size() != 0) {
                        productListAdapter = new ProdListAdapter(activity,
                                prodList);
                        productListView.setAdapter(productListAdapter);
                    } else {
                        Toast.makeText(activity, "No Product Records",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }


    }

    /*
     * This method is invoked from MainActivity onFinishDialog() method. It is
     * called from ProdUpdateDialogFragment when an product record is updated.
     * This is used for communicating between fragments.
     */
    public void updateView() {
        task = new GetProdTask(activity);
        task.execute((Void) null);
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.products);
        getActivity().getActionBar().setTitle(R.string.products);
        super.onResume();
    }
}
