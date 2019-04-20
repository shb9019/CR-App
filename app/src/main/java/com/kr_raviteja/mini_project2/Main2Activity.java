package com.kr_raviteja.mini_project2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public SharedPreferences sp;

    RequestQueue mRequestQueue;
    StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("login_data",Context.MODE_PRIVATE);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_main2);
        View inflated = stub.inflate();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sendRequest();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Toast.makeText(this,"profile selected",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ProfileActivity.class);
            startActivity(i);
        }
        else if (id == R.id.notifications) {
            Toast.makeText(this,"notifications selected",Toast.LENGTH_SHORT).show();
            createNotification();
        }
        else if (id == R.id.refresh) {
            //Toast.makeText(this,"settings selected",Toast.LENGTH_SHORT).show();
            sendRequest();
        }
        else if(id == R.id.logout) {
            handleLogout();
        }
        else if(id == R.id.editschedule) {

            if(sp.getBoolean("student_teacher",true)) {
                Toast.makeText(this,"not available",Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(this,"editschedule selected",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,EditSchedule.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void handleLogout() {
        Toast.makeText(this,"logout selected",Toast.LENGTH_SHORT).show();
        sp.edit().clear().apply();

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public void createNotification() {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("my_channel_01",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }


        Intent intent = new Intent(this,Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,"my_channel_01")
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        mNotificationManager.notify(001, mBuilder.build());

    }


    //session create kaaledhu ani vasthondhi reply
    public void sendRequest() {

        CookieManager manager = new CookieManager();
        CookieHandler.setDefault( manager  );

        String url = "http://10.0.2.2:3000/student/schedule";
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray arr = obj.getJSONArray("classes");
                    for (int i=0; i < arr.length(); i++) {


                        String coursename = arr.getJSONObject(i).getString("coursename");
                        String starttime = arr.getJSONObject(i).getString("starttime");
                        String endtime = arr.getJSONObject(i).getString("endtime");
                        int slot = getSlot(starttime,endtime);
                        String s1 = Integer.toString(slot);
                        s1 = "course" + s1;

                        int rid = getResources().getIdentifier(s1,"id",getPackageName());
                        TextView tview = (TextView) findViewById(rid);
                        tview.setText(coursename);
                        Toast.makeText(getApplicationContext(),Integer.toString(slot),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"press F",Toast.LENGTH_SHORT).show();
                }


                return ;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                return ;
            }

    })
        {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();

                params.put("rollno",sp.getString("username",null));
                params.put("password",sp.getString("password",null));

                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }




    public int getSlot(String starttime, String endtime) {

        String hrs = starttime.substring(11,13);
        String minutes = starttime.substring(14,16);
        //Toast.makeText(getApplicationContext(),hrs + " " + minutes, Toast.LENGTH_LONG).show();

        switch (Integer.parseInt(hrs)) {
            case 8:
                return 1;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                return 4;
            case 1:
                return 5;
            case 2:
                return 6;
            case 3:
                return 7;
            default:
                return 8;

        }

    }
}
