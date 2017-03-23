package com.example.sukritgulati.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sukritgulati on 3/10/17.
 */

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private List<Stock> stockList;
    private StocksMainActivity ma;
    public StockAdapter(List<Stock> stockList, StocksMainActivity ma){
        this.stockList = stockList;
        this.ma = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockview,parent,false);
        myView.setOnClickListener(ma);
        myView.setOnLongClickListener(ma);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        StringBuffer priceChange = new StringBuffer(Double.toString(stock.getPriceChangeAmount()));
        if (priceChange.toString().contains("+") ) {
            holder.StockSymbol.setText(stock.getStockSymbol());
            holder.StockName.setText(stock.getCompanyName());
            holder.StockPrice.setText(Double.toString(stock.getLastTradePrice()));
           // priceChange.replace(0,1,"");
            String change = priceChange.toString() + "("+stock.getPriceChangePercentage()+"%)";
            holder.StockChange.setText(change);
            holder.StockChangeArrow.setText("▲");

            holder.StockName.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockSymbol.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockPrice.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockChange.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockChangeArrow.setTextColor(ma.getResources().getColor(R.color.colorGreen));
        }else if (priceChange.toString().contains("-") ){
            holder.StockSymbol.setText(stock.getStockSymbol());
            holder.StockName.setText(stock.getCompanyName());
            holder.StockPrice.setText(Double.toString(stock.getLastTradePrice()));
            //priceChange.replace(0,1,"");
            String change = priceChange.toString() + "("+stock.getPriceChangePercentage()+"%)";
            holder.StockChange.setText(change);
            holder.StockChangeArrow.setText("▼");
            holder.StockName.setTextColor(ma.getResources().getColor(R.color.colorRed));
            holder.StockSymbol.setTextColor(ma.getResources().getColor(R.color.colorRed));
            holder.StockPrice.setTextColor(ma.getResources().getColor(R.color.colorRed));
            holder.StockChange.setTextColor(ma.getResources().getColor(R.color.colorRed));
            holder.StockChangeArrow.setTextColor(ma.getResources().getColor(R.color.colorRed));
        }else {
            holder.StockSymbol.setText(stock.getStockSymbol());
            holder.StockName.setText(stock.getCompanyName());
            holder.StockPrice.setText(Double.toString(stock.getLastTradePrice()));
            // priceChange.replace(0,1,"");
            String change = priceChange.toString() + "("+stock.getPriceChangePercentage()+"%)";
            holder.StockChange.setText(change);
            holder.StockChangeArrow.setText("▲");

            holder.StockName.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockSymbol.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockPrice.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockChange.setTextColor(ma.getResources().getColor(R.color.colorGreen));
            holder.StockChangeArrow.setTextColor(ma.getResources().getColor(R.color.colorGreen));
        }

       // holder.StockChangeImage.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
