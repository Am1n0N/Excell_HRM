package com.app.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.Pto_management.PtoManagement;
import com.app.myapplication.Record_management.RecordManagement;
import com.app.myapplication.attendance_management.AttendanceManagement;
import com.app.myapplication.mainActivity.CustomAdapter_Main;
import com.app.myapplication.models.Employee;
import com.app.myapplication.models.Task;
import com.app.myapplication.profile.ProfileActivity;
import com.app.myapplication.staff_management.StaffManagement;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static OkHttpClient client;
    private static JSONObject jsnobject;
    private static ArrayList<Task> listdata;
    private static JSONArray jsonArray;
    private static Task entity;
    private static String json;
    private TextView username,Email,staff_num,pto_num,rec_num;
    private CustomAdapter_Main customAdapterMain;
    private static RecyclerView recyclerView;
    private static String myResponse;
    private static String ImageUrl_String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Changing action bar color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        recyclerView = findViewById(R.id.recyclerView);
        //initialize Task_array
        listdata = new ArrayList<Task>();

        //buttons
        CardView manage_staff = findViewById(R.id.manage_staff);
        CardView manage_attendance = findViewById(R.id.manage_attendance);
        CardView manage_record = findViewById(R.id.manage_record);
        CardView manage_PTOs = findViewById(R.id.manage_PTOs);

        //buttons actionlistener
        manage_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StaffManagement.class);
                startActivity(intent);
            }
        });

        manage_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AttendanceManagement.class);
                startActivity(intent);
            }
        });
        manage_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordManagement.class);
                startActivity(intent);
            }
        });
        manage_PTOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PtoManagement.class);
                startActivity(intent);
            }
        });
        //Creating Http client
        client = new OkHttpClient.Builder()
                .cookieJar(new MycookieJar(){
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        MycookieJar.cookieStore.put(HttpUrl.parse("http://10.0.2.2:5000/"), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://10.0.2.2:5000/"));
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();

        //Getting & Displaying Employee Data from API
        EmployeeObjectCreationFromApi();

        //Getting & Displaying Task Data from API
        TaskObjectCreationFromAPI();

    }

    public void TaskObjectCreationFromAPI(){
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000" + "/dashboard")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                //Converting jsonData string into JSON object
                try {
                    jsnobject = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Getting tasks JSON array from the JSON object
                try {
                    jsonArray = jsnobject.getJSONArray("tasks");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                //Converting jsonObject string into task object
                for (int i=0;i<jsonArray.length();i++){
                    try {
                        json = jsonArray.get(i).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    entity = gson.fromJson(json,Task.class);
                    //Creating Arraylist of Object task
                    listdata.add(entity);
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Displaying Data
                        DisplayDataFromTaskArray(listdata);
                    }
                });
            }
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }
        });
    }

    public void EmployeeObjectCreationFromApi(){
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000" + "/current_user")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //Getting response from api
                myResponse = response.body().string();
                //Creating gson object
                Gson gson = new Gson();
                //Converting Json to Object
                DisplayDataFromEmployeeObject(gson.fromJson(myResponse, Employee.class));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void DisplayDataFromEmployeeObject(@NonNull Employee current_user){

        //Getting Textview from activity
        username = findViewById(R.id.username_profile);
        Email = findViewById(R.id.Email);
        recyclerView = findViewById(R.id.recyclerView);
        staff_num = findViewById(R.id.staff_num);
        pto_num = findViewById(R.id.pto_num);
        rec_num = findViewById(R.id.rec_num);

        //Changing the content of Textview
        username.setText(current_user.getName());
        Email.setText(current_user.getPosition());
        staff_num.setText(current_user.getNumEmp().toString());
        pto_num.setText(current_user.getNumPtoreq().toString());
        rec_num.setText(current_user.getNumRecordreq().toString());

        //Getting Profile pic
        ImageUrl_String="http://10.0.2.2:5000/static/pics/"+current_user.getId()+".jpg";
        new DownloadImageProfile((ImageView) findViewById(R.id.profile_pic))
                .execute(ImageUrl_String);

    }

    public void DisplayDataFromTaskArray(ArrayList<Task> listdata){
        //CustomAdapter for task recycler_view
        customAdapterMain = new CustomAdapter_Main(MainActivity.this,listdata);
        recyclerView.setAdapter(customAdapterMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void Profile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    }
