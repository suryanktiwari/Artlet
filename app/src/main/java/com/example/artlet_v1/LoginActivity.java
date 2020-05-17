package com.example.artlet_v1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

public class LoginActivity extends AppCompatActivity {

    private EditText e;
    private EditText p;
    Button lg;
    SQLiteDatabase databaseObj;
    DatabaseHelper databaseHelper;
    Cursor cursor;
    private Properties properties;
    private PropertyReader propertyReader;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        databaseHelper = new DatabaseHelper(context);

        propertyReader = new PropertyReader(context);
        properties = propertyReader.getMyProperties("messages.properties");
    }

    public void show_login(View v)
    {
        e=(EditText)findViewById(R.id.et4);
        p=(EditText)findViewById(R.id.et5);
        lg=(Button)findViewById(R.id.loginb);
        lg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email=e.getText().toString().trim();
                String password=p.getText().toString().trim();

                Log.d("CHECKING::::",properties.getProperty("ENTER_ALL_DETAILS"));
                if(email.matches("") && password.matches(""))
                    showMessage(properties.getProperty("ENTER_ALL_DETAILS"));
                else if(email.matches(""))
                    showMessage(properties.getProperty("ENTER_EMAIL_ADDRESS"));
                else if(password.matches(""))
                    showMessage(properties.getProperty("ENTER_PASSWORD"));
                else{
                    checkLoginDetails(email, password);
                }
            }
        });
    }

    public void checkLoginDetails(String email, String password){
        databaseObj = databaseHelper.getReadableDatabase();
        String query = "SELECT "+TableUser.TableUserClass.USER_EMAIL+", "+TableUser.TableUserClass.USER_NAME +", "+ TableUser.TableUserClass.USER_ID
                        +" FROM "+ TableUser.TableUserClass.TABLE_Users
                        +" WHERE email='"+email +"' AND password='"+password+"'";

        cursor = databaseObj.rawQuery(query,null);

        if (cursor!= null && cursor.moveToFirst() && cursor.getCount()>0) {
            String loginEmail = cursor.getString(0);
            String loginName = cursor.getString(1);
            String loginUserId = cursor.getString(2);

            showMessage(properties.getProperty("LOGIN_SUCCESS"));

            Intent goToDashboardIntent = new Intent(this, DashboardActivity.class);

            goToDashboardIntent.putExtra("key_userid", loginUserId);
            goToDashboardIntent.putExtra("key_email",loginEmail);
            goToDashboardIntent.putExtra("key_username", loginName);

            startActivity(goToDashboardIntent);
        }
        else{
            showMessage(properties.getProperty("LOGIN_DETAILS_MISMATCH"));
        }
    }
    public void backToMain(View view) {
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
