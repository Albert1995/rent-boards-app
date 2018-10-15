package br.pucpr.appdev.rentalboardgames.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.pucpr.appdev.rentalboardgames.model.Delivery;

public class DeliverySpinAdapter extends ArrayAdapter<Delivery> {

    private List<Delivery> deliveries;
    private Context context;


    public DeliverySpinAdapter(@NonNull Context context, int resource, @NonNull List<Delivery> deliveries) {
        super(context, resource, deliveries);
        this.context = context;
        this.deliveries = deliveries;
    }

    @Override
    public int getCount() {
        return deliveries.size();
    }

    @Nullable
    @Override
    public Delivery getItem(int position) {
        return deliveries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        Delivery d = deliveries.get(position);
        label.setTextColor(Color.BLACK);
        label.setText(String.format("%s (R$ %.2f)", d.getType(), d.getPrice()));
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        Delivery d = deliveries.get(position);
        label.setTextColor(Color.BLACK);
        label.setText(String.format("%s (R$ %.2f)", d.getType(), d.getPrice()));
        return label;
    }


}
