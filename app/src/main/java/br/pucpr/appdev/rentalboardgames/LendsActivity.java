package br.pucpr.appdev.rentalboardgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.pucpr.appdev.rentalboardgames.controller.DetailBoardgameActivity;
import br.pucpr.appdev.rentalboardgames.model.Boardgame;
import br.pucpr.appdev.rentalboardgames.model.Lending;
import br.pucpr.appdev.rentalboardgames.model.LendingHistory;
import br.pucpr.appdev.rentalboardgames.model.Rental;
import br.pucpr.appdev.rentalboardgames.view.LendAdapter;
import br.pucpr.appdev.rentalboardgames.view.LendHistoryAdapter;

public class LendsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static LendAdapter adapter1;
    static LendHistoryAdapter adapter2;
    static CustomApplication appContext;
    static LinearLayoutManager manager1, manager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lends);

        appContext = (CustomApplication) getApplicationContext();
        manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);

        manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LendsActivity.this.finish();
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTab1() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_lends, container, false);
            int tab = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.d("BOARD-LEND", "onCreateView: " + tab);

            if (tab == 1) {
                final RecyclerView rv = rootView.findViewById(R.id.recyclerViewUserLends);
                adapter1 = new LendAdapter(appContext.getUser().getLendings());
                rv.setAdapter(adapter1);
                rv.setLayoutManager(manager1);
                final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        View view = rv.findChildViewUnder(e.getX(), e.getY());
                        final Lending l = appContext.getUser().getLendings().get(rv.getChildAdapterPosition(view));
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Devolver").setMessage("Deseja devolver o boardgame?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseFirestore.getInstance().collection("boardgames").document(l.getBoardgame()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                Boardgame b = task.getResult().toObject(Boardgame.class);
                                                b.setId(task.getResult().getId());
                                                for (Rental r : b.getRentals()) {
                                                    if (r.getUser() != null && r.getUser().equals(appContext.getUser().getId())) {
                                                        r.setEndDate(null);
                                                        r.setStartDate(null);
                                                        r.setUser(null);

                                                        FirebaseFirestore.getInstance().collection("boardgames").document(b.getId()).update(b.toMap());
                                                        break;
                                                    }
                                                }
                                            }
                                        });

                                        l.setBoardgame(null);
                                        l.setTotalRentValue(null);
                                        l.setStartDate(null);
                                        l.setEndDate(null);

                                        FirebaseFirestore.getInstance().collection("users").document(appContext.getUser().getId()).update(appContext.getUser().toMap());

                                        appContext.getUser().getLendings().remove(l);
                                        adapter1.notifyDataSetChanged();
                                    }
                                }).setNegativeButton("Não", null).show();
                        return true;
                    }
                });

                rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View view = rv.findChildViewUnder(e.getX(), e.getY());
                        return (view != null && gd.onTouchEvent(e));
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });

            } else if (tab == 2) {
                final RecyclerView rv = rootView.findViewById(R.id.recyclerViewUserLends);
                FirebaseFirestore.getInstance().collection("history").whereEqualTo("user", appContext.getUser().getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<LendingHistory> lendings = new ArrayList<>();
                            for (DocumentSnapshot d : task.getResult()) {
                                LendingHistory l = d.toObject(LendingHistory.class);
                                lendings.add(l);
                            }
                            adapter2 = new LendHistoryAdapter(lendings);
                            rv.setAdapter(adapter2);
                            rv.setLayoutManager(manager2);
                        } else {
                            Log.d("BOARD-LEND", "onComplete: " + task.getException());
                        }
                    }
                });
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
