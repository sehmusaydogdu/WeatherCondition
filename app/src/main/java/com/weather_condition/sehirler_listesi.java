package com.weather_condition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class sehirler_listesi extends Activity {


    private ListView lstView;
    private EditText editSehirAdi;
    private List<Sehirler> appList;
    private List<String>appSehirler;

    private Database appDatabase;

    private void init(){
        lstView=findViewById(R.id.lstView);
        editSehirAdi=findViewById(R.id.editSehirAdi);
        appDatabase=new Database(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sehirler_listesi);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog alertMessage = new AlertDialog.Builder(sehirler_listesi.this).create();
                alertMessage.setTitle("Sil");
                alertMessage.setMessage(" silmek istediğinizden emin misiniz?");

                alertMessage.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                        //appDatabase.sehirSil();
                    }
                });

                alertMessage.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertMessage.show();
                return false;

            }
        });
    }

    private void Listele(){
        try{
            appList=appDatabase.tumKayitlar();
            appSehirler=new ArrayList<>();
            for (Sehirler s : appList){
                appSehirler.add(s.getSehirAdi());
            }

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                    this,android.R.layout.simple_list_item_1,appSehirler);
            lstView.setAdapter(arrayAdapter);
        }
        catch (SQLiteException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Listele();
    }

    public void onEkle(View view) {
        String deger=editSehirAdi.getText().toString();
        if (deger.equals("")){
            Toast.makeText(this, "Lütfen Şehir Giriniz", Toast.LENGTH_SHORT).show();
        }
        else{
            //Database db=new Database(this);
            appDatabase.sehirEkle(deger);
            Listele();
        }

    }
}
