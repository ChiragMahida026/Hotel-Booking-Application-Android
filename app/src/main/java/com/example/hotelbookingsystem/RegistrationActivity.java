package com.example.hotelbookingsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, address, phone, password, dateedit;
    DatePickerDialog picker;
    Button register;
    TextView login;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid, isAddressValid, isdateValid, isdropValid;
    TextInputLayout nameError, emailError, phoneError, passError, addressError, dateError, dropError;
    AutoCompleteTextView dropdown;
    ProgressBar progressbar;
    String userid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth fauth;
    FirebaseFirestore fstore;

    String UserType = "cust";

//    String demo = "[7-9]{1}-[0-9]{9}";

    String[] COUNTRIES = new String[]{"Surat", "Bardoli"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name =  findViewById(R.id.name);
        email = findViewById(R.id.email);
        address =  findViewById(R.id.address);
        phone =  findViewById(R.id.phone);
        password = findViewById(R.id.password);
        login =  findViewById(R.id.login);
        dateedit =  findViewById(R.id.dateedit);
        register =  findViewById(R.id.register);
        progressbar =  findViewById(R.id.progressbar);
        nameError =  findViewById(R.id.nameError);
        emailError =  findViewById(R.id.emailError);
        phoneError =  findViewById(R.id.phoneError);
        passError =  findViewById(R.id.passError);
        addressError =  findViewById(R.id.AddressError);
        dateError =  findViewById(R.id.dateError);
        dropError =  findViewById(R.id.dropError);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        name.setFilters(new InputFilter[]{acceptonlyAlphabetValuesnotNumbersMethod()});


        if (fauth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
        }

        dropdown = findViewById(R.id.filled_exposed_dropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, COUNTRIES);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                String item = adapterView.getItemAtPosition(i).toString();

//                Toast.makeText(getApplicationContext(),"Selected Country : " + item, Toast.LENGTH_LONG).show();

            }


        });


        dateedit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateedit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker.getDatePicker().setMinDate(Long.parseLong("1072895400000"));
                picker.show();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetValidation();

                if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && isdateValid && isdropValid && isAddressValid) {

                    String emails = email.getText().toString();
                    String Passwords = password.getText().toString();
                    String Name = name.getText().toString();
                    String Address = address.getText().toString();
                    String Phone = phone.getText().toString();
                    String dateedits = dateedit.getText().toString();
                    String dropdowns = dropdown.getText().toString();
                    String userss = UserType.toString();
                    String st = phone.getText().toString();
                    progressbar.setVisibility(View.VISIBLE);


                    fauth.createUserWithEmailAndPassword(emails,Passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressbar.setVisibility(view.GONE);
                            if(task.isSuccessful())
                            {
                                fauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())

                                        {
                                            userid = fauth.getCurrentUser().getUid();
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
                                                Toast.makeText(getApplicationContext(), "Registration Success,Please Check Your Email For Verification.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                        else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                 }
                                });


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


//                    Toast.makeText(getApplicationContext(), "jejffjf", Toast.LENGTH_SHORT).show();



//                    fauth.createUserWithEmailAndPassword(emails,Passwords)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Toast.makeText(getApplicationContext(), "demosss", Toast.LENGTH_SHORT).show();
//                                    if(task.isSuccessful()) {
//
//
//                                        userid = fauth.getCurrentUser().getUid();
//                                        DocumentReference documentReference = fstore.collection("users").document(userid);
//                                        Map<String, Object> user = new HashMap<>();
//                                        user.put("name", Name);
//                                        user.put("email", emails);
////                                        user.put("address", Address);
////                                        user.put("phone", st);
////                                        user.put("dateedits", dateedits);
////                                        user.put("dropdowns", dropdowns);
////                                        user.put("password", Passwords);
////                                        user.put("usertype", userss);
//                                        Toast.makeText(getApplicationContext(), "Successsssss", Toast.LENGTH_SHORT).show();
//                                        //documentReference.set(user);
//
////                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
////                                            @Override
////                                            public void onComplete(@NonNull Task<Void> task) {
////                                                if(task.isSuccessful())
////                                                {
////                                                    runOnUiThread(new Runnable() {
////                                                        @Override
////                                                        public void run() {
////                                                            Toast.makeText(getApplicationContext(), "User Created. Please check Email", Toast.LENGTH_SHORT).show();
////                                                        }
////                                                    });
////
////                                                }
////                                                else {
////                                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG);
////                                                }
////                                            }
////                                        });
////                                    }
//                                    }
//                                    else{
//                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG);
//                                    }
//                                }
//                            });
//                    Toast.makeText(getApplicationContext(), "completeddemos", Toast.LENGTH_SHORT).show();


//                    Intent intent=new Intent(getApplicationContext(),ActivityOTP.class);

//                    intent.putExtra("emails", emails);
//                    intent.putExtra("Passwords", Passwords);
//                    intent.putExtra("Name", Name);
//                    intent.putExtra("Address", Address);
//                    intent.putExtra("phone", st);
//                    intent.putExtra("dateedits", dateedits);
//                    intent.putExtra("dropdowns", dropdowns);
//                    intent.putExtra("userss", userss);
//                   startActivity(intent);

                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });



    }

    public static InputFilter acceptonlyAlphabetValuesnotNumbersMethod() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean isCheck = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) {
                        sb.append(c);
                    } else {
                        isCheck = false;
                    }
                }
                if (isCheck)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString spannableString = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, spannableString, 0);
                        return spannableString;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
                Matcher match = pattern.matcher(String.valueOf(c));
                return match.matches();
            }
        };
    }

    private void SetValidation () {

        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            nameError.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else {
            isNameValid = true;
            nameError.setErrorEnabled(false);
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }  else {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }
        //Check for a valid address.
        if (address.getText().toString().isEmpty()) {
            addressError.setError(getResources().getString(R.string.address_error));
            isAddressValid = false;
        } else {
            isAddressValid = true;
            addressError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (phone.getText().toString().isEmpty()) {
            if (phone.getText().toString().trim().length() == 10) {
                phoneError.setError(getResources().getString(R.string.phone_error));
                isPhoneValid = false;
            }
        }
        else if(!Pattern.matches("^[6-9]\\d{9}$",phone.getText().toString()))
        {
            phoneError.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;

        }
//        else if (!Patterns.PHONE.matcher(phone.getText().toString()).matches()) {
//            phoneError.setError(getResources().getString(R.string.phone_error));
//            isEmailValid = false;
//        }
        else {
            isPhoneValid = true;
            phoneError.setErrorEnabled(false);
        }


        //Check for a valid date.
        if (dateedit.getText().toString().isEmpty()) {
            dateError.setError(getResources().getString(R.string.date_error));
            isdateValid = false;

        } else {
            isdateValid = true;
            dateError.setErrorEnabled(false);
        }

        //Check for a valid input dropdown
        if (dropdown.getText().toString().isEmpty()) {
            dropError.setError(getResources().getString(R.string.drop_error));
            isdropValid = false;
        } else {
            isdropValid = true;
            dropError.setErrorEnabled(false);
        }


        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

//        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && isdateValid && isdropValid && isAddressValid) {
//            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
//        }


    }
}