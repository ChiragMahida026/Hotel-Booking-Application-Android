package com.example.hotelbookingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.CheckoutActivity;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private Button buttonConfirmOrder;
    private EditText editTextPayment;
    EditText usermail,userphone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        findViews();
        listeners();
    }

    public void findViews() {
        buttonConfirmOrder = (Button) findViewById(R.id.bt_pay);
        editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        usermail=findViewById(R.id.usermail);
        userphone=findViewById(R.id.userphone);
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userphone.setText(value.getString("phone"));
                usermail.setText(value.getString("email"));
            }
        });
//        editTextPayment.setText(CartFragment.getValues());


    }

    public void listeners() {


        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPayment.getText().toString().equals(""))
                {
                    Toast.makeText(PaymentActivity.this, "Please fill payment", Toast.LENGTH_LONG).show();
                    return;
                }
                startPayment();
            }
        });
    }

    private void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");

            String payment = editTextPayment.getText().toString();

            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();

            String namesss=userphone.getText().toString();
            String emailsss=usermail.getText().toString();

            preFill.put("email", emailsss);
            preFill.put("contact",namesss );
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        Toast.makeText(this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {

        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}