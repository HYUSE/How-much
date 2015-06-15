package hyuse.how_much;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Gpsinfo extends Activity{

    TextView tv1;
    TextView tv2;
    GetGPS gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_start);
        gps = new GetGPS(this);
        tv1 = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addr = gps.get_location();
                tv1.setText(addr);
                tv2.setText(gps.get_wido());
            }
        });
    }
}