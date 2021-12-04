package com.example.hotelbookingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
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

public class Addcustomerdetailsatreceptionst extends AppCompatActivity {

    EditText namess, emailss, addressss, phoness, dateeditss;
    DatePickerDialog picker;
    Button registerss;

    boolean isNameValid, isEmailValid, isPhoneValid, isAddressValid, isdateValid, isdropValid;
    TextInputLayout nameError, emailError, phoneError, passError, addressError, dateError, dropError;
    AutoCompleteTextView dropdown;
    ProgressBar progressbarss;
    String userid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth fauth;
    FirebaseFirestore fstore;

    String UserType = "cust";

    String[] COUNTRIES = new String[]{"Surat", "Bardoli"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomerdetailsatreceptionst);

        namess =  findViewById(R.id.namess);
        emailss = findViewById(R.id.emailss);
        addressss =  findViewById(R.id.addressss);
        phoness =  findViewById(R.id.phoness);
        dateeditss =  findViewById(R.id.dateeditss);
        registerss =  findViewById(R.id.registerss);
        progressbarss =  findViewById(R.id.progressbarss);
        nameError =  findViewById(R.id.nameError);
        emailError =  findViewById(R.id.emailError);
        phoneError =  findViewById(R.id.phoneError);
        passError =  findViewById(R.id.passError);
        addressError =  findViewById(R.id.AddressError);
        dateError =  findViewById(R.id.dateError);
        dropError =  findViewById(R.id.dropError);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        namess.setFilters(new InputFilter[]{acceptonlyAlphabetValuesnotNumbersMethod()});

        dropdown = findViewById(R.id.filled_exposed_dropdownss);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, COUNTRIES);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }


        });

        dateeditss.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(Addcustomerdetailsatreceptionst.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateeditss.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker.getDatePicker().setMinDate(Long.parseLong("1072895400000"));
                picker.show();
            }
        });

        registerss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetValidation();



                if (isNameValid && isEmailValid && isPhoneValid  && isdateValid && isdropValid && isAddressValid) {


                    String emails = emailss.getText().toString();
                    String Name = namess.getText().toString();
                    String Address = addressss.getText().toString();
                    String Phone = phoness.getText().toString();
                    String dateedits = dateeditss.getText().toString();
                    String dropdowns = dropdown.getText().toString();
                    String userss = UserType.toString();
                    String st = phoness.getText().toString();
                    progressbarss.setVisibility(View.VISIBLE);

                    Map<String, Object> user = new HashMap<>();

                    user.put("name", Name);
                    user.put("email", emails);
                    user.put("address", Address);
                    user.put("phone", st);
                    user.put("dateedits", dateedits);
                    user.put("dropdowns", dropdowns);
                    user.put("usertype", userss);



                    fstore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Customer Added Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),ActivityreceptionistBoard.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                            }
                        }
                    });
    }

    private void SetValidation() {

        if (namess.getText().toString().isEmpty()) {
            nameError.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else {
            isNameValid = true;
            nameError.setErrorEnabled(false);
        }

        // Check for a valid email address.
        if (emailss.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailss.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }  else {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }
        //Check for a valid address.
        if (addressss.getText().toString().isEmpty()) {
            addressError.setError(getResources().getString(R.string.address_error));
            isAddressValid = false;
        } else {
            isAddressValid = true;
            addressError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (phoness.getText().toString().isEmpty()) {
            if (phoness.getText().toString().trim().length() == 10) {
                phoneError.setError(getResources().getString(R.string.phone_error));
                isPhoneValid = false;
            }
        }
        else if(!Pattern.matches("^[6-9]\\d{9}$",phoness.getText().toString()))
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
        if (dateeditss.getText().toString().isEmpty()) {
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

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to go Dashboard?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      Intent i=new Intent(getApplicationContext(),ActivityreceptionistBoard.class);
                      startActivity(i);
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
}
