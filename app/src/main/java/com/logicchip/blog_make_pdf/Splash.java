package com.logicchip.blog_make_pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static com.logicchip.blog_make_pdf.Const.FOLDER_PDF;

public class Splash extends AppCompatActivity {
    String[] permissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean isSettings = false;

    private static final int PERMISSION_CALLBACK = 111;
    private static final int PERMISSION_REQUEST = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadInt();
    }


    public void loadInt(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            ChkPerm();
        }else{
            afterPermission();
        }
    }


    private void afterPermission() {

        File folderPdf=new File(FOLDER_PDF);

        if (!folderPdf.exists()){
            folderPdf.mkdir();
            forOpeningActivity();
        }else {
            forOpeningActivity();
        }

    }

    public void forOpeningActivity(){
        startActivity(new Intent(Splash.this,MainActivity.class));
        finish();
    }


    public void ChkPerm(){
        if(forSelfPermission()){

            if(shouldShow()){
                permissionCallBack();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(Splash.this,permissionList, PERMISSION_CALLBACK);
            }
        } else {
            //You already have the permission, just go ahead.
            afterPermission();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                afterPermission();
            } else if(shouldShow()){

                permissionCallBack();
            } else {

                permissionSettings();
                Toast.makeText(getBaseContext(),"Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    private boolean forSelfPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(Splash.this, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }

    private boolean resultPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(Splash.this, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
            } else {
                allgranted = false;
                break;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }


    private boolean shouldShow(){

        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionList[i])) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }
    }

    private void permissionCallBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs Multiple permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(Splash.this,permissionList, PERMISSION_CALLBACK);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void permissionSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs permission allow them from settings.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isSettings = true;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, PERMISSION_REQUEST);
                // Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("R","result");
        if (requestCode == PERMISSION_REQUEST) {

            if (resultPermission()){
                Log.d("R","result s");
                afterPermission();
            }else{
                Log.d("R","result c");
                ChkPerm();
            }

        }
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isSettings) {
            Log.d("R","resume");
            if (resultPermission()){
                Log.d("R","resume s");
                afterPermission();
            }else{
                Log.d("R","resume c");
                ChkPerm();
            }

            isSettings=false;
        }
    }


}
