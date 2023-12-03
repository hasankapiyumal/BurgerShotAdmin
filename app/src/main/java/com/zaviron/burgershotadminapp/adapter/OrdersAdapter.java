package com.zaviron.burgershotadminapp.adapter;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.zaviron.burgershotadminapp.R;
import com.zaviron.burgershotadminapp.model.Orders;


import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private ArrayList<Orders> orders;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    public OrdersAdapter(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.storage = FirebaseStorage.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println(position);
        Orders order = orders.get(position);
     //   firestore=FirebaseFirestore.getInstance();
       // storage=FirebaseStorage.getInstance();
        holder.order_id.setText("Order ID: "+order.getOrder_id().toString());
        System.out.println(order.getOrder_id()+"hello");
        holder.product_title.setText(order.getProduct_title().toString());
        holder.product_qty.setText(String.valueOf(order.getQty()).toString());
        holder.date.setText(String.valueOf(order.getDate()).toString());
        holder.total_price.setText(order.getTotal_price().toString());

        storage.getReference("product-images/"+order.getProduct_id()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Picasso.get()
                        .load(uri)
                        .resize(500, 500)
                        .centerCrop()
                        .into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Image Download Failed");
            }
        });

    }

    @Override
    public int getItemCount() {
        System.out.println(orders.size());
        return orders.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_id,date,product_title,product_qty,total_price;
        ImageView imageView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id =itemView.findViewById(R.id.order_view_order_id);
            date =itemView.findViewById(R.id.order_view_date);
            product_title =itemView.findViewById(R.id.order_vew_product_title);
            product_qty=itemView.findViewById(R.id.order_view_product_qty);
            total_price =itemView.findViewById(R.id.order_view_total_price);
            imageView =itemView.findViewById(R.id.order_image);


        }
    }
}
