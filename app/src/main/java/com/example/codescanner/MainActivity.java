package com.example.codescanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;

    private ScanOptions options = new ScanOptions();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new MyDatabaseHelper(MainActivity.this);

        getSupportActionBar().hide();



    }



    public void ScanClick(View view) {
            ScanCode();
    }
    //-------------------------- add an item -------------------------
    private void ScanCode(){
        scanOption();
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->{
        TextView ids;
        EditText name,value;
        if(result.getContents() != null ){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog,null);
            ids = view.findViewById(R.id.itemCodeTV);
            name = view.findViewById(R.id.ItemNameET);
            value = view.findViewById(R.id.ItemvalueET);
            ids.setText(result.getContents());
            builder.setView(view)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(result.getContents() != null){
                            //    ItemsAdded[0].add(result.getContents());
                            //    ItemsAdded[1].add(name.getText().toString());
                            //    ItemsAdded[2].add(value.getText().toString());

                                myDB.AddItems(result.getContents(),
                                        name.getText().toString(),
                                        Float.parseFloat(value.getText().toString()));
                            }
                        }
                    }).show();
        }
    } );


    public void scanOption(){
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
    }

    // -----------------------show an item info--------------------
    public void showDetales(View view) {
       scanOption();

        barLan.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLan = registerForActivityResult(new ScanContract(),result -> {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogmassage,null);
        TextView tv = view.findViewById(R.id.messageTV);
        tv.setText(myDB.ShowingItems(result.getContents()));
       builder.setView(view)
                       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dialogInterface.dismiss();
                           }
                       })

               .show();


    });



    // -------------------------modifying an item info---------------------
    ActivityResultLauncher<ScanOptions> blan = registerForActivityResult(new ScanContract(),result -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog,null);
        TextView tv = view.findViewById(R.id.itemCodeTV);
        EditText name = view.findViewById(R.id.ItemNameET);
        EditText value = view.findViewById(R.id.ItemvalueET);

        tv.setText(result.getContents());
        myDB.ItemNV(result.getContents());
        name.setText(myDB.items[0]);
        value.setText(myDB.items[1]);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Modify Canceled", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.modifyItem(result.getContents(),name.getText().toString(),
                                Float.parseFloat(value.getText().toString()));
                    }
                })
                .show();

    });

    public void ModifyItem(View view) {

        scanOption();
        blan.launch(options);
    }


}