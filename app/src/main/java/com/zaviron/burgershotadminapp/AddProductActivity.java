package com.zaviron.burgershotadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreKtxRegistrar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.FirebaseStorageKtxRegistrar;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zaviron.burgershotadminapp.model.Product;

import java.util.ArrayList;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    public static  final String TAG =AddProductActivity.class.getName();
    private String selected_item;


    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Uri imagePath;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore =FirebaseFirestore.getInstance();
        storage =FirebaseStorage.getInstance();



        setContentView(R.layout.activity_add_product);

        Spinner spinner =findViewById(R.id.spinner);

        imageButton =findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(Intent.createChooser(intent,"Select Product Image"));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item =parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
             selected_item ="";
            }
        });

        findViewById(R.id.addProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText  editTextName =findViewById(R.id.itemName);
                EditText editTextQty =findViewById(R.id.qty);
                EditText editTextDesc =findViewById(R.id.description);
                EditText editTextPrice =findViewById(R.id.price);

                String name =editTextName.getText().toString();
                int quantity = Integer.parseInt(editTextQty.getText().toString());
                String description =editTextDesc.getText().toString();
                String price =editTextPrice.getText().toString();
                String id = UUID.randomUUID().toString();
                ProgressBar progressBar =findViewById(R.id.progressBar);




                if (name.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Please Enter Product Name",Toast.LENGTH_LONG).show();
                } else if (quantity==0) {
                    Toast.makeText(getApplicationContext(),"Please Enter Product Quantity",Toast.LENGTH_LONG).show();
                } else if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Enter Product Description",Toast.LENGTH_LONG).show();
                } else if (price.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Enter Price ",Toast.LENGTH_LONG).show();
                } else if (selected_item.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Enter Product Category",Toast.LENGTH_LONG).show();
             } else if (imagePath==null) {
                    Toast.makeText(getApplicationContext(),"Please Enter Product Image",Toast.LENGTH_LONG).show();
                }else {
                    if (id !=null){
                        Product product =new Product(id,name,quantity,description,price,selected_item,id,true);
                        firestore.collection("items").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                StorageReference reference = storage.getReference("product-images").child(id);

                                reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        RestartActivity();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        int progress =(int) (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBar.setProgress(progress);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Product Adding Process Failed Try Again",Toast.LENGTH_LONG).show();
                            }
                        });
                    }




                }


            }
        });


        ArrayList<String> arrayList =new ArrayList<>();
        arrayList.add("burger");
        arrayList.add("pizza");
        arrayList.add("kottu");

        ArrayAdapter<String> adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

    }
    ActivityResultLauncher<Intent> activityResultLauncher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode()== Activity.RESULT_OK){
                imagePath =result.getData().getData();
                Picasso.get().load(imagePath).fit()
                        .centerCrop()
                        .into(imageButton);

            }else {

                        Toast.makeText(getApplicationContext(),"Please Select Product Image",Toast.LENGTH_LONG).show();

            }
        }
    });

    public  void RestartActivity(){
        finish();
        startActivity(getIntent());
        overridePendingTransition(0,1);
    }
}