package com.example.hotelbookingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register,forgetpassword;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;
    FirebaseAuth fauth;

    ProgressBar simpleProgressBar;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn);

        email =   findViewById(R.id.email);
        password =   findViewById(R.id.password);
        login =   findViewById(R.id.login);
        register =  findViewById(R.id.register);
        emailError =  findViewById(R.id.emailError);
        passError =  findViewById(R.id.passError);
        forgetpassword= findViewById(R.id.forgetpassword);

        simpleProgressBar=findViewById(R.id.simpleProgressBar);
        fauth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();





        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Reset Password ?");
                builder.setMessage("Enter your email to received reset link");
                builder.setView(resetMail);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail=resetMail.getText().toString();

                        if(!mail.isEmpty()) {
                            fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Reset link send to your email.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error ! Reset link is not sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("No", null);
                builder.create().show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();

                if (isEmailValid && isPasswordValid ) {

                        String emails = email.getText().toString();
                        String Passwords = password.getText().toString();
                        simpleProgressBar.setVisibility(View.VISIBLE);

                    fauth.signInWithEmailAndPassword(emails,Passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                if(fauth.getCurrentUser().isEmailVerified()) {
                                    checkUserAccessLevel();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Please verify Your Email", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        }) ;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void checkUserAccessLevel() {

        DocumentReference reference = firestore.collection("users").document(fauth.getCurrentUser().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if (documentSnapshot.getString("usertype").equals("admin") ){
                    Toast.makeText(LoginActivity.this, "SuccessFully Logged In as Admin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ActivityAdminBoard.class));
                    finish();
                }
                if (documentSnapshot.getString("usertype").equals("receptionist") ){
                    Toast.makeText(LoginActivity.this, "SuccessFully Logged In as receptionist", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ActivityreceptionistBoard.class));
                    finish();
                }
                if (documentSnapshot.getString("usertype").equals("cust")){
                    Toast.makeText(LoginActivity.this, "SuccessFully Logged In as customer", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }



    private void SetValidation() {

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else if (email.getText().length()<6) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

//        if (isEmailValid && isPasswordValid) {
//            //ok
//        }
    }

    public void onSuccess(AuthResult authResult) {
        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        // checkUserAccessLevel(authResult.getUser().getUid());
    }
}



