package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;

public class DetailBoardgameActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Boardgame boardgame;
    ImageView img;
    TextView title;
    TextView description;
    TextView rentPrice;
    TextView age;
    TextView playingTime;
    TextView players;
    TextView amount;
    Button btnRent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_boardgame);

        img = findViewById(R.id.imgBoardgameDetail);
        title = findViewById(R.id.lblTitleDetail);
        description = findViewById(R.id.lblDescriptionDetail);
        rentPrice = findViewById(R.id.lblRentValueDetail);
        age = findViewById(R.id.lblAgeDetail);
        playingTime = findViewById(R.id.lblPlayingTimeDetail);
        players = findViewById(R.id.lblPlayersDetail);
        amount = findViewById(R.id.lblAmountAvalibleDetail);
        btnRent = findViewById(R.id.btnRent);

        setTitle("Detalhe do Boardgame");

        db = FirebaseFirestore.getInstance();

        db.collection("boardgames")
                .document(this.getIntent().getStringExtra("boardgameId"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boardgame = task.getResult().toObject(Boardgame.class);
                boardgame.setId(task.getResult().getId());

                StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(boardgame.getImageURL());
                Glide.with(DetailBoardgameActivity.this).using(new FirebaseImageLoader()).load(image).into(img);

                title.setText(boardgame.getName());
                description.setText(boardgame.getDescription());
                rentPrice.setText(String.format("R$ %.2f", boardgame.getRentPrice()));
                age.setText(String.format("%d+", boardgame.getAge()));
                amount.setText(String.format("%d unidade(s)", boardgame.avalibleToRent()));
                if (boardgame.getPlayingTimeMax() == null || boardgame.getPlayingTimeMax() == boardgame.getPlayingTimeMin())
                    playingTime.setText(String.format("%d %s", boardgame.getPlayingTimeMin(), DetailBoardgameActivity.this.getResources().getString(R.string.minutes)));
                else
                    playingTime.setText(String.format("%d - %d %s", boardgame.getPlayingTimeMin(), boardgame.getPlayingTimeMax(), DetailBoardgameActivity.this.getResources().getString(R.string.minutes)));

                if (boardgame.getPlayersMax() == null || boardgame.getPlayersMax() == boardgame.getPlayersMin())
                    players.setText(String.format("%d", boardgame.getPlayersMin()));
                else
                    players.setText(String.format("%d - %d", boardgame.getPlayersMin(), boardgame.getPlayersMax()));

                btnRent.setVisibility(boardgame.avalibleToRent() > 0 ? View.VISIBLE : View.GONE);

            }
        });

    }

    public void btnRentOnClick(View v) {
        Intent i = new Intent(this, CheckoutRentActivity.class);
        i.putExtra("boardgame", boardgame);
        startActivity(i);
    }
}
