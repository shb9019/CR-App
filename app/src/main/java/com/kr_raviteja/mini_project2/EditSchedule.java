package com.kr_raviteja.mini_project2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] country = { "India", "USA", "China", "Japan", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        Spinner spinner = (Spinner) findViewById(R.id.classes);
        spinner.setOnItemSelectedListener(this);

        EditText e1 = (EditText)findViewById(R.id.course1);
        e1.setEnabled(false);

        CheckBox c1 = (CheckBox) findViewById(R.id.checkbox1);
        c1.setEnabled(false);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onSubmit(View view) {
        Toast.makeText(this,"submitted sending to server",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
    }
}
