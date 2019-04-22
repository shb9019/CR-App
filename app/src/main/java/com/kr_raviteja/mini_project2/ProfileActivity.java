package com.kr_raviteja.mini_project2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    String TAG = "Toast Debug";
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

        if(sp.getBoolean("student_teacher",true)) {
            Populateviewsstudent();
        }
        else {
            Populateviewsteacher();
        }
    }

    public void Populateviewsstudent() {
        troll.setText(sp.getString("username",null));
        tname.setText(sp.getString("name",null));
        temail.setText(sp.getString("email",null));
        tclassid.setText(sp.getString("classid",null));
        tsemester.setText(sp.getString("semester",null));
    }

    public void Populateviewsteacher() {
        tname.setText(sp.getString("name",null));
        temail.setText(sp.getString("email",null));

        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow tr1 = (TableRow) findViewById(R.id.rowrollnumber);
        table.removeView(tr1);

        TableRow tr2 = (TableRow) findViewById(R.id.rowsemester);
        table.removeView(tr2);

        TableRow tr3 = (TableRow) findViewById(R.id.rowclass);
        table.removeView(tr3);
    }
}
