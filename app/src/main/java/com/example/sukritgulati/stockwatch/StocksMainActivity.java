package com.example.sukritgulati.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StocksMainActivity extends AppCompatActivity implements View.OnLongClickListener,View.OnClickListener {

    private ArrayList<Stock> stockList = new ArrayList<>();
    private HashMap<String,String> symbolMap = new HashMap<>();
    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHandler databaseHandler;
    private final String DETAIL_STOCK = "http://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Swiper);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        stockAdapter = new StockAdapter(stockList,this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DatabaseHandler(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stockList.removeAll(stockList);
                getStocks();
            }
        });
        getStocks();
    }

    public void getStocks(){
        ArrayList<String[]> stock = databaseHandler.loadStocks();
        if(stock.size()==0){
            swipeRefreshLayout.setRefreshing(false);
            return;
        }else {
            StringBuffer symbolString = new StringBuffer();
            for (String[] current : stock) {
                symbolMap.put(current[0],current[1]);
                symbolString.append(current[0] + ",");
            }
            if (networkCheck()) {
                new AsyncStockDataDownloader(this,symbolMap).execute(symbolString.toString());
                swipeRefreshLayout.setRefreshing(false);
            } else {
                showDialogAlert("No Network Connection", "Stocks Cannot be updated Without network Connection", R.drawable.alert);
                swipeRefreshLayout.setRefreshing(false);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addItem:
                if(!networkCheck()){
                    showDialogAlert("No Network Connection", "Stocks Cannot be updated Without network Connection", R.drawable.alert);
                }else {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    final View view = inflater.inflate(R.layout.stockdialog, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(view);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText userInput = (EditText) view.findViewById(R.id.editText);

                            if (userInput.getText().length() != 0) {
                                new AsyncStockSymbolDownloader(StocksMainActivity.this).execute(userInput.getText().toString());
                            }
                        }
                    });
                    builder.setNegativeButton("NO WAY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateData(ArrayList<Stock> uStockList) {
        stockList.addAll(uStockList);
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateSymbol(ArrayList<String[]> cList) {
        if(cList.size()==0){
           showDialogAlert("Error","No Stock Found",R.drawable.alert);
        }else if (cList.size() == 1){
            if(stockList.contains(new Stock(cList.get(0)[0],"",0.0,0.0,0.0))){
                showDialogAlert("Duplicate Stock","Stock Symbol "+cList.get(0)[0]+" is already displayed", R.drawable.alert);
            }else{
                symbolMap.put(cList.get(0)[0],cList.get(0)[1]);
                new AsyncStockDataDownloader(this,symbolMap).execute(cList.get(0)[0]);
                databaseHandler.addStock(cList.get(0)[0],cList.get(0)[1]);
            }
        }else{
            String[] symbols = new String[cList.size()];
            for(int i = 0; i<cList.size();i++){
                symbols[i] = cList.get(i)[0] + "-"+cList.get(i)[1];
            }
            showItemDialogAlert(symbols);
        }
    }

    public void showItemDialogAlert(final String[] symbols){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        builder.setItems(symbols, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String [] inputArray =  symbols[which].split("-");
                ArrayList<String []> input = new ArrayList<String[]>();
                input.add(new String[]{inputArray[0],inputArray[1]});
                updateSymbol(input);
            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showDialogAlert(String title,String message, int drawable){
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(drawable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public boolean networkCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);

        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete " + stockList.get(pos).getStockSymbol() +" ?" );
        builder.setTitle("Delete?");
        builder.setIcon(R.drawable.alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                int row = databaseHandler.deleteStock(stockList.get(pos).getStockSymbol());
                if(row>0) {
                    symbolMap.remove(stockList.get(pos).getStockSymbol());
                    stockList.remove(pos);
                    stockAdapter.notifyDataSetChanged();
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        String url = DETAIL_STOCK + stockList.get(pos).getStockSymbol();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
