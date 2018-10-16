package br.pucpr.appdev.rentalboardgames.dao;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import br.pucpr.appdev.rentalboardgames.model.Boardgame;

public class BoardgameDAO {

    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_AGE = "age";
    private static final String COL_PLAYERS_MIN = "players_min";
    private static final String COL_PLAYERS_MAX = "players_max";
    private static final String COL_PLAYING_TIME_MIN = "playing_time_min";
    private static final String COL_PLAYING_TIME_MAX = "playing_time_max";

    private static final BoardgameDAO instance = new BoardgameDAO();

    private static final String TAG = "BOARD-DAO";

    private FirebaseFirestore db;

    public static BoardgameDAO getInstance() {
        return instance;
    }

    private BoardgameDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public void populate(final List<Boardgame> list, final RecyclerView.Adapter adapter) {
        Log.d(TAG, "populate: iniciando coleta");

        db.collection("boardgames").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d(TAG, "onComplete: Board recuperado = " + doc.getData());
                        /*Boardgame b = new Boardgame();
                        b.setId(doc.getId());
                        b.setAge(doc.getLong(COL_AGE).intValue());
                        b.setDescription(doc.getString(COL_DESCRIPTION));
                        b.setName(doc.getString(COL_NAME));
                        b.setPlayersMin(doc.getLong(COL_PLAYERS_MIN).intValue());
                        b.setPlayersMax(doc.getLong(COL_PLAYERS_MAX).intValue());
                        b.setPlayingTimeMin(doc.getLong(COL_PLAYING_TIME_MIN).intValue());
                        b.setPlayingTimeMax(doc.getLong(COL_PLAYING_TIME_MAX).intValue());*/
                        Boardgame b = doc.toObject(Boardgame.class);
                        list.add(b);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onComplete: ERROR = " + task.getException());
                }
            }
        });
    }



}
