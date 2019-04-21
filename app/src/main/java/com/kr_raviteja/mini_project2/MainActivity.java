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
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText usernamefield,passwordfield;
    SharedPreferences sp;

    RequestQueue mRequestQueue;
    StringRequest mStringRequest;


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


        if(Pattern.matches("1.*",username)) {
            sp.edit().putBoolean("student_teacher",true).apply();
        }
        else {
            sp.edit().putBoolean("student_teacher",false).apply();
        }

        sendRequest(username,password);

    }

    public void gotodashboard() {
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
        finish();
    }

    public void sendRequest(String username, String password) {

        String url ;

        mRequestQueue = Volley.newRequestQueue(this);
        if(sp.getBoolean("student_teacher",true)) {
            url = "http://10.0.2.2:3000/student/login";
            //134.209.79.159
            sendRequestStudent(username,password,url);
        }
        else {
            url = "http://134.209.79.159:3000/teacher/login";
            sendRequestTeacher(username,password,url);
        }
    }

    public void sendRequestTeacher(final String username, final String password,final String url) {

        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    String type = obj.getString("type");

                    sp.edit().putString("email", username).apply();
                    sp.edit().putString("password", password).apply();
                    Integer emailLength = username.length();
                    sp.edit().putString("name", username.substring(0, emailLength - 9)).apply();

                    Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"press F",Toast.LENGTH_SHORT).show();
                }

                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putString("username",username).apply();
                sp.edit().putString("password",password).apply();



                gotodashboard();

                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                return ;
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                        Toast.makeText(getApplicationContext(),"wrong credentials",Toast.LENGTH_LONG).show();
                        return ;
                    }
                })

        {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",username);
                params.put("password",password);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }



    public void sendRequestStudent(final String username, final String password, String url) {


        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    String type = obj.getString("type");
                    String name = obj.getJSONObject("details").getString("name");
                    String email = obj.getJSONObject("details").getString("email");
                    String semester = obj.getJSONObject("details").getString("semester");
                    String classid = obj.getJSONObject("details").getString("classid");

                    sp.edit().putString("name",name).apply();
                    sp.edit().putString("email",email).apply();
                    sp.edit().putString("semester",semester).apply();
                    sp.edit().putString("classid", classid).apply();

                    Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"press F",Toast.LENGTH_SHORT).show();
                }

                sp.edit().putBoolean("logged",true).apply();
                sp.edit().putString("username",username).apply();
                sp.edit().putString("password",password).apply();



                gotodashboard();

                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                return ;
            }
        },

                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(),"wrong credentials",Toast.LENGTH_LONG).show();
                return ;
            }
        })

        {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("rollno",username);
                params.put("password",password);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

}


