package com.example.artlet_v1;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Properties;

public class RegisterActivity extends AppCompatActivity {

	private EditText email2;
    private EditText pswd;
    private EditText n;
    private Button register;
    private Properties properties;
    private PropertyReader propertyReader;
    private Context context;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        propertyReader = new PropertyReader(context);
        properties = propertyReader.getMyProperties("messages.properties");
        db = new DatabaseHelper(context);
    }

    public void go_back(View v)
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void show_register(View v)
    {
        email2=(EditText)findViewById(R.id.et1);
        pswd=(EditText)findViewById(R.id.et2);
        n=(EditText)findViewById(R.id.et3);
        register=(Button)findViewById(R.id.registerb);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email=email2.getText().toString().trim();
                String password=pswd.getText().toString().trim();
                String name=n.getText().toString().trim();

                Boolean validFlag = false;
                int count = 0;
                for(int i=0;i<email.length();i++)
                {
                    if(email.charAt(i)=='@') {
                        count++;
                        if((email.charAt(i+1)>='A'&&email.charAt(i+1)<='Z')||(email.charAt(i+1)>='a'&&email.charAt(i+1)<='z'))
                            validFlag=true;
                    }
                }
                if(count != 1||!(email.substring(email.length()-4, email.length()).equals(".com"))||!validFlag) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(email.matches("") || password.matches("")|| name.matches(""))
                    showMessage(properties.getProperty("ENTER_ALL_DETAILS"));
                else{
                    String query = " Select * from " + TableUser.TableUserClass.TABLE_Users +
                                    " WHERE "+TableUser.TableUserClass.USER_EMAIL +"='"+email+"' ";
                    Cursor cursor = db.getReadableDatabase().rawQuery(query, null);

                    if(cursor!=null && cursor.moveToFirst() && cursor.getCount()>0){
                        Log.d("RegisterActivit","User already Exists");
                        showMessage(properties.getProperty("USER_EXISTS"));
                    }
                    else {
                        long userId = db.InsertUserData(db, name, email, password, "Delhi");
                        db.InsertUserProfileData(db, userId, name, 0, 0,0,"");
                        Log.d("RegisterActivity", "Successfully inserted");
                        showMessage(properties.getProperty("REGISTRATION_SUCCESS"));

                        Intent i=new Intent(view.getContext(), LoginActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
