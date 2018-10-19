package br.pucpr.appdev.rentalboardgames.view;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.model.Lending;

public class LendAdapter
        extends RecyclerView.Adapter<LendAdapter.LendHolder>
         {

    private static final String TAG = "BOARD-LEND";
    private List<Lending> lendings;

    public LendAdapter(List<Lending> lendings) {
        Log.d(TAG, "LendAdapter: " + lendings);
        this.lendings = lendings;
    }

    protected void onBindViewHolder(final LendHolder holder, int position, Lending model) {
        Log.d(TAG, "onBindViewHolder: " + model.toString());

        FirebaseFirestore.getInstance().collection("boardgames").document(model.getBoardgame()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                holder.lblTitle.setText(task.getResult().getString("name"));

                StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(task.getResult().getString("imageURL"));
                Glide.with(holder.itemView.getContext()).using(new FirebaseImageLoader()).load(image).into(holder.imgBoardgame);
            }
        });

        holder.lblTotalRentValue.setText(String.format("R$ %.2f", model.getTotalRentValue()));
        holder.lblStartDate.setText("Pego em " + new SimpleDateFormat("dd/MM/yyyy").format(model.getStartDate()));
        holder.lblEndDate.setText("Devolver em " + new SimpleDateFormat("dd/MM/yyyy").format(model.getEndDate()));


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
