package br.pucpr.appdev.rentalboardgames.model;

import java.util.Calendar;

public class User {

    private String id;
    private String name;
    private String email;
    private Lending lending;

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

    public Lending getLending() {
        return lending;
    }

    public void setLending(Lending lending) {
        this.lending = lending;
    }

    public void newLending(Boardgame boardgame, Calendar startDate, Calendar endDate) {
        if (lending == null)
            lending = new Lending();

        lending.setBoardgame(boardgame.getId());
        lending.setStartDate(startDate.getTime());
        lending.setEndDate(endDate.getTime());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", lending=" + lending +
                '}';
    }
}
