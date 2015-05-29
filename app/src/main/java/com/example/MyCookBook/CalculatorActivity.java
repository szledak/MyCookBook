package com.example.MyCookBook;

import android.app.ActionBar;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.*;
import android.widget.*;
import com.example.MyCookBook.adapter.CalcListAdapter;
import com.example.MyCookBook.adapter.ProdListAdapter;
import com.example.MyCookBook.calculator.CalcProdUpdateDialogFragment;
import com.example.MyCookBook.calculator.CalcProduct;
import com.example.MyCookBook.database.*;
import com.example.MyCookBook.products.Product;
import info.androidhive.actionbar.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CalculatorActivity extends Activity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, View.OnClickListener {

    private ListView listView;
    private ArrayList<CalcProduct> calcProducts;
    private CalcListAdapter calcListAdapter;
    private CalcProductDAO calcProductDAO;
    private Queries query;
    private GetCalcTask task;
    private ProductDAO productDAO;
    private ProdListAdapter adapter;
    private AutoCompleteTextView textView;
    private EditText editText, editText2;
    private ArrayList<Product> productList;
    private ActionBar actionBar;
    private Button button;
    private String str;
    private String selectionName;
    private CalcProduct calcProduct;
    private ImageView imageView;
    private int selectionKcal;
    private double sum = 0;
    private  TextView textViewSum, textView2;

    public static final String ARG_ITEM_ID = "calc_list";

    public CalculatorActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_calculator);

        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable( getResources().getColor(R.color.action_bar_color)));

        productDAO = new ProductDAO(this);
        calcProductDAO = new CalcProductDAO(this);
        query = new Queries();
        calcProduct = new CalcProduct();

        task = new GetCalcTask(this);
        task.execute((Void) null);

        textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_product);
        editText = (EditText) findViewById(R.id.editText2);
        textViewSum = (TextView)findViewById(R.id.textView6);
        textView2 = (TextView)findViewById(R.id.textView3);

        imageView = (ImageView)findViewById(R.id.im_view_add);
        imageView.setOnClickListener(CalculatorActivity.this);

        listView = (ListView)findViewById(R.id.listVCalc);

        productList = productDAO.getProducts(query.getAllProducts());
        calcProducts = calcProductDAO.getCalcProduct(query.getCalcProducts());

        textView.setThreshold(1);
        textView.setAdapter(new ProdListAdapter(this, productList));

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                Product selection = (Product)parent.getItemAtPosition(position);
                selectionName = selection.getName();
                selectionKcal = selection.getKcal();
                textView2.setText(String.valueOf(selectionKcal));
            }
        });

        for(int k = 0; k < calcProducts.size(); k++)
          for(int i = 0; i < productList.size(); i++){
             if((productList.get(i).getName()).equals(calcProducts.get(k).getName()))
                 sum+=(productList.get(i).getKcal()*calcProducts.get(k).getAmount())/100;
          }

        textViewSum.setText(String.valueOf(sum) + " [kcal]");

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void imageViewOnClick(){

        int eTxt = editText.getText().length();
        if(selectionName == null || eTxt <=0){
            Toast.makeText(CalculatorActivity.this,
                    "Uzupełnij puste pola",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            calcProduct.setName(selectionName);
            calcProduct.setAmount(Integer.parseInt(editText.getText().toString()));

            sum += (selectionKcal*calcProduct.getAmount())/100;

            textViewSum.setText(String.valueOf(sum) + " [kcal]");

            long result = calcProductDAO.save(calcProduct);
            if (result > 0) {
                editText.setText("");
                textView.setText("");
                textView2.setText("");

                CalculatorActivity activity= CalculatorActivity.this;
                activity.onFinishDialog();
            } else {
                Toast.makeText(CalculatorActivity.this,
                        "Unable to save product",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteAllCalcProducts(){

        if(calcProducts.size() != 0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Usuwanie");
            alertDialog.setMessage("Usunąć wszystkie produkty?");

            alertDialog.setIcon(R.drawable.ic_action_remove);
            alertDialog.setPositiveButton("Tak",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            long result = calcProductDAO.deleteAll();

                            if (result > 0) {
                                calcListAdapter.clear();
                                calcListAdapter.notifyDataSetChanged();
                                textViewSum.setText("0 [kcal]");
                                textView2.setText("");
                                sum=0;
                                CalculatorActivity activity= CalculatorActivity.this;
                                activity.onFinishDialog();

                            } else {
                                Toast.makeText(CalculatorActivity.this,
                                        "Unable to delete all products",
                                        Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(CalculatorActivity.this, "Usunięto", Toast.LENGTH_SHORT).show();
                        }
                    });

            alertDialog.setNegativeButton("Nie", null);
            alertDialog.show();
        }else{
            Toast.makeText(CalculatorActivity.this,
                    "Brak produktów do usunięcia",
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.im_view_add:
                imageViewOnClick();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position,
                            long id) {
        CalcProduct calcProduct = (CalcProduct) list.getItemAtPosition(position);

        if (calcProduct != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("selectedProduct", calcProduct);
            CalcProdUpdateDialogFragment calcProdUpdateDialogFragment = new CalcProdUpdateDialogFragment();
            calcProdUpdateDialogFragment.setArguments(arguments);
            calcProdUpdateDialogFragment.show(getFragmentManager(),
                    calcProdUpdateDialogFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        final  CalcProduct calcProduct = (CalcProduct) parent.getItemAtPosition(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Usuwanie");
        alertDialog.setMessage("Usunąć produkt: '" + calcProduct.getName() + "' ?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_action_discard);
        alertDialog.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        // Use AsyncTask to delete from database
                        sum -= (selectionKcal*calcProduct.getAmount())/100;

                        long result = calcProductDAO.deleteCalcProduct(calcProduct);
                        calcListAdapter.remove(calcProduct);

                        if (result > 0) {
                            if(calcProducts.size() == 0){
                                textViewSum.setText("0 [kcal]");
                                sum = 0;
                            }

                            else
                                textViewSum.setText(String.valueOf(sum) + " [kcal]");
                            CalculatorActivity activity= CalculatorActivity.this;
                            activity.onFinishDialog();
                        } else {
                            Toast.makeText(CalculatorActivity.this,
                                    "Unable to delete product",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(CalculatorActivity.this, "Usunięto", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialog.setNegativeButton("Nie", null);
        alertDialog.show();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                deleteAllCalcProducts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_calc_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public class GetCalcTask extends AsyncTask<Void, Void, ArrayList<CalcProduct>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetCalcTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<CalcProduct> doInBackground(Void... arg0) {
            ArrayList<CalcProduct> calcProductList = calcProductDAO.getCalcProduct(query.getCalcProducts());
            return calcProductList;
        }

        @Override
        protected void onPostExecute(ArrayList<CalcProduct> calcProductList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                calcProducts = calcProductList;
                if (calcProductList != null) {
                    if (calcProductList.size() != 0) {
                        calcListAdapter = new CalcListAdapter(CalculatorActivity.this,
                                calcProductList);
                        listView.setAdapter(calcListAdapter);
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new GetCalcTask(this);
        task.execute((Void) null);
    }

    @Override
    public void onResume() {
        this.setTitle(R.string.calculator);
        this.getActionBar().setTitle(R.string.calculator);
        super.onResume();
    }

    //@Override
    public void onFinishDialog() {
        updateView();
    }
}