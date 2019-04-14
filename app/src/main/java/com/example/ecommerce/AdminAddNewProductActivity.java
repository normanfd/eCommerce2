package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Productname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, DownloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProdutData();
            }
        });

    }

    private void ValidateProdutData() {
        Productname = InputProductName.getText().toString();
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Product Image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Productname)){
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Add new product");
        loadingBar.setMessage("please wait, while we are adding the new product..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask UploadTask = filePath.putFile(ImageUri);
        UploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Image uploaded succesfully", Toast.LENGTH_SHORT).show();
                //get Link Image
                Task<Uri> urlTask = UploadTask.continueWithTask(new Continuation<com.google.firebase.storage.UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<com.google.firebase.storage.UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }else{
                            DownloadImageUrl =filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            DownloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "got the product Image url succcesfully...", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> ProductMap = new HashMap<>();
        ProductMap.put("pid", productRandomKey);
        ProductMap.put("date",saveCurrentDate);
        ProductMap.put("time",saveCurrentTime);
        ProductMap.put("description", Description);
        ProductMap.put("image", DownloadImageUrl);
        ProductMap.put("category", CategoryName);
        ProductMap.put("price",Price);
        ProductMap.put("productname", Productname);

        ProductRef.child(productRandomKey).updateChildren(ProductMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                        }else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //PICK IMAGE FROM GALLERY
    private void OpenGallery() {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!= null){
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
