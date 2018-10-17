package br.pucpr.appdev.rentalboardgames.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import br.pucpr.appdev.rentalboardgames.view.BoardgameAdapter;

public class Lending {

    private Boardgame boardgame;
    private Date startDate;
    private Date endDate;
    private double totalRentValue;

    public Boardgame getBoardgame() {
        return boardgame;
    }

    public void setBoardgameObj(Boardgame boardgame) {
        this.boardgame = boardgame;
    }

    public void setBoardgame(DocumentReference reference) {
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boardgame = task.getResult().toObject(Boardgame.class);
            }
        });
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalRentValue() {
        return totalRentValue;
    }

    public void setTotalRentValue(double totalRentValue) {
        this.totalRentValue = totalRentValue;
    }
}
