package br.pucpr.appdev.rentalboardgames.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class Lending {

    private Boardgame boardgame;
    private Date startDate;
    private Date endDate;
    private Double totalRentValue;

    public Boardgame getBoardgame() {
        return boardgame;
    }

    public void setBoardgameObj(Boardgame boardgame) {
        this.boardgame = boardgame;
    }

    public void setBoardgame(DocumentReference reference) {
        if (reference != null) {
            Log.d("MODEL", "setBoardgame: " + reference.getPath());
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                        boardgame = task.getResult().toObject(Boardgame.class);
                    else
                        Log.d("MODEL", "onComplete: " + task.getException());
                }
            });
        }
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

    public Double getTotalRentValue() {
        return totalRentValue;
    }

    public void setTotalRentValue(Double totalRentValue) {
        this.totalRentValue = totalRentValue;
    }

    @Override
    public String toString() {
        return "Lending{" +
                "boardgame=" + boardgame +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalRentValue=" + totalRentValue +
                '}';
    }
}
