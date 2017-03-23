package com.example.sukritgulati.stockwatch;

import android.content.DialogInterface;
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

/**
 * Created by sukritgulati on 3/11/17.
 */

public class AsyncStockSymbolDownloader extends AsyncTask<String,Integer, String> {

    private StocksMainActivity mainActivity;
    private int count;
    private final String API_KEY = "34af19aebe22faf63fa7dca205b776814285cdb1";
    private final String API_String = "api_key";
    private final String Search_String = "search_text";
    private final String dataURL = "http://stocksearchapi.com/api/?";
    private static final String TAG = "AsyncStockSymbolDownloader";

    public AsyncStockSymbolDownloader(StocksMainActivity stocksMainActivity) {
        mainActivity = stocksMainActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder sb = new StringBuilder();
        try {

            Uri.Builder builder = Uri.parse(dataURL).buildUpon();

            builder.appendQueryParameter(API_String,API_KEY);
            builder.appendQueryParameter(Search_String,params[0]);
            URL url =  new URL(builder.build().toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {

            return null;
        }


        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<String[]> stockSymbolList = parseJSON(s);
        mainActivity.updateSymbol(stockSymbolList);
    }

    private ArrayList<String[]> parseJSON(String s) {
        ArrayList<String []> stockSymbolList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String stockName = jStock.getString("company_name");
                String stockSymbol = jStock.getString("company_symbol");

                stockSymbolList.add(new String[] {stockSymbol, stockName});
            }
            return stockSymbolList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockSymbolList;

    }
}