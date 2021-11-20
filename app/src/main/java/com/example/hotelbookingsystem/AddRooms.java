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

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRooms extends AppCompatActivity {

    ImageView view;
    EditText text,editdesc;
    Button button;

    private final int REQ=1;
    private Bitmap bitmap;
    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        button=findViewById(R.id.btnsave);
        text=findViewById(R.id.editnames);
        editdesc=findViewById(R.id.editdesc);
        view=findViewById(R.id.uimages);
        pd=new ProgressDialog(this);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getText().toString().isEmpty())
                {
                    text.setError("Empty");
                    text.requestFocus();

                }
                else if(editdesc.getText().toString().isEmpty())
                {
                    editdesc.setError("Empty");
                    editdesc.requestFocus();

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
        reference=reference.child("Notice");
        final String uniqueKey=reference.push().getKey();

        String title=text.getText().toString();
        String editdescs=editdesc.getText().toString();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yy");
        String date=dateFormat.format(calendar.getTime());

        Calendar calendar1=Calendar.getInstance();
        SimpleDateFormat dateFormat1=new SimpleDateFormat("hh:mm a");
        String time=dateFormat1.format(calendar1.getTime());

        Noticedata noticedata=new Noticedata(title,editdescs,downloadUrl,date,time,uniqueKey);

        reference.child(uniqueKey).setValue(noticedata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddRooms.this, "Dones", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddRooms.this, "Wrong 2", Toast.LENGTH_SHORT).show();
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
        filePath=storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadTask=filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddRooms.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                }
                            });
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(AddRooms.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
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

            view.setImageBitmap(bitmap);
        }
    }
}