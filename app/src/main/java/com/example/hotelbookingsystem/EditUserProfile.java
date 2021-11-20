package com.example.hotelbookingsystem;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditUserProfile extends AppCompatActivity {

    EditText editname, editemail, editaddress, editphone;
    Button btnsavedata;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    ImageView profileImageView;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageuri;
    ActivityResultLauncher<String> mgetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        profileImageView = (ImageView) findViewById(R.id.uimage);
        editphone = findViewById(R.id.editphone);
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editaddress = findViewById(R.id.editaddress);
        user = fAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        btnsavedata = findViewById(R.id.btnsavedata);
        storageReference = FirebaseStorage.getInstance().getReference();


        Intent data = getIntent();
        String fullname = data.getStringExtra("name");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        String address = data.getStringExtra("address");

        editname.setText(fullname);
        editemail.setText(email);
        editphone.setText(phone);
        editaddress.setText(address);

        editemail.setEnabled(false);
        editphone.setEnabled(false);

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        mgetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    profileImageView.setImageURI(result);
                    imageuri = result;


                }
            }

        });


        btnsavedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editname.getText().toString().isEmpty() || editemail.getText().toString().isEmpty() || editaddress.getText().toString().isEmpty() || editphone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "one or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = editemail.getText().toString();
                String names = editname.getText().toString();
                String addresss = editaddress.getText().toString();
                String phones = editphone.getText().toString();

                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        DocumentReference documentReference = firestore.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("name", names);
                        edited.put("address", addresss);
                        edited.put("phone", phones);
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity2demosssss.class));
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}