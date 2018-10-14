package br.pucpr.appdev.rentalboardgames.model;

public class Boardgame {

    private String id;
    private String name;
    private String description;
    private int playersMin;
    private int playersMax;

    private int age;

    private int playingTimeMin;
    private int playingTimeMax;

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

    public int getPlayersMin() {
        return playersMin;
    }

    public void setPlayersMin(int playersMin) {
        this.playersMin = playersMin;
    }

    public int getPlayersMax() {
        return playersMax;
    }

    public void setPlayersMax(int playersMax) {
        this.playersMax = playersMax;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPlayingTimeMin() {
        return playingTimeMin;
    }

    public void setPlayingTimeMin(int playingTimeMin) {
        this.playingTimeMin = playingTimeMin;
    }

    public int getPlayingTimeMax() {
        return playingTimeMax;
    }

    public void setPlayingTimeMax(int playingTimeMax) {
        this.playingTimeMax = playingTimeMax;
    }
}
