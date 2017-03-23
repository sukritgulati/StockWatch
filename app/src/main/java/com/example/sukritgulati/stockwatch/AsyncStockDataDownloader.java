package com.example.sukritgulati.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sukritgulati on 3/11/17.
 */

public class AsyncStockDataDownloader extends AsyncTask<String,Integer,String> {

    private StocksMainActivity mainActivity;
    private int count;

    private final String dataURL = "http://finance.google.com/finance/info?client=ig&q=";
    private static final String TAG = "AsyncStockDataDownloader";
    private ArrayList<Stock> stockList = new ArrayList<>();
    private HashMap<String,String> symbolMap = new HashMap<>();

    public AsyncStockDataDownloader(StocksMainActivity ma, HashMap<String,String> ss) {
        mainActivity = ma;
        symbolMap = ss;
    }


    @Override
    protected String doInBackground(String... params) {
        String urlToUse = dataURL + params[0];

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            sb.replace(0,3,"");

        } catch (Exception e) {

            return null;
        }


        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        mainActivity.updateData(stockList );
    }

    private ArrayList<Stock> parseJSON(String s) {
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String stockName = jStock.getString("t");
                Double stockPrice = jStock.getDouble("l");
                Double stockChangeAmount = jStock.getDouble("c");
                Double stockChangePercentage = jStock.getDouble("cp");
                stockList.add(new Stock(stockName,symbolMap.get(stockName),stockPrice,stockChangeAmount,stockChangePercentage));
            }
            return stockList;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return stockList;

    }
}
