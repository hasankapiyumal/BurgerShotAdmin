package com.zaviron.burgershotadminapp.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.zaviron.burgershotadminapp.R;
import com.zaviron.burgershotadminapp.model.Product;


import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> products;
    private FirebaseStorage storage;
    private Context context;
    private Uri product_uri;

    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.storage =FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(com.zaviron.burgershotadminapp.R.layout.home_item_view, parent, false);


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textTitle.setText(product.getName());
        holder.textPrice.setText(product.getPrice());
        storage.getReference("product-images/"+product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .resize(500,500)
                        .centerCrop()
                        .into(holder.image);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("ImageDownload Failed");
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle ,textPrice;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle =itemView.findViewById(com.zaviron.burgershotadminapp.R.id.itemName);
            textPrice =itemView.findViewById(com.zaviron.burgershotadminapp.R.id.itemPrice);
            image =itemView.findViewById(com.zaviron.burgershotadminapp.R.id.imageView);

        }
    }
}
