package com.example.hotelbookingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class cust_paymentmodel extends FirestoreRecyclerAdapter<getsetmethodpayment, cust_paymentmodel.PaymentHolder> {


    public cust_paymentmodel(@NonNull FirestoreRecyclerOptions<getsetmethodpayment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PaymentHolder holder, int position, @NonNull getsetmethodpayment model) {
        holder.names.setText(model.getPaymentId());
        holder.addressss.setText(model.getEmails());
        holder.phoness.setText(model.getPayment()+"₹");
        holder.emailss.setText(model.getName());
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list,
                parent, false);
        return new cust_paymentmodel.PaymentHolder(v);
    }

    class PaymentHolder extends RecyclerView.ViewHolder {
        TextView names;
        TextView addressss;
        TextView phoness;
        TextView emailss;

        public PaymentHolder(View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.text_view_titles);
            addressss = itemView.findViewById(R.id.text_view_descriptions);
            phoness = itemView.findViewById(R.id.text_view_prioritys);
            emailss = itemView.findViewById(R.id.text_view_Emails);
        }
    }
}
