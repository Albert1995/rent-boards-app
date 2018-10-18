package br.pucpr.appdev.rentalboardgames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinner;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle("Configurações");

        spinner = findViewById(R.id.when_notify);
        aSwitch = findViewById(R.id.switch_notification);


        aSwitch.setChecked(true);



    }
}
