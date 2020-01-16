package com.festfeed.awf.activities.gamescan;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.festfeed.awf.R;
import com.festfeed.awf.gamemodel.QrGameIcon;
import com.festfeed.awf.utils.Preferences;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class EventScan extends Activity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
  //  int i=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().   addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow(). getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.event_scan);
       // i=0;
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }

        pref = getApplicationContext().getSharedPreferences("Festfeed", MODE_PRIVATE);
        editor = pref.edit();
        //i= pref.getInt("counDEBUG",0);
    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EventScan.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera(camId);
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView = null;
    }

    @Override
    public void handleResult(Result rawResult) {

        final String result = rawResult.getText();
        Log.e("QRCodeScanner", rawResult.getText());
        Log.e("QRCodeScanner", rawResult.getBarcodeFormat().toString());


        Boolean qrCodeScanedPerferct=false;
        QrGameIcon qrGameIconSelected=null;

        for (int i = 0; i < Preferences.INSTANCE.getSelectedGame().getQrGameIcons().size(); i++) {

            QrGameIcon qrGameIcon= Preferences.INSTANCE.getSelectedGame().getQrGameIcons().get(i);
            if(qrGameIcon.getQrValue().equalsIgnoreCase(result))
            {
                qrGameIconSelected= Preferences.INSTANCE.getSelectedGame().getQrGameIcons().get(i);
                qrCodeScanedPerferct=true;
                break;
            }
        }



        if(qrCodeScanedPerferct)
        {
            if(!pref.getBoolean(Preferences.INSTANCE.getSelectedGame().getEventid()+""+qrGameIconSelected.getQrValue(), false)) {
                saveValues(Preferences.INSTANCE.getSelectedGame().getEventid()+""+qrGameIconSelected.getQrValue());
            }else{
                returnValue();
            }
        }else
        {
            editor.putBoolean("none", true);
            editor.commit();

            Intent intent=new Intent();
            intent.putExtra("unsuccessful",true);
            setResult(2,intent);
            finish();//finishing activity
        }






    }

    public void saveValues(String value) {
        editor.putBoolean(value, true);
        editor.putInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count",pref.getInt(Preferences.INSTANCE.getSelectedGame().getEventid()+"count", 0)+1);
        //editor.putString("newScannedItem", value);
        editor.commit();

        Intent intent=new Intent();
        intent.putExtra("unsuccessful",false);
        setResult(2,intent);
        finish();//finishing activity
    }

    public void returnValue() {
        Intent intent=new Intent();
        intent.putExtra("unsuccessful",false);
        setResult(2,intent);
        finish();//finishing activity
    }

}