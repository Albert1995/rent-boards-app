package br.pucpr.appdev.rentalboardgames;

import android.app.Application;

import br.pucpr.appdev.rentalboardgames.model.User;

public class CustomApplication extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
