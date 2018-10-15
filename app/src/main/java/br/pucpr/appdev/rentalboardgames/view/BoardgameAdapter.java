package br.pucpr.appdev.rentalboardgames.view;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;

public class BoardgameAdapter extends FirestoreRecyclerAdapter<Boardgame, BoardgameAdapter.BoardgameHolder> {

    private static final String TAG = "BOARD-ADAPTER";
    private List<Boardgame> boardgames;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BoardgameAdapter(FirestoreRecyclerOptions<Boardgame> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(BoardgameHolder holder, int position, Boardgame model) {
        Log.d(TAG, "onBindViewHolder: " + model.toString());
        holder.lblTitle.setText(model.getName());
        holder.lblRentValue.setText(String.format("R$ %.2f", model.getRentPrice()));

        int countToRent = model.avalibleToRent();
        if (countToRent > 0) {
            View v = holder.itemView;
            holder.lblAvalible.setText(countToRent == 1 ? v.getResources().getString(R.string.unique_avalibles) : countToRent + " " + v.getResources().getString(R.string.avalibles));
        } else {
            holder.lblAvalible.setText(holder.itemView.getResources().getString(R.string.unavalible));
        }

        StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageURL());
        Glide.with(holder.itemView.getContext()).using(new FirebaseImageLoader()).load(image).into(holder.imgBoardgame);
    }

    public String getModelId(int position) {
        DocumentSnapshot snap = getSnapshots().getSnapshot(position);
        return snap.getId();
    }

    @Override
    public BoardgameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boardgame_item, parent, false);
        return new BoardgameHolder(view);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        super.onError(e);
    }

    class BoardgameHolder extends RecyclerView.ViewHolder {

        TextView lblTitle;
        TextView lblRentValue;
        ImageView imgBoardgame;
        TextView lblAvalible;


        BoardgameHolder(View itemView) {
            super(itemView);

            imgBoardgame = itemView.findViewById(R.id.imgBoardgame);
            lblTitle = itemView.findViewById(R.id.lblTitle);
            lblRentValue = itemView.findViewById(R.id.lblRentValue);
            lblAvalible = itemView.findViewById(R.id.lblAvalible);
        }
    }
}
