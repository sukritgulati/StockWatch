package com.example.sukritgulati.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sukritgulati on 3/10/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView StockSymbol;
    public TextView StockName;
    public TextView StockPrice;
    public TextView StockChange;
    public TextView StockChangeArrow;


    public MyViewHolder(View itemView) {
        super(itemView);

        StockSymbol = (TextView) itemView.findViewById(R.id.StockSymbolText);
        StockName = (TextView) itemView.findViewById(R.id.StockNameText);
        StockPrice = (TextView) itemView.findViewById(R.id.StockPriceText);
        StockChange = (TextView) itemView.findViewById(R.id.StockChangeText);
        StockChangeArrow = (TextView) itemView.findViewById(R.id.StockChangeArrow);

    }
}
