package br.pucpr.appdev.rentalboardgames.model;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String id;
    private String name;
    private String email;
    private List<Lending> lendings;

    public User() {}

    public User(DocumentSnapshot document) {
        this.id = document.getId();
        this.name = document.getString("name");
        this.email = document.getString("email");
        this.lendings = new ArrayList<>();

        if (document.get("lendings") != null) {
            for (Map<String, Object> lendMap : (List<Map<String, Object>>) document.get("lendings")) {
                Log.d("MODEL USER", "User: " + lendMap);
                Lending l = new Lending();

                Timestamp start = ((Timestamp) lendMap.get("startDate"));
                l.setStartDate(start != null ? start.toDate() : null);

                Timestamp end = ((Timestamp) lendMap.get("endDate"));
                l.setEndDate(end != null ? end.toDate() : null);

                l.setBoardgame((String) lendMap.get("boardgame"));
                l.setTotalRentValue(lendMap.get("totalRentValue") instanceof Long ? (Long) lendMap.get("totalRentValue") : (Double) lendMap.get("totalRentValue"));
                lendings.add(l);
            }
        }

    }

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

    public void newLending(Boardgame boardgame, Date startDate, Date endDate, double totalRentValue) {
        Log.d("USER", "newLending: " + lendings);
        for (Lending l : lendings) {
            if (l.getBoardgame() == null) {
                l.setBoardgame(boardgame.getId());
                l.setStartDate(startDate);
                l.setEndDate(endDate);
                l.setTotalRentValue(totalRentValue);
                break;
            }
        }
    }


    public boolean isLendingBoardgame(Boardgame boardgame) {
        for (Lending l : lendings) {
            if (boardgame.getId().equals(l.getBoardgame()))
                return true;
        }
        return false;
    }

    public boolean hasLending() {
        for (Lending l : lendings) {
            if (l.getBoardgame() == null)
                return true;
        }
        return false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("email", email);

        List<Map<String, Object>> lendingMap = new ArrayList<>();
        for (Lending l : lendings) {
            lendingMap.add(l.toMap());
        }
        map.put("lendings", lendingMap);

        return map;
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
