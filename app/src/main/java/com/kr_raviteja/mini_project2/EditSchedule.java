package com.kr_raviteja.mini_project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditSchedule extends AppCompatActivity {
    RequestQueue mRequestQueue;
    ArrayList<String> classes = new ArrayList<String>();
    ArrayList<String> courses = new ArrayList<String>();
    SharedPreferences sp;
    ArrayAdapter<String> classesAdapter, coursesAdapter, timingsAdapter;
    Spinner spinner;
    String[] classTimings = {"8:30 - 9:20", "9:20 - 10:10", "10:30 - 11:20", "11:20 - 12:10", "1:30 - 2:20", "2:20 - 3:10",
                             "3:30 - 4:20", "4:20 to 5:10"};
    String activeClass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        spinner = (Spinner) findViewById(R.id.classes);
        initializeSpinnerOnClickHandler();
        initializeAddClassButton();

        classesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classes);
        classesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(classesAdapter);

        coursesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courses);
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timingsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classTimings);
        timingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp = getSharedPreferences("login_data",Context.MODE_PRIVATE);
        mRequestQueue = Volley.newRequestQueue(this);
        this.getAllClasses();
        this.getAllCourses();
    }

    public void initializeSpinnerOnClickHandler() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Toast.makeText(EditSchedule.this, item.toString(), Toast.LENGTH_SHORT).show();
                    activeClass = item.toString();
                    getClassSchedule(activeClass);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void onSubmit(View view) {
        Toast.makeText(this,"submitted sending to server",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,Main2Activity.class);
        startActivity(i);
    }

    public void getAllClasses() {
        String url = "http://134.209.79.159:3000/teacher/classes";
        final String email = sp.getString("email","nrs@nitt.edu");
        final String password = sp.getString("password","nrs");

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String fetchedClassesString = obj.getString("classes");
                    JSONArray classesArray = new JSONArray(fetchedClassesString);
                    for (int i = 0; i < classesArray.length(); i++) {
                        JSONObject jsonClass = classesArray.getJSONObject(i);
                        String classname = jsonClass.getString("classname");
                        classes.add(classname);
                    }
                    classesAdapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"error in connection",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    public void getAllCourses() {
        String url = "http://134.209.79.159:3000/teacher/courses";
        final String email = sp.getString("email","nrs@nitt.edu");
        final String password = sp.getString("password","nrs");

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String fetchedCoursesString = obj.getString("courses");
                    JSONArray coursesArray = new JSONArray(fetchedCoursesString);
                    for (int i = 0; i < coursesArray.length(); i++) {
                        JSONObject jsonCourse = coursesArray.getJSONObject(i);
                        String coursename = jsonCourse.getString("coursename");
                        courses.add(coursename);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"error in connection",Toast.LENGTH_SHORT).show();
                }

                coursesAdapter.notifyDataSetChanged();
                Log.d("Debugging", courses.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    public void getClassSchedule(final String classname) {
        String url = "http://134.209.79.159:3000/teacher/schedule";
        final String email = sp.getString("email","nrs@nitt.edu");
        final String password = sp.getString("password","nrs");

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String scheduleString = obj.getString("classes");
                    JSONArray scheduleJsonArray = new JSONArray(scheduleString);
                    renderSchedule(scheduleJsonArray);
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"error in connection",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("classname", classname);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    public void renderSchedule(JSONArray schedule) {
        Map<Integer, Integer> slots = new HashMap<Integer, Integer>();

        for (int i = 0; i < schedule.length(); i++) {
            JSONObject scheduleClass;
            Integer slot;
            try {
                scheduleClass = schedule.getJSONObject(i);
                String startTime = scheduleClass.getString("starttime");
                slot = getSlot(Integer.parseInt(startTime.substring(11, 13)));
            } catch (Exception e) {
                return;
            }
            slots.put(slot, i);
        }

        TableLayout table = findViewById(R.id.schedule_table);
        table.removeAllViews();

        for (int i = 1; i <= 8; i++) {
            TableRow classRow = new TableRow(this);
            classRow.setBackgroundResource(R.color.colorScheduleRow);
            classRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView timings = new TextView(this);
            timings.setText(classTimings[i - 1]);
            timings.setPadding(10,10,10,10);
            classRow.addView(timings);

            if (slots.containsKey(i)) {
                try {
                    Integer scheduleIndex = slots.get(i);
                    final JSONObject scheduleClass = schedule.getJSONObject(scheduleIndex);

                    TextView course = new TextView(this);
                    course.setText(scheduleClass.getString("coursename"));
                    course.setPadding(10,10,10,10);
                    classRow.addView(course);

                    TextView teacher = new TextView(this);
                    String teacherName = scheduleClass.getString("teachername");
                    teacher.setText(scheduleClass.getString("teachername"));
                    teacher.setPadding(10,10,10,10);
                    classRow.addView(teacher);

                    Log.d("Debugging", sp.getString("name", "NRS"));
                    Log.d("Debugging", teacherName);
                    Button cancel = new Button(this);
                    cancel.setText("Cancel");
                    if (!sp.getString("name", "NRS").equals(teacherName)) {
                        cancel.setEnabled(false);
                    } else {
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                try {
                                    cancelClass(scheduleClass.getString("scheduleid"));
                                } catch (Exception e) {
                                    return;
                                }
                            }
                        });
                    }
                    classRow.addView(cancel);
                } catch (Exception e) {
                    Log.d("Debugging", e.toString());
                    return;
                }
            } else {
                TextView course = new TextView(this);
                course.setText("");
                classRow.addView(course);

                TextView teacher = new TextView(this);
                teacher.setText("");
                classRow.addView(teacher);

                Button cancel = new Button(this);
                cancel.setText("Cancel");
                cancel.setEnabled(false);
                classRow.addView(cancel);
            }

            table.addView(classRow);
        }
    }

    public Integer getSlot(Integer startHour) {
        switch(startHour) {
            case 8:
                return 1;
            case 9:
                return 2;
            case 10:
                return 3;
            case 11:
                return 4;
            case 13:
                return 5;
            case 14:
                return 6;
            case 15:
                return 7;
            default:
                return 8;
        }
    }

    public void cancelClass( final String scheduleId) {
        String url = "http://134.209.79.159:3000/teacher/cancel";
        final String email = sp.getString("email","nrs@nitt.edu");
        final String password = sp.getString("password","nrs");

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    getClassSchedule(activeClass);
                    Log.d("Debugging", obj.toString());
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"error in connection",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("scheduleid", scheduleId);
                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

    public void initializeAddClassButton() {
        Button addClassButton = (Button) findViewById(R.id.submitbtn);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSchedule.this);
                LayoutInflater inflater = EditSchedule.this.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_add_class, null))
                        .setPositiveButton("Add Class", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Spinner classesSpinner = (Spinner) ((AlertDialog) dialog).findViewById(R.id.classList);
                                String className = classes.get(classesSpinner.getSelectedItemPosition());

                                Spinner coursesSpinner = (Spinner) ((AlertDialog) dialog).findViewById(R.id.courseList);
                                String courseName = courses.get(coursesSpinner.getSelectedItemPosition());

                                Spinner timeSpinner = (Spinner) ((AlertDialog) dialog).findViewById(R.id.timingList);
                                Integer timeSlot = timeSpinner.getSelectedItemPosition() + 1;

                                addClass(className, courseName, timeSlot);
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                String activeClass = "", activeCourse = "", activeTime = "";
                Spinner classes = (Spinner) alertDialog.findViewById(R.id.classList);
                classes.setAdapter(classesAdapter);
                Spinner courses = (Spinner) alertDialog.findViewById(R.id.courseList);
                courses.setAdapter(coursesAdapter);
                Spinner timings = (Spinner) alertDialog.findViewById(R.id.timingList);
                timings.setAdapter(timingsAdapter);
            }
        });
    }

    public void addClass( final String className, final String courseName, final Integer slot) {
        String url = "http://134.209.79.159:3000/teacher/add";
        final String email = sp.getString("email","nrs@nitt.edu");
        final String password = sp.getString("password","nrs");

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    getClassSchedule(activeClass);
                    Log.d("Debugging", obj.toString());
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"error in connection",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Cannot add class",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("classname", className);
                params.put("coursename", courseName);
                params.put("slot", slot.toString());
                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }
}
