package br.pucpr.appdev.rentalboardgames.dao;

import com.google.firebase.database.FirebaseDatabase;

public class BoardgameDAO {

    private static final BoardgameDAO instance = new BoardgameDAO();

    private FirebaseDatabase db;

    public static BoardgameDAO getInstance() {
        return instance;
    }

}
