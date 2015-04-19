package com.example.hwang_gyojun.hyu_se;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity
        implements View.OnClickListener, HomeFragment.OnFragmentInteractionListener,
                   GpsFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener,
                   RetrieveFragment.OnFragmentInteractionListener, SearchIndexFragment.OnFragmentInteractionListener {
    int current_fragment_index;
    public final static int fragment_home = 0;
    public final static int fragment_gps = 1;
    public final static int fragment_search_index = 2;
    public final static int fragment_retrieve = 3;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_home = (Button) findViewById(R.id.home);
        button_home.setOnClickListener(this);
        Button button_gps = (Button) findViewById(R.id.gps);
        button_gps.setOnClickListener(this);
        Button button_search_index = (Button) findViewById(R.id.search_index);
        button_search_index.setOnClickListener(this);
        Button button_retrieve = (Button) findViewById(R.id.retrieve);
        button_retrieve.setOnClickListener(this);

        current_fragment_index = fragment_home;

        fragmentReplace(current_fragment_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                current_fragment_index = fragment_home;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.gps:
                current_fragment_index = fragment_gps;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.search_index:
                current_fragment_index = fragment_search_index;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.retrieve:
                current_fragment_index = fragment_retrieve;
                fragmentReplace(current_fragment_index);
                break;
        }
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Fragment newFragment = null;

        newFragment = getFragment(reqNewFragmentIndex);

        // replace fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case fragment_home:
                newFragment = new HomeFragment();
                break;
            case fragment_gps:
                newFragment = new GpsFragment();
                break;
            case fragment_search_index:
                newFragment = new SearchIndexFragment();
                break;
            case fragment_retrieve:
                newFragment = new RetrieveFragment();
                break;
            default:
                break;
        }

        return newFragment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
