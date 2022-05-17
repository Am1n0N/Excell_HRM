package com.app.myapplication.attendance_management.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.attendance_management.Fragments.Adapters.CustomAdapter_AttendanceEmployees;
import com.app.myapplication.attendance_management.Fragments.Adapters.CustomAdapter_History;
import com.app.myapplication.models.AttendanceHistory;
import com.app.myapplication.models.Employee;
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

public class Dashboard_AttendanceFragment extends Fragment implements OnClickListener {
    View view;
    //API STUFF
    private  OkHttpClient client;
    private  JSONObject jsnobject;
    private  ArrayList<Employee> listEmployee;
    private  ArrayList<AttendanceHistory> listHistory;
    private  JSONArray jsonArray_employee,jsonArray_history;
    private  String json;
    private  Employee entity;
    private  AttendanceHistory HistoryEntity;

    //RECYCLE VIEW STUFF
    private static RecyclerView recyclerView_Employees,recyclerView_History;
    private Context mContext;
    private CustomAdapter_AttendanceEmployees customAdapterEmployee;
    private CustomAdapter_History customAdapterHistory;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentattendance_dashboard, container, false);

        //initialization of recycleView
        recyclerView_Employees = view.findViewById(R.id.recycleView_AttendanceEmployees);
        recyclerView_History = view.findViewById(R.id.attendanceHistory_recycleView);
        //Creating Http client
        client = new OkHttpClient.Builder()
                .cookieJar(new MycookieJar() {
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

        //initialize Employee_array
        listEmployee = new ArrayList<Employee>();
        listHistory = new ArrayList<AttendanceHistory>();
        //Displaying Data
        DataCreationFromAPI(null);


        return view;
    }

    private void DataCreationFromAPI(String id) {
        if (!(id==null)) {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_URL)
                    .port(5000)
                    .addPathSegment("attendance_dashboard")
                    .addQueryParameter("id",id)
                    .build();
            Request request = new Request.Builder()
                    .url(httpUrl)
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
                        jsonArray_history = jsnobject.getJSONArray("History");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    //Converting jsonObject string into task object
                    for (int i = 0; i < jsonArray_history.length(); i++) {
                        try {
                            json = jsonArray_history.get(i).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HistoryEntity = gson.fromJson(json, AttendanceHistory.class);
                        //Creating Arraylist of Object task
                        listHistory.add(HistoryEntity);
                    }
                    //Displaying Data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayDataFromHistoryArray(listHistory);
                        }
                    });
                }
                public void onFailure(Call call, IOException e) {
                    e.getStackTrace();
                }
            });
        }else {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_URL)
                    .port(5000)
                    .addPathSegment("attendance_dashboard")
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
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
                        jsonArray_employee = jsnobject.getJSONArray("Employees");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    //Converting jsonObject string into task object
                    for (int i = 0; i < jsonArray_employee.length(); i++) {
                        try {
                            json = jsonArray_employee.get(i).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        entity = gson.fromJson(json, Employee.class);
                        //Creating Arraylist of Object task
                        listEmployee.add(entity);
                    }
                    //Displaying Data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayDataFromEmployeeArray(listEmployee);
                        }
                    });
                }
                public void onFailure(Call call, IOException e) {
                    e.getStackTrace();
                }
            });
        }

    }

    private void DisplayDataFromEmployeeArray(ArrayList<Employee> list){
        //CustomAdapter for Employee recycler_view
        customAdapterEmployee = new CustomAdapter_AttendanceEmployees(this,getActivity().getApplicationContext(), list);
        recyclerView_Employees.setAdapter(customAdapterEmployee);
        recyclerView_Employees.setLayoutManager(new LinearLayoutManager(mContext));
    }
    private void DisplayDataFromHistoryArray(ArrayList<AttendanceHistory> list){
        //CustomAdapter for Employee recycler_view
        customAdapterHistory = new CustomAdapter_History(getActivity().getApplicationContext(), list);
        recyclerView_History.setAdapter(customAdapterHistory);
        recyclerView_History.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onCardClick(int id_) {
        listHistory = new ArrayList<AttendanceHistory>();
        //Displaying Data
        recyclerView_Employees = view.findViewById(R.id.recycleView_AttendanceEmployees);
        recyclerView_History = view.findViewById(R.id.attendanceHistory_recycleView);
        DataCreationFromAPI(String.valueOf(id_));
    }
}
