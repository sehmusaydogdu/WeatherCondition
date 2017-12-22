package com.weather_condition;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class hava_durumu extends Activity {

    private TextView txtAciklama,txtSehir,txtSicaklik,txtHavaDurumu;
    private Spinner spinner;
    private ImageView imgView;
    private Bitmap bitImage;

    private String secilenSehir;
    private Database appDatabase;

    private void init(){
        txtAciklama=findViewById(R.id.txtAciklama);
        txtHavaDurumu=findViewById(R.id.txtHavaDurumu);
        txtSehir=findViewById(R.id.txtSehir);
        txtSicaklik=findViewById(R.id.txtSicaklik);
        spinner=findViewById(R.id.spinner);
        imgView=findViewById(R.id.imgView);

        appDatabase=new Database(this);
        secilenSehir="İZMİR";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hava_durumu);
        init();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secilenSehir=adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1,appDatabase.getSehirAdiList());

        spinner.setAdapter(arrayAdapter);
        new HavaDurumu().execute();
    }

    public void onGoster(View view) {
        new HavaDurumu().execute();
    }

    private class HavaDurumu extends AsyncTask<String,String,String>{

        private String url_kaynak="http://api.openweathermap.org/data/2.5/weather?q="+secilenSehir+"&appid=5519df78a91952f50079565124888a76";
        String icon="";
        String Aciklama,Sehir,HavaDurumu;
        int sicaklik,code;
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection=null;
            BufferedReader bufferedReader=null;

            try{
                URL url=new URL(url_kaynak);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String satir;
                String dosya="";
                while((satir=bufferedReader.readLine())!=null){
                    dosya+=satir;
                }


                JSONObject jsonObject=new JSONObject(dosya);//Veriler JSON formatında döner
                code=jsonObject.getInt("cod"); //Json'dan gelen code alıyorum.


                if(code==200){//Cod = 200 ise işlem başarılıdır.


                    Sehir=jsonObject.getString("name").toUpperCase(); //Seçilen şehrin adını döndürüyor.

                    //Şehrin sıcaklığını Kelvinden dereceye dönüştürüp alıyorum
                    JSONObject jsonObject_main=jsonObject.getJSONObject("main");
                    sicaklik=jsonObject_main.getInt("temp")-273;

                    JSONObject json_sys=jsonObject.getJSONObject("sys");
                    Sehir+=" - "+json_sys.getString("country");

                    JSONArray jsonArray=jsonObject.getJSONArray("weather");
                    JSONObject jsonArrayJSONObject=jsonArray.getJSONObject(0);

                    Aciklama=jsonArrayJSONObject.getString("description").toUpperCase();
                    HavaDurumu=jsonArrayJSONObject.getString("main").toUpperCase();


                    icon=jsonArrayJSONObject.getString("icon");
                    URL icon_url = new URL("http://openweathermap.org/img/w/"+icon+".png");
                    bitImage = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());
                }
                else{
                    Toast.makeText(hava_durumu.this, "Bulunamadı", Toast.LENGTH_SHORT).show();
                }

                return dosya;
            }
            catch (Exception e){
                Toast.makeText(hava_durumu.this, "Hata", Toast.LENGTH_SHORT).show();
            }

            return "Hata";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imgView.setImageBitmap(bitImage);
            txtAciklama.setText(Aciklama);
            txtSicaklik.setText(String.valueOf(sicaklik)+"\u2103");
            txtSehir.setText(Sehir);
            txtHavaDurumu.setText(HavaDurumu);

        }
    }

}
