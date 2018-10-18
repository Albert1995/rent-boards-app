package br.pucpr.appdev.rentalboardgames;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import br.pucpr.appdev.rentalboardgames.controller.DetailBoardgameActivity;
import br.pucpr.appdev.rentalboardgames.controller.ListBoardgameToRentActivity;
import br.pucpr.appdev.rentalboardgames.controller.MainActivity;
import br.pucpr.appdev.rentalboardgames.listeners.EndlessRecyclerOnScrollListener;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.view.BoardgameAdapter;

public class ListBoardgames extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BOARD-LIST";

    FirebaseFirestore db;
    RecyclerView listRent;
    BoardgameAdapter adapter;
    LinearLayoutManager manager;
    GestureDetector gd;
    List<Boardgame> boardgames;
    SearchView searchView;
    ListenerRegistration listenerRegistration;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_boardgames);

        db = FirebaseFirestore.getInstance();

        setUpRecyclerView();
        loadData("timestamp");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpRecyclerView() {
        listRent = findViewById(R.id.recyclerBoardgames);

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
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View view = listRent.findChildViewUnder(e.getX(), e.getY());
                String id = boardgames.get(listRent.getChildAdapterPosition(view)).getId();
                Log.d(TAG, "onSingleTapConfirmed: clicado no objeto de id " + id);
                Intent i = new Intent(ListBoardgames.this, DetailBoardgameActivity.class);
                i.putExtra("boardgameId", id);
                startActivity(i);
                return true;
            }
        });
    }

    private void loadData(String orderBy) {
        loadData(orderBy, Query.Direction.ASCENDING);
    }

    private void setUpQueryListener(Query query, final boolean reloadData) {
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            boolean first = true;
            boolean reload = reloadData;

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.getMessage());
                    return;
                }

                if (reload) {
                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        Boardgame b = change.getDocument().toObject(Boardgame.class);
                        switch (change.getType()) {
                            case ADDED:
                                if (!first)
                                    Snackbar.make(findViewById(android.R.id.content), b.getName() + " disponível para alugar!", Snackbar.LENGTH_LONG).setAction("New", null).show();
                                b.setId(change.getDocument().getId());
                                boardgames.add(b);
                                break;
                            case MODIFIED:
                                boardgames.set(boardgames.indexOf(b), b);
                                break;
                            case REMOVED:
                                boardgames.remove(b);
                                break;
                        }
                    }
                    if (adapter == null) {
                        adapter = new BoardgameAdapter(boardgames);
                        listRent.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    first = false;
                }
                reload = true;
            }
        });
    }

    private void loadData(String orderBy, Query.Direction direction) {
        if (boardgames == null) {
            boardgames = new ArrayList<>();
        } else {
            boardgames.clear();
        }

        query = db.collection("boardgames").orderBy(orderBy, direction);
        setUpQueryListener(query, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.list_boardgames, menu);

        getMenuInflater().inflate(R.menu.search_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_by) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.order_title)
                    .setItems(R.array.order_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: " + which);
                            switch(which) {
                                case 0:
                                    loadData("name");
                                    break;
                                case 1:
                                    loadData("name", Query.Direction.DESCENDING);
                                    break;
                                case 2:
                                    loadData("rentPrice");
                                    break;
                                case 3:
                                    loadData("rentPrice", Query.Direction.DESCENDING);
                                    break;
                            }
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();

            ((CustomApplication) getApplication()).setUser(null);

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            this.finish();
        } else if (id == R.id.nav_lends) {
            Intent i = new Intent(this, LendsActivity.class);
            startActivity(i);
        /*} else if (id == R.id.nav_my_account) {*/

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpQueryListener(query, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        listenerRegistration.remove();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}
