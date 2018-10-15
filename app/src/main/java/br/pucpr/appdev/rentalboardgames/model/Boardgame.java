package br.pucpr.appdev.rentalboardgames.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boardgame implements Serializable {

    private String id;
    private String name;
    private String description;
    private Integer playersMin;
    private Integer playersMax;
    private Integer age;
    private Integer playingTimeMin;
    private Integer playingTimeMax;
    private Double rentPrice;
    private List<Rental> rentals;
    private String imageURL;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlayersMin() {
        return playersMin;
    }

    public void setPlayersMin(Integer playersMin) {
        this.playersMin = playersMin;
    }

    public Integer getPlayersMax() {
        return playersMax;
    }

    public void setPlayersMax(Integer playersMax) {
        this.playersMax = playersMax;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPlayingTimeMin() {
        return playingTimeMin;
    }

    public void setPlayingTimeMin(Integer playingTimeMin) {
        this.playingTimeMin = playingTimeMin;
    }

    public Integer getPlayingTimeMax() {
        return playingTimeMax;
    }

    public void setPlayingTimeMax(Integer playingTimeMax) {
        this.playingTimeMax = playingTimeMax;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int avalibleToRent() {
        int count = 0;
        for (Rental rental : rentals) {
            if (rental.getUser() == null)
                count++;
        }
        return count;
    }

    public int getRentAvailable() {
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getUser() == null)
                return i;
        }
        return -1;
    }

    public void newRent(User user, Date start, Date end) {
        for (Rental rental : rentals) {
            if (rental.getUser() == null) {
                rental.setUser(user.getId());
                rental.setStartDate(start);
                rental.setEndDate(end);
            }

        }
    }

    public List<Map<String, Object>> getArrayRentals() {
        List<Map<String, Object>> arr = new ArrayList<>();

        for (Rental r : rentals) {
            Map<String, Object> rentPositionUpdate = new HashMap<>();
            rentPositionUpdate.put("user", r.getUser());
            rentPositionUpdate.put("startDate", r.getStartDate());
            rentPositionUpdate.put("endDate", r.getEndDate());
            arr.add(rentPositionUpdate);
        }

        return arr;
    }

    @Override
    public String toString() {
        return "Boardgame{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", playersMin=" + playersMin +
                ", playersMax=" + playersMax +
                ", age=" + age +
                ", playingTimeMin=" + playingTimeMin +
                ", playingTimeMax=" + playingTimeMax +
                ", rentPrice=" + rentPrice +
                ", rentals=" + rentals +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
