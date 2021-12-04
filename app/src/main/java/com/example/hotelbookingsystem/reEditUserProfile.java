package com.example.hotelbookingsystem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class reEditUserProfile extends AppCompatActivity {

    EditText editname, editemail, editaddress, editphone;
    Button btnsavedata;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    ImageView profileImageView;
    FirebaseUser user;
    private Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int REQ=1;
    private ProgressDialog pd;
    String downloadUrl = "";

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_edit_user_profile);

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        profileImageView = findViewById(R.id.uimage);
        editphone = findViewById(R.id.editphone);
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editaddress = findViewById(R.id.editaddress);
        user = fAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        btnsavedata = findViewById(R.id.btnsavedata);
        storageReference = FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);


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



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                Glide.with(getApplicationContext()).load(value.get("image")).into(profileImageView);

            }
        });


        btnsavedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editemail.getText().toString().isEmpty())
                {
                    editemail.setError("Empty");
                    editemail.requestFocus();

                }
                else if(editname.getText().toString().isEmpty())
                {
                    editname.setError("Empty");
                    editname.requestFocus();

                }
                else if(editaddress.getText().toString().isEmpty())
                {
                    editaddress.setError("Empty");
                    editaddress.requestFocus();

                }
                else if(editphone.getText().toString().isEmpty())
                {
                    editphone.setError("Empty");
                    editphone.requestFocus();

                }
                else if(bitmap == null)
                {
                    uploadData();
                }
                else
                {
                    uploadImage();

                }

                }

        });

    }
    private void uploadData() {

            //downloadUrl String call here
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
                    edited.put("image",downloadUrl);
                    edited.put("address", addresss);
                    edited.put("phone", phones);
                    documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ActivityreceptionistBoard.class));
                            finish();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    private void uploadImage() {
        pd.setMessage("Uploadig......");
        pd.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filePath;
        filePath=storageReference.child("Profile").child( finalimg+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(reEditUserProfile.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    uploadData();
                                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
                else
                {

                    Toast.makeText(reEditUserProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK)
        {

            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            profileImageView.setImageBitmap(bitmap);

        }
    }

    private void openGallery() {

            Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImage,REQ);

    }



}