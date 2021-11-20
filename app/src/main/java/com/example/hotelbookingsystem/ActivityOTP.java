package com.example.hotelbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ActivityOTP extends AppCompatActivity {
    String verficationCodebysystem;
    EditText Phoneedit, otp;
    String emails, Passwords, Name, Address, st, dateedits, dropdowns, userss;
    Button Submit;
    FirebaseAuth auth;
    ProgressBar bar;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Phoneedit = findViewById(R.id.emailedit);
        otp = findViewById(R.id.otp);
        Submit = findViewById(R.id.Submit);

        auth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.bar);
        bar.setVisibility(View.VISIBLE);

        Phoneedit.setEnabled(false);

        emails = getIntent().getExtras().getString("emails");
        Passwords = getIntent().getExtras().getString("Passwords");
        Name = getIntent().getExtras().getString("Name");
        Address = getIntent().getExtras().getString("Address");
        st = getIntent().getExtras().getString("phone");
        dateedits = getIntent().getExtras().getString("dateedits");
        dropdowns = getIntent().getExtras().getString("dropdowns");
        userss = getIntent().getExtras().getString("userss");
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



        Phoneedit.setText(String.format("+91%s",st));

        sendverficationcodetouser(st);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otp.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Wrong Otp.");
                    otp.requestFocus();
                    return;
                }
                bar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });

    }


    private void sendverficationcodetouser(String st) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(String.format("+91%s",st))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verficationCodebysystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                bar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode(String codebyuser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationCodebysystem, codebyuser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                            userid = auth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userid);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", Name);
                            user.put("email", emails);
                            user.put("address", Address);
                            user.put("phone", st);
                            user.put("dateedits", dateedits);
                            user.put("dropdowns", dropdowns);
                            user.put("password", Passwords);
                            user.put("usertype", userss);


                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                else

                {
                    otp.setError("Incorrect OTP");
                }

            }
        });
    }
}