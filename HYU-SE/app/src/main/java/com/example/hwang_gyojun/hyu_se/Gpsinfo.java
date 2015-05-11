package com.example.hwang_gyojun.hyu_se;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Gpsinfo extends Activity implements LocationListener{

    TextView tv1 = null;
    TextView tv2 = null;

    LocationManager lm = null;
    String provider = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_start);

        tv1 = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        Button btn = (Button)findViewById(R.id.button);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria c = new Criteria();
        provider = lm.getBestProvider(c, true);

        if(provider == null || !lm.isProviderEnabled(provider)){
            List<String> list = lm.getAllProviders();

            for(int i = 0; i < list.size(); i++){
                String temp = list.get(i);
                if(lm.isProviderEnabled(temp)){
                    provider = temp;
                    break;
                }
            }
        }
        Location location = lm.getLastKnownLocation(provider);

        if(location == null){
            Toast.makeText(this, "사용가능한 위치 정보 제공자가 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            onLocationChanged(location);
        }
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                onResume();
            }
        });
    }

    public void onResume(){
        super.onResume();
        Toast.makeText(this, "찾는중...", Toast.LENGTH_SHORT).show();
        lm.requestLocationUpdates(provider, 500, 1, this);
    }
    @Override
    public void onPause(){
        super.onPause();
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        tv1.setText(String.valueOf(lat) + " / " + String.valueOf(lng));
        tv2.setText(getAddress(lat, lng));

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    /** 위도와 경도 기반으로 주소를 리턴하는 메서드*/
    public String getAddress(double lat, double lng){
        String address = null;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> list = null;

        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address = addr.getCountryName() + " "
                    + addr.getAdminArea() +" "
                    + addr.getLocality() + " ";
        }
        return address;
    }
}