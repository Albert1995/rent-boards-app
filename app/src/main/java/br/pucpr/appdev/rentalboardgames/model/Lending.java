package br.pucpr.appdev.rentalboardgames.model;

import java.util.Date;

class Lending {

    private String boardgame;
    private Date startDate;
    private Date endDate;

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
}
