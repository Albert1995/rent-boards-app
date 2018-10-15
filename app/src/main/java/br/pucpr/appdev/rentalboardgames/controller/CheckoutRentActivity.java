package br.pucpr.appdev.rentalboardgames.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucpr.appdev.rentalboardgames.CustomApplication;
import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.model.Delivery;
import br.pucpr.appdev.rentalboardgames.model.User;
import br.pucpr.appdev.rentalboardgames.view.DeliverySpinAdapter;

public class CheckoutRentActivity extends AppCompatActivity {

    static final String TAG = "BOARD-CHECKOUT";
    List<Delivery> deliveries = new ArrayList<>();
    Boardgame boardgame;
    TextView boardgameName, rentPrice, totalRentPrice, lblAddress;
    EditText txtAddress;
    Spinner deliverySpinner;
    DeliverySpinAdapter spinAdapter;
    FirebaseFirestore db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_rent);
        setTitle("Checkout");

        user = ((CustomApplication) getApplicationContext()).getUser();
        db = FirebaseFirestore.getInstance();
        boardgame = (Boardgame) getIntent().getSerializableExtra("boardgame");
        boardgameName = findViewById(R.id.lblBoardgameCheckout);
        rentPrice = findViewById(R.id.lblRentPriceCheckout);
        totalRentPrice = findViewById(R.id.lblTotalRentPriceCheckout);
        deliverySpinner = findViewById(R.id.deliveryCheckout);
        lblAddress = findViewById(R.id.lblAddressCheckout);
        txtAddress = findViewById(R.id.txtAddressCheckout);

        boardgameName.setText(boardgame.getName());
        rentPrice.setText(String.format("%.2f", boardgame.getRentPrice()));
        totalRentPrice.setText(String.format("%.2f", boardgame.getRentPrice()));
        deliverySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Delivery d = deliveries.get(position);
                Log.d(TAG, "onItemSelected: " + d.toString());

                totalRentPrice.setText(String.format("%.2f", boardgame.getRentPrice() + d.getPrice()));
                lblAddress.setVisibility(d.isNeedAddress() ? View.VISIBLE : View.GONE);
                txtAddress.setVisibility(d.isNeedAddress() ? View.VISIBLE : View.GONE);

                if (d.isNeedAddress())
                    txtAddress.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        db.collection("deliveries").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Delivery d = doc.toObject(Delivery.class);
                                d.setId(doc.getId());
                                deliveries.add(d);
                            }
                            spinAdapter = new DeliverySpinAdapter(CheckoutRentActivity.this, android.R.layout.simple_spinner_item, deliveries);
                            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            deliverySpinner.setAdapter(spinAdapter);
                        }
                    }
                });
    }

    public void btnConfirmCheckoutRent(View v) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        endDate.add(Calendar.DAY_OF_YEAR, 7);
        int rentAvailable = boardgame.getRentAvailable();
        if (rentAvailable >= 0) {
            boardgame.newRent(user, startDate.getTime(), endDate.getTime());

            Log.d(TAG, "btnConfirmCheckoutRent: " + boardgame);
            Log.d(TAG, "btnConfirmCheckoutRent: " + user);

            Map<String, Object> boardgameFieldsUpdate = new HashMap<>();
            boardgameFieldsUpdate.put("rentals", boardgame.getArrayRentals());

            Log.d(TAG, "btnConfirmCheckoutRent: " + boardgameFieldsUpdate);
            db.collection("boardgames").document(boardgame.getId()).update(boardgameFieldsUpdate);

            Map<String, Object> userFieldsUpdate = new HashMap<>();
            userFieldsUpdate.put("lending.boardgame", boardgame.getId());
            userFieldsUpdate.put("lending.startDate", startDate.getTime());
            userFieldsUpdate.put("lending.endDate", endDate.getTime());
            Log.d(TAG, "btnConfirmCheckoutRent: " + userFieldsUpdate);
            db.collection("users").document(user.getId()).update(userFieldsUpdate);

        }
    }

    public void btnCancelCheckoutRent(View v) {
        finish();
    }


}
