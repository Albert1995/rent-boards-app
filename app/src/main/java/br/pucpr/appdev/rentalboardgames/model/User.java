package br.pucpr.appdev.rentalboardgames.model;

import java.util.Calendar;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String email;
    private List<Lending> lendings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Lending> getLendings() {
        return lendings;
    }

    public void setLendings(List<Lending> lendings) {
        this.lendings = lendings;
    }

    public void newLending(Boardgame boardgame, Calendar startDate, Calendar endDate, double totalRentValue) {
        for (Lending l : lendings) {
            if (l.getBoardgame() == null) {
                l.setBoardgameObj(boardgame);
                l.setStartDate(startDate.getTime());
                l.setEndDate(endDate.getTime());
                l.setTotalRentValue(totalRentValue);
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", lendings=" + lendings +
                '}';
    }
}
