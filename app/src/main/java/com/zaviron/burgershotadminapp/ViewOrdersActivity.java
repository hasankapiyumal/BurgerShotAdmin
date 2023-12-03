package com.zaviron.burgershotadminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.burgershotadminapp.adapter.OrdersAdapter;
import com.zaviron.burgershotadminapp.model.Orders;
import com.zaviron.burgershotadminapp.model.Product;

import java.util.ArrayList;

public class ViewOrdersActivity extends AppCompatActivity {
    private ArrayList<Orders> orders;
    private FirebaseFirestore firestore;
    private SearchView searchView;
    private OrdersAdapter ordersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        orders = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.orderSearch);
        searchView.clearFocus();
        RecyclerView recyclerView = findViewById(R.id.viewOrderRecycleView);
         ordersAdapter = new OrdersAdapter(orders, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ordersAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                search(text);
                return false;
            }
        });
        firestore.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange change : value.getDocumentChanges()) {
                    Orders order = change.getDocument().toObject(Orders.class);
                    System.out.println(order.getOrder_id() + " " + order.getProduct_price() + " " + order.getProduct_id());
                    switch (change.getType()) {
                        case ADDED:
                            orders.add(order);
                        case MODIFIED:
                            break;
                        case REMOVED:
                            orders.remove(order);
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }
        });
    }
    public void search(String text) {
        firestore.collection("orders").whereGreaterThanOrEqualTo("order_id", text).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                orders.clear();
                for (DocumentChange change : value.getDocumentChanges()) {
                    Orders order =change.getDocument().toObject(Orders.class);

                    switch (change.getType()) {
                        case ADDED:
                            orders.add(order);
                        case MODIFIED:
                            break;
                        case REMOVED:
                            orders.remove(order);
                    }
                }
                ordersAdapter.notifyDataSetChanged();
            }
        });
    }
}