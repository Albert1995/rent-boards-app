package br.pucpr.appdev.rentalboardgames.view;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Lending;
import br.pucpr.appdev.rentalboardgames.model.LendingHistory;

public class LendHistoryAdapter
        extends RecyclerView.Adapter<LendHistoryAdapter.LendHolder>
        /*implements Filterable*/ {

    private static final String TAG = "BOARD-LEND";
    private List<LendingHistory> lendings;

    public LendHistoryAdapter(List<LendingHistory> lendings) {
        Log.d(TAG, "LendAdapter: " + lendings);
        this.lendings = lendings;
    }

    protected void onBindViewHolder(LendHolder holder, int position, LendingHistory model) {
        Log.d(TAG, "onBindViewHolder: " + model.toString());
        holder.lblTitle.setText(model.getBoardgame());
        holder.lblTotalRentValue.setText(String.format("R$ %.2f", model.getTotalRentValue()));
        holder.lblStartDate.setText("Pego em " + new SimpleDateFormat("dd/MM/yyyy").format(model.getStartDate()));
        holder.lblEndDate.setText("Devolvido em " + new SimpleDateFormat("dd/MM/yyyy").format(model.getEndDate()));

        /*StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(model.getBoardgame().getImageURL());
        Glide.with(holder.itemView.getContext()).using(new FirebaseImageLoader()).load(image).into(holder.imgBoardgame);*/
    }

    @Override
    public LendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lend_boardgame_item, parent, false);
        return new LendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LendHolder holder, int position) {
        onBindViewHolder(holder, position, lendings.get(position));
    }

    @Override
    public int getItemCount() {
        return lendings.size();
    }

    class LendHolder extends RecyclerView.ViewHolder {

        TextView lblTitle;
        TextView lblTotalRentValue;
        ImageView imgBoardgame;
        TextView lblStartDate;
        TextView lblEndDate;

        LendHolder(View itemView) {
            super(itemView);

            imgBoardgame = itemView.findViewById(R.id.imgBoardgame);
            lblTitle = itemView.findViewById(R.id.lblTitle);
            lblTotalRentValue = itemView.findViewById(R.id.lblTotalRentValue);
            lblStartDate = itemView.findViewById(R.id.lblStartDate);
            lblEndDate = itemView.findViewById(R.id.lblEndDate);
        }
    }
}
