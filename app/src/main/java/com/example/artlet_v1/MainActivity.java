package com.example.artlet_v1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    private int STORAGE_CODE = 100;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Boolean entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DatabaseHelper(getApplicationContext());

        entry = false;
        if(permissionGrantCheck())
        {
    //        Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
            entry = true;
        }

    }

    private boolean permissionGrantCheck()
    {
        List<String> permissionsNeeded = new ArrayList<>();
        for(String permission: permissions)
        {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
                permissionsNeeded.add(permission);
            }
        }

        if(!permissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), STORAGE_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_CODE) {
            HashMap<String, Integer> results = new HashMap<>();
            int denied = 0;

            for(int i=0; i<grantResults.length; i++)
            {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    results.put(permissions[i], grantResults[i]);
                    denied++;
                }
            }

            if(denied == 0)
            {
                entry = true;
                return;
            }
            else
            {
                for(Map.Entry<String, Integer> entry: results.entrySet())
                {
                    String perm = entry.getKey();
                    int result = entry.getValue();

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, perm))
                    {
                        Toast.makeText(getApplicationContext(),  "This app needs storage read and write permissions to work. Kindly, Grant Permissions", Toast.LENGTH_LONG);
                        openExplanatoryDialog();
                    }
                    else
                    {
                        openSettingsDialog();
                    }
                }
            }
        }
    }

    private void openExplanatoryDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require storage permission to use awesome feature. Kindly Grant them.");
        builder.setPositiveButton("Grant Permissions", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                permissionGrantCheck();
            }
        });
        builder.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    public void show_login(View v) {
        if (entry) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else
        {
            permissionGrantCheck();
        }
    }

    public void show_register(View v) {
        if (entry) {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
        else
        {
            permissionGrantCheck();
        }
    }

    public void skip(View v) {
        if (entry) {
            Intent goToDashboardIntent = new Intent(this, DashboardActivity.class);
            goToDashboardIntent.putExtra("key_email", "abc@abc.com");
            goToDashboardIntent.putExtra("key_username", "abc");
            startActivity(goToDashboardIntent);
        }
        else
        {
            permissionGrantCheck();
        }
    }
}


/*
package com.example.artlet_v1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.R;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    private int STORAGE_CODE_READ = 100;
    private int STORAGE_CODE_WRITE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DatabaseHelper(getApplicationContext());
        if (permissionAlreadyGrantedRead()) {
            Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionRead();
        }

        this.db = new DatabaseHelper(getApplicationContext());
        if (permissionAlreadyGrantedWrite()) {
            Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionWrite();
        }


    }

    private boolean permissionAlreadyGrantedRead() {

        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }


    private void requestPermissionRead() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE_READ);

    }

    private boolean permissionAlreadyGrantedWrite() {

        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }


    private void requestPermissionWrite() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_CODE_WRITE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_CODE_READ) {
            Log.d("bruno", "code read");

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (!showRationale) {
                    openSettingsDialog();
                }
            }
        }
        else if(requestCode == STORAGE_CODE_WRITE)
        {
            Log.d("bruno", "code write");

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (!showRationale) {
                    openSettingsDialog();
                }
            }
        }
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    public void show_login(View v) {
        if (permissionAlreadyGrantedRead()) {
            if (permissionAlreadyGrantedWrite()) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            } else
                requestPermissionWrite();
        } else
            requestPermissionRead();
    }

    public void show_register(View v) {
        if (permissionAlreadyGrantedRead()) {
            if (permissionAlreadyGrantedWrite()) {
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
            } else
                requestPermissionWrite();
        } else
            requestPermissionRead();

    }

    public void skip(View v) {
        if (permissionAlreadyGrantedRead()) {
            if (permissionAlreadyGrantedWrite()) {
                Intent goToDashboardIntent = new Intent(this, DashboardActivity.class);
                goToDashboardIntent.putExtra("key_email", "abc@abc.com");
                goToDashboardIntent.putExtra("key_username", "abc");
                startActivity(goToDashboardIntent);
            } else
                requestPermissionWrite();
        } else
            requestPermissionRead();

    }
}

*/

/*package com.example.artlet_v1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.artlet_v1.DashboardActivity;
import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.LoginActivity;
import com.example.artlet_v1.R;
import com.example.artlet_v1.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    private int STORAGE_CODE = 100;
    private boolean granted= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DatabaseHelper(getApplicationContext());
        if (permissionAlreadyGranted()) {
            granted=true;
            Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }

    private boolean permissionAlreadyGranted() {

        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result1 == result2 && result2 == PackageManager.PERMISSION_GRANTED){
            granted= true;
            return true;
        }
        else {
            granted = false;
            return false;
        }
    }

    private void requestPermission() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_CODE);
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                granted=false;
                return false;
            }
        }
        granted=true;
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_CODE) {
            if(hasAllPermissionsGranted(grantResults)){
                // all permissions granted
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
                granted= true;
            }else {
                // some permission are denied.
                granted= false;
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = false;
                showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (showRationale || hasAllPermissionsGranted(grantResults)==false) {
                    openSettingsDialog();
                }
            }
        }
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app requires permissions to use awesome features. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                granted= false;
                finish();
            }
        });
        builder.show();
    }

    public void show_login(View v) {
        if (granted) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else
            requestPermission();
    }

    public void show_register(View v) {
        if (granted) {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        } else
            requestPermission();
    }

    public void skip(View v) {
        if (granted) {
            Intent goToDashboardIntent = new Intent(this, DashboardActivity.class);
            goToDashboardIntent.putExtra("key_email", "abc@abc.com");
            goToDashboardIntent.putExtra("key_username", "abc");
            startActivity(goToDashboardIntent);
        } else
            requestPermission();
    }
}

 */