package com.shopmetrixosa.json;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by td-engagia
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> productList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, uom;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            uom = itemView.findViewById(R.id.uom);
        }
    }

    public ProductAdapter(List<Product>productList){
        this.productList = productList;
    }


    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder myViewHolder, int i) {
        Product product = productList.get(i);
        myViewHolder.name.setText("Product Name: "+product.getName());
        myViewHolder.price.setText("Price: "+product.getPrice());
        myViewHolder.uom.setText("UOM: "+product.getUom());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
