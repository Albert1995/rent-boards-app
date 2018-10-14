package br.pucpr.appdev.rentalboardgames.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.pucpr.appdev.rentalboardgames.model.Boardgame;

public class BoardgameAdapter extends RecyclerView.Adapter<BoardgameAdapter.BoardgameHolder> {

    private List<Boardgame> boardgames;

    @Override
    public BoardgameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BoardgameHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BoardgameHolder extends RecyclerView.ViewHolder {

        public BoardgameHolder(View itemView) {
            super(itemView);
        }
    }
}
