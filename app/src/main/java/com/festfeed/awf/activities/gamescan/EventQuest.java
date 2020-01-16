package com.festfeed.awf.activities.gamescan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



import com.festfeed.awf.R;
import com.festfeed.awf.activities.InstructionsActivity;
import com.festfeed.awf.activities.TapToScanActivity;
import com.festfeed.awf.adapters.CustomAdapter;
import com.festfeed.awf.gamemodel.Configuration;
import com.festfeed.awf.gamemodel.GameData;
import com.festfeed.awf.utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class EventQuest extends Activity {

    ImageView scan_logo;
    public static Context context;
    TextView additional_instructions,progress_text, scan_text;
    ImageView close;
    SharedPreferences pref;
    ImageView elfie, frosty, holly, mrsclaus, rudolph, sugarplum;
    ProgressBar progress_bar;
   // Typeface helveticaNeueR, helveticaNeueM;
    GridView simpleGrid;
    int sizeOfGame=6;
    CustomAdapter customAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().   addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow(). getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        sizeOfGame=Preferences.INSTANCE.getSelectedGame().getQrGameIcons().size();




        setContentView(R.layout.activity_tap_to_scan);
        context=this;
        initializeUI();
        scan_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EventQuest.this,EventScan.class);
                startActivityForResult(intent, 2);
            }
        });

        additional_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });

        progress_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

    }

    public void initializeUI(){
        scan_logo = (ImageView) findViewById(R.id.scan_logo);

//        elfie = (ImageView) findViewById(R.id.elfie);
//        frosty = (ImageView) findViewById(R.id.frosty);
//        holly = (ImageView) findViewById(R.id.holly);
//        mrsclaus = (ImageView) findViewById(R.id.mrsclaus);
//        rudolph = (ImageView) findViewById(R.id.rudolph);
//        sugarplum = (ImageView) findViewById(R.id.sugarplum);



        simpleGrid = (GridView) findViewById(R.id.gamegrid); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
         customAdapter = new CustomAdapter(getApplicationContext(), Preferences.INSTANCE.getSelectedGame().getQrGameIcons());
        simpleGrid.setAdapter(customAdapter);


        scan_text = (TextView) findViewById(R.id.scan_text);

        additional_instructions = (TextView) findViewById(R.id.needInstructionBtn);
        close = (ImageView) findViewById(R.id.backBtn);
        progress_text = (TextView) findViewById(R.id.progressText);
        progress_bar = (ProgressBar) findViewById(R.id.progressBar);
        pref = getApplicationContext().getSharedPreferences("Festfeed", MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        customAdapter.notifyDataSetChanged();
        if(pref.getBoolean("none", false)){ }
        progress();
    }

    public void popUpBox(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert1 = builder.create();
        if(!alert1.isShowing()) {
            alert1.show();
        }
    }

    public void resetGame() {
        if (pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) == sizeOfGame) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            builder.setTitle("Warning!");
            builder.setMessage("You're about to reset the game. Make sure you take this to the appropriate representative.");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    
                    pref.edit().putInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0).commit();
                    //pref.edit().putString("newScannedItem","").commit();
                    progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
                    progress_bar.setProgress(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0));

                    SharedPreferences sharedPrefs = getApplicationContext()
                            .getSharedPreferences("Festfeed", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.clear();
                    editor.commit();
                    customAdapter.notifyDataSetChanged();
                }
            });

            AlertDialog alert1 = builder.create();
            if(!alert1.isShowing()) {
                alert1.show();
            }
        }
    }

    public void progress() {

        progress_bar.setProgress(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0));
        if (pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) == sizeOfGame) {
            progress_text.setText("COMPLETED!");
            popUpBox("Scan Successful!", "Congrats! You have scanned all QR codes. Go redeem your prize!");
        } else if (pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) == 0) {
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                boolean val = extras.getBoolean("unsuccessful");
                if (val) {
                    progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
                    popUpBox("Invalid QR Code!", "Please try Again!");
                } else {
                    progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
                    popUpBox("Scan Successful!", "Only " + (sizeOfGame -pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0)) + " more left to scan. Keep it going!");
                }
            }
            else
            {
                progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        try {
            if(requestCode==2)
            {
                boolean val=data.getBooleanExtra("unsuccessful",false);
                customAdapter.notifyDataSetChanged();
                if (val) {
                    progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
                    popUpBox("Invalid QR Code!", "Please try Again!");
                } else {
                    progress_text.setText(pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0) * 100 / sizeOfGame + "% Completed");
                    if (pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0)< sizeOfGame) {
                        popUpBox("Scan Successful!", "Only " + (sizeOfGame - pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0)) + " more left to scan. Keep it going!");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
