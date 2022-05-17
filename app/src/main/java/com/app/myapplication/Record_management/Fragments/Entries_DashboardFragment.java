package com.app.myapplication.Record_management.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.Record_management.Fragments.Adapters.CustomAdapter_Entry;
import com.app.myapplication.Record_management.Fragments.Adapters.CustomAdapter_RecordEmployees;
import com.app.myapplication.attendance_management.Fragments.OnClickListener;
import com.app.myapplication.models.Employee;
import com.app.myapplication.models.EntryHistory;
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

public class Entries_DashboardFragment extends Fragment implements OnClickListener {
    View view;
    //API STUFF
    private OkHttpClient client;
    private JSONObject jsnobject;
    private ArrayList<Employee> listEmployee;
    ArrayList<EntryHistory> listHistory;
    private JSONArray jsonArray_employee,jsonArray_entryHistory;
    private  String json;
    private  Employee entity;
    private EntryHistory entryHistory;
    //RECYCLE VIEW STUFF
    private RecyclerView recyclerView_Employees,recyclerView_Entry;
    private Context mContext;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    //EDITTEXTs & Buttons & Layouts
    LinearLayout linearLayout;


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
        view = inflater.inflate(R.layout.fragment_entries_dashboard, container, false);
        //initialization
        linearLayout = view.findViewById(R.id.Entries_linear);
        linearLayout.setVisibility(View.INVISIBLE);

        //initialization of recycleView
        recyclerView_Employees = (RecyclerView) view.findViewById(R.id.recycleView_EntriesList);


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
        //initialize data arrays
        listEmployee = new ArrayList<Employee>();
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
                    .addPathSegment("entries")
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
                        jsonArray_employee = jsnobject.getJSONArray("Employees");
                        jsonArray_entryHistory = jsnobject.getJSONArray("History");
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
                    for (int i = 0; i < jsonArray_entryHistory.length(); i++) {
                        try {
                            json = jsonArray_entryHistory.get(i).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        entryHistory = gson.fromJson(json, EntryHistory.class);
                        //Creating Arraylist of Object task
                        listHistory.add(entryHistory);
                        System.out.println("json:");
                        System.out.println(json);
                    }
                    //Displaying Data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayDataFromEmployeeArray(listEmployee);
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
                    .addPathSegment("entries")
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
        CustomAdapter_RecordEmployees customAdapterEmployee = new CustomAdapter_RecordEmployees(this, getActivity().getApplicationContext(), list);
        recyclerView_Employees.setAdapter(customAdapterEmployee);
        recyclerView_Employees.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void DisplayDataFromHistoryArray(ArrayList<EntryHistory> list){
        linearLayout.setVisibility(View.VISIBLE);
        //initialization of recycleView
        recyclerView_Entry = view.findViewById(R.id.EntryHistory_recycleView);
        //CustomAdapter for entry recycler_view
        CustomAdapter_Entry customAdapterEntry = new CustomAdapter_Entry(getActivity().getApplicationContext(), list);
        recyclerView_Entry.setAdapter(customAdapterEntry);
        recyclerView_Entry.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void onCardClick(int id_) {
        linearLayout.setVisibility(View.VISIBLE);
        //initialize data arrays
        listEmployee = new ArrayList<Employee>();
        listHistory = new ArrayList<EntryHistory>();
        DataCreationFromAPI(String.valueOf(id_));
    }

    private void SendDataViaAPI(String id) {}
}
