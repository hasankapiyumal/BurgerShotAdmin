package com.zaviron.burgershotadminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.burgershotadminapp.adapter.ProductAdapter;
import com.zaviron.burgershotadminapp.model.Product;

import java.util.ArrayList;

public class ViewProductActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        searchView = findViewById(R.id.productSearch);
        searchView.clearFocus();

        firestore = FirebaseFirestore.getInstance();
        //storage = FirebaseStorage.getInstance();
        products = new ArrayList<>();

        RecyclerView itemView = findViewById(R.id.viewProductView);
        productAdapter = new ProductAdapter(products, ViewProductActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        itemView.setLayoutManager(gridLayoutManager);
        itemView.setAdapter(productAdapter);

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
        firestore.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //  products.clear();
                for (DocumentChange change : value.getDocumentChanges()) {
                    Product product = change.getDocument().toObject(Product.class);
                    switch (change.getType()) {
                        case ADDED:
                            products.add(product);
                        case MODIFIED:
                            break;
                        case REMOVED:
                            products.remove(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
        });

    }

    public void search(String text) {
        firestore.collection("items").whereGreaterThanOrEqualTo("name", text).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                products.clear();
                for (DocumentChange change : value.getDocumentChanges()) {
                    Product product = change.getDocument().toObject(Product.class);
                    switch (change.getType()) {
                        case ADDED:
                            products.add(product);
                        case MODIFIED:
                            break;
                        case REMOVED:
                            products.remove(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
        });
    }

}
