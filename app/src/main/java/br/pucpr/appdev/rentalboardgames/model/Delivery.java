package br.pucpr.appdev.rentalboardgames.model;

public class Delivery {

    private String id;
    private String type;
    private double price;
    private boolean needAddress;

    public Delivery() {}

    public Delivery(String id, String type, double price, boolean needAddress) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.needAddress = needAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isNeedAddress() {
        return needAddress;
    }

    public void setNeedAddress(boolean needAddress) {
        this.needAddress = needAddress;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", needAddress=" + needAddress +
                '}';
    }
}
