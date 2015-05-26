package com.example.hwang_gyojun.hyu_se;

import android.support.v4.app.Fragment;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import static com.example.hwang_gyojun.hyu_se.R.id.fragment_layout;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, GpsFragment.OnFragmentInteractionListener
                                                , SeachIndexFragment.OnFragmentInteractionListener,ResultFragment.OnFragmentInteractionListener
                                                , RetrieveFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener{

    int mCurrentFragmentIndex;
    public final static int FRAGMENT_HOME = 0;
    public final static int FRAGMENT_SEARCH_INDEX = 1;
    public final static int FRAGMENT_RETRIEVE = 2;
    public final static int FRAGMENT_GPS = 3;
    private boolean keyboard_close;
    private DBOpenHelper db_open_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_home = (Button) findViewById(R.id.button_home);
        button_home.setOnClickListener(this);
        Button button_search_index = (Button) findViewById(R.id.button_search_index);
        button_search_index.setOnClickListener(this);
        Button button_retrieve = (Button) findViewById(R.id.button_retrieve);
        button_retrieve.setOnClickListener(this);
        Button button_gps = (Button) findViewById(R.id.button_gps);
        button_gps.setOnClickListener(this);

        db_open_helper = new DBOpenHelper(this);
        db_open_helper = db_open_helper.open();
        keyboard_close = false;

        mCurrentFragmentIndex = FRAGMENT_HOME;

        fragmentReplace(mCurrentFragmentIndex);
    }


    public void fragmentReplace(int reqNewFragmentIndex) {
        if(keyboard_close) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.autoCompleteTextView).getWindowToken(), 0);
            keyboard_close = false;
        }

        Fragment newFragment = null;

        newFragment = getFragment(reqNewFragmentIndex);

        // replace fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(fragment_layout, newFragment);
        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case FRAGMENT_HOME:
                newFragment = new HomeFragment();
                break;
            case FRAGMENT_SEARCH_INDEX:
                newFragment = new SeachIndexFragment();
                break;
            case FRAGMENT_RETRIEVE:
                newFragment = new RetrieveFragment();
                break;
            case FRAGMENT_GPS:
                newFragment = new GpsFragment();
                break;
        }
        return newFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_home:
                mCurrentFragmentIndex = FRAGMENT_HOME;
                fragmentReplace(mCurrentFragmentIndex);
                break;
            case R.id.button_search_index:
                mCurrentFragmentIndex = FRAGMENT_SEARCH_INDEX;
                fragmentReplace(mCurrentFragmentIndex);
                break;
            case R.id.button_retrieve:
                mCurrentFragmentIndex = FRAGMENT_RETRIEVE;
                fragmentReplace(mCurrentFragmentIndex);
                keyboard_close = true;
                break;
            case R.id.button_gps:
                mCurrentFragmentIndex = FRAGMENT_GPS;
                fragmentReplace(mCurrentFragmentIndex);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu( menu );


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
    public void onFragmentInteraction(Uri uri) {

    }
}
