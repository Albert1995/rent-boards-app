package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.listeners.EndlessRecyclerOnScrollListener;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.view.BoardgameAdapter;

public class ListBoardgameToRentActivity extends AppCompatActivity {

    private static final String TAG = "BOARD-LIST";

    FirebaseFirestore db;
    RecyclerView listRent;
    BoardgameAdapter adapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    LinearLayoutManager manager;
    GestureDetector gd;
    List<Boardgame> boardgames;

    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_boardgame_to_rent);

        boardgames = new ArrayList<>();

        setUpDrawer();
        setUpDatabase();
        setUpRecyclerView();
        loadData();

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View view = listRent.findChildViewUnder(e.getX(), e.getY());
                String id = boardgames.get((int) listRent.getChildItemId(view)).getId();
                Log.d(TAG, "onSingleTapConfirmed: clicado no objeto de id " + id);
                Intent i = new Intent(ListBoardgameToRentActivity.this, DetailBoardgameActivity.class);
                i.putExtra("boardgameId", id);
                startActivity(i);
                return true;
            }
        });


    }

    private void setUpDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    private void loadData() {
        db.collection("boardgames").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.getMessage());
                    return;
                }

                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Boardgame b = document.toObject(Boardgame.class);
                    b.setId(document.getId());
                    boardgames.add(b);
                }
                if (adapter == null) {
                    adapter = new BoardgameAdapter(boardgames);
                    listRent.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /*db.collection("boardgames").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Boardgame b = document.toObject(Boardgame.class);
                        b.setId(document.getId());
                        boardgames.add(b);
                    }
                    adapter = new BoardgameAdapter(boardgames);
                    listRent.setAdapter(adapter);
                }
            }
        });*/
    }

    private void setUpDrawer() {
        toolbar = findViewById(R.id.toolbarList);
        drawer = findViewById(R.id.drawerMenu);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);

        setSupportActionBar(toolbar);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void setUpRecyclerView() {
        listRent = findViewById(R.id.listRent);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listRent.setLayoutManager(manager);

        listRent.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

            }
        });

        listRent.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                return (view != null && gd.onTouchEvent(motionEvent));
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_list, menu);

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: Procurando boards com nome " + query + ".........");
                Query q = db.collection("boardgames");
                FirestoreRecyclerOptions<Boardgame> optionsSearch = new FirestoreRecyclerOptions.Builder<Boardgame>()
                        .setQuery(q, Boardgame.class)
                        .build();
                BoardgameAdapter adapterSearch = new BoardgameAdapter(optionsSearch);

                listRent.setAdapter(adapterSearch);
                adapterSearch.startListening();
                adapterSearch.stopListening();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listRent.setAdapter(adapter);
                return false;
            }
        });*/

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //adapter.stopListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
