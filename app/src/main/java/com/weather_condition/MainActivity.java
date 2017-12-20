package com.weather_condition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onHavaDurumuGoster(View view) {
        Intent intent=new Intent(this,hava_durumu.class);
        startActivity(intent);
    }

    public void onListele(View view) {
        Intent intent=new Intent(this,sehirler_listesi.class);
        startActivity(intent);
    }
}
