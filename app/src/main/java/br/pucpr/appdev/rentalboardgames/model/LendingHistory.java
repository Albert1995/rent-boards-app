package br.pucpr.appdev.rentalboardgames.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class LendingHistory {

    private String boardgame;
    private Date startDate;
    private Date endDate;
    private Date giveBackDate;
    private double totalRentValue;

    public String getBoardgame() {
        return boardgame;
    }

    public void setBoardgame(String boardgame) {
        this.boardgame = boardgame;
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

    public Date getGiveBackDate() {
        return giveBackDate;
    }

    public void setGiveBackDate(Date giveBackDate) {
        this.giveBackDate = giveBackDate;
    }
}
