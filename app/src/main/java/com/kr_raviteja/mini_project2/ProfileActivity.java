package com.kr_raviteja.mini_project2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    TextView troll,tname,temail,tclassid,tsemester;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        troll = (TextView) findViewById(R.id.rollnumber);
        tname = (TextView) findViewById(R.id.name);
        temail = (TextView) findViewById(R.id.email);
        tclassid = (TextView) findViewById(R.id.classid);
        tsemester = (TextView) findViewById(R.id.semester);
        sp = getSharedPreferences("login_data",Context.MODE_PRIVATE);

        Populateviews();
    }

    public void Populateviews() {
        troll.setText(sp.getString("username",null));
        tname.setText(sp.getString("name",null));
        temail.setText(sp.getString("email",null));
        tclassid.setText(sp.getString("classid",null));
        tsemester.setText(sp.getString("semester",null));
        return ;

    }

}
