package com.kr_raviteja.mini_project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login_data",Context.MODE_PRIVATE);
        if(sp.getBoolean("logged",false)) {
            gotodashboard();
        }

        usernamefield = (EditText) findViewById(R.id.username);
        passwordfield = (EditText) findViewById(R.id.password);
    }

    public void onSubmit(View view) {

        String username,password;
        username = usernamefield.getText().toString();
        password = passwordfield.getText().toString();

        Toast.makeText(this,username + " " + password,Toast.LENGTH_LONG).show();

        if(!username.equals("106") && !username.equals("206") || !password.equals("106")) {
            Toast.makeText(this,"Wrong User Credentials",Toast.LENGTH_LONG).show();
            return ;
        }

        if(Pattern.matches("1.*",username)) {
            Toast.makeText(this,"student login",Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("student_teacher",true).apply();
        }
        else if(Pattern.matches("2.*",username)) {
            Toast.makeText(this,"Teacher Login",Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("student_teacher",false).apply();
        }

        sp.edit().putBoolean("logged",true).apply();
        sp.edit().putString("username",username).apply();
        sp.edit().putString("password",password).apply();

        gotodashboard();

        return ;
    }

    public void gotodashboard() {
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
        finish();
    }
}
