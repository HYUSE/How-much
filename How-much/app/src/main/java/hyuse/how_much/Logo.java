package hyuse.how_much;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by VVl-lYSl3eaR on 2015-05-04.
 */
public class Logo extends Activity {
    Handler hand;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_layout);
        hand = new Handler();
        hand.postDelayed(postrun,3000);
    }
    Runnable postrun = new Runnable() {
        @Override
        public void run() {
            finish();
            overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        }
    };
    public void onBackPressed(){
        super.onBackPressed();
        hand.removeCallbacks(postrun);
    }
}