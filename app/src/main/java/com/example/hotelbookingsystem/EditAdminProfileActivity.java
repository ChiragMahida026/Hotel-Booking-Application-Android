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

public class EditAdminProfileActivity extends AppCompatActivity {

    EditText editnamesss, editemailsss, editaddresssss, editphonesss;
    Button btnsavedatas;
    FirebaseAuth fAuths;
    FirebaseFirestore firestores;
    ImageView profileImageViews;
    FirebaseUser users;
    private Bitmap bitmaps;
    FirebaseStorage storages;
    StorageReference storageReferences;
    private final int REQs=1;
    private ProgressDialog pds;
    String downloadUrls = "";

    String userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_profile);

        editnamesss = findViewById(R.id.editnamess);
        editemailsss = findViewById(R.id.editemails);
        profileImageViews = findViewById(R.id.uimagess);
        editphonesss = findViewById(R.id.editphones);
        fAuths = FirebaseAuth.getInstance();
        firestores = FirebaseFirestore.getInstance();
        editaddresssss = findViewById(R.id.editaddresss);
        users = fAuths.getCurrentUser();
        storages = FirebaseStorage.getInstance();
        btnsavedatas = findViewById(R.id.btnsavedatas);
        storageReferences = FirebaseStorage.getInstance().getReference();
        pds=new ProgressDialog(this);

        Intent data = getIntent();
        String fullnamess = data.getStringExtra("name");
        String emailss = data.getStringExtra("email");
        String phoness = data.getStringExtra("phone");
        String addressss = data.getStringExtra("address");

        editnamesss.setText(fullnamess);
        editemailsss.setText(emailss);
        editphonesss.setText(phoness);
        editaddresssss.setText(addressss);

        editemailsss.setEnabled(false);
        editphonesss.setEnabled(false);

        profileImageViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        userIds = fAuths.getCurrentUser().getUid();
        DocumentReference documentReference = firestores.collection("users").document(userIds);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                Glide.with(getApplicationContext()).load(value.get("image")).into(profileImageViews);

            }
        });

        btnsavedatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editemailsss.getText().toString().isEmpty())
                {
                    editemailsss.setError("Empty");
                    editemailsss.requestFocus();

                }
                else if(editnamesss.getText().toString().isEmpty())
                {
                    editnamesss.setError("Empty");
                    editnamesss.requestFocus();

                }
                else if(editaddresssss.getText().toString().isEmpty())
                {
                    editaddresssss.setError("Empty");
                    editaddresssss.requestFocus();

                }
                else if(editphonesss.getText().toString().isEmpty())
                {
                    editphonesss.setError("Empty");
                    editphonesss.requestFocus();

                }
                else if(bitmaps == null)
                {
                    uploadsData();
                }
                else
                {
                    uploadsImage();

                }

            }

        });
    }

    private void uploadsImage() {

        pds.setMessage("Uploadig......");
        pds.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmaps.compress(Bitmap.CompressFormat.JPEG,0,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filePath;
        filePath=storageReferences.child("Profile").child(finalimg+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EditAdminProfileActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    downloadUrls=String.valueOf(uri);
                                    uploadsData();
                                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else
                {
                    pds.dismiss();
                    Toast.makeText(EditAdminProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQs && resultCode==RESULT_OK)
        {

            Uri uri=data.getData();
            try {
                bitmaps=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            profileImageViews.setImageBitmap(bitmaps);

        }
    }

    private void uploadsData() {
        //downloadUrl String call here
        String emailss = editemailsss.getText().toString();
        String namesss = editnamesss.getText().toString();
        String addresssss = editaddresssss.getText().toString();
        String phonesss = editphonesss.getText().toString();

        users.updateEmail(emailss).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DocumentReference documentReference = firestores.collection("users").document(users.getUid());
                Map<String, Object> edited = new HashMap<>();
                edited.put("email", emailss);
                edited.put("name", namesss);
                edited.put("image",downloadUrls);
                edited.put("address", addresssss);
                edited.put("phone", phonesss);
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ActivityAdminBoard.class));
                        finish();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pds.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQs);
    }
}