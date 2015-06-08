package hyuse.how_much;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import static android.os.SystemClock.sleep;

/**
 * Created by VVl-lYSl3eaR on 2015-05-04.
 */
public class Logo extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_layout);

        Intent intent=new Intent(this,Gpsinfo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }
}
