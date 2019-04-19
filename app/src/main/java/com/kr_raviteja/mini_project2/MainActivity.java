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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;
    SharedPreferences sp;

    RequestQueue mRequestQueue;
    StringRequest mStringRequest;
    String url = "http://www.mocky.io/v2/5cba15423000001a3bbfa71a";


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

        //Toast.makeText(this,username + " " + password,Toast.LENGTH_LONG).show();

        if(!username.equals("106") && !username.equals("206") || !password.equals("106")) {
            //Toast.makeText(this,"Wrong User Credentials",Toast.LENGTH_LONG).show();
            return ;
        }

        if(Pattern.matches("1.*",username)) {
            //Toast.makeText(this,"student login",Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("student_teacher",true).apply();
        }
        else if(Pattern.matches("2.*",username)) {
            //Toast.makeText(this,"Teacher Login",Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("student_teacher",false).apply();
        }

        sendRequest(username,password);

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

    public void sendRequest(String username, String password) {



        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
