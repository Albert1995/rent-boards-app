package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.view.BoardgameAdapter;

public class ListBoardgameToRentActivity extends AppCompatActivity {

    private static final String TAG = "BOARD-LIST";

    RecyclerView listRent;
    BoardgameAdapter adapter;

    FirebaseFirestore db;

    GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_boardgame_to_rent);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("boardgames");

        FirestoreRecyclerOptions<Boardgame> options = new FirestoreRecyclerOptions.Builder<Boardgame>()
                .setQuery(query, Boardgame.class)
                .build();

        listRent = findViewById(R.id.listRent);
        adapter = new BoardgameAdapter(options);
        listRent.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listRent.setLayoutManager(manager);

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View view = listRent.findChildViewUnder(e.getX(), e.getY());
                String id = adapter.getModelId(listRent.getChildAdapterPosition(view));
                Log.d(TAG, "onSingleTapConfirmed: clicado no objeto de id " + id);
                Intent i = new Intent(ListBoardgameToRentActivity.this, DetailBoardgameActivity.class);
                i.putExtra("boardgameId", id);
                startActivity(i);
                return true;
            }
        });

        listRent.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                return (view != null && gd.onTouchEvent(motionEvent));
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
