package com.app.myapplication.Record_management.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.Record_management.Fragments.Adapters.CustomAdapter_RecordEmployees;
import com.app.myapplication.attendance_management.Fragments.OnClickListener;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Add_EntryFragment extends Fragment implements OnClickListener {
    View view;
    //API STUFF
    private OkHttpClient client;
    private JSONObject jsnobject;
    private ArrayList<Employee> listEmployee;
    private JSONArray jsonArray_employee,jsonArray_employeeSelected;
    private  String json;
    private  Employee entity,SelectedEmployee;
    //RECYCLE VIEW STUFF
    private static RecyclerView recyclerView_Employees;
    private Context mContext;
    private CustomAdapter_RecordEmployees customAdapterEmployee;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    //EDITTEXTs & Buttons & Layouts
    LinearLayout linearLayout;
    EditText title,description;
    CheckBox isPublic,AllowResponce;
    Button Create_entry;

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
        view = inflater.inflate(R.layout.fragment_add_entry, container, false);
        //initialization
        linearLayout = view.findViewById(R.id.Record_linear);
        linearLayout.setVisibility(View.INVISIBLE);
        //initialization of recycleView
        recyclerView_Employees = view.findViewById(R.id.recycleView_RecordEmployees);
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
                    .addPathSegment("set_attendance")
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
                        jsonArray_employeeSelected = jsnobject.getJSONArray("employeeSelected");
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

                    try {
                        json = jsonArray_employeeSelected.get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SelectedEmployee = gson.fromJson(json, Employee.class);
                    //Displaying Data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DisplayDataFromEmployeeArray(listEmployee);
                            DisplayDataFromHistoryArray(SelectedEmployee);
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
                    .addPathSegment("set_attendance")
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
        customAdapterEmployee = new CustomAdapter_RecordEmployees(this,getActivity().getApplicationContext(), list);
        recyclerView_Employees.setAdapter(customAdapterEmployee);
        recyclerView_Employees.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void DisplayDataFromHistoryArray(Employee SelectedEmployee){
        linearLayout.setVisibility(View.VISIBLE);
        title = view.findViewById(R.id.EntryTitle_input);
        description = view.findViewById(R.id.EntryDescription_input);
        isPublic = view.findViewById(R.id.isPublic_checkbox);
        AllowResponce = view.findViewById(R.id.AllowResponce_checkBox);
        Create_entry = view.findViewById(R.id.create_entry);

        Create_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDataViaAPI(SelectedEmployee.getId().toString(),
                        title.getText().toString(),
                        description.getText().toString().trim(),
                        String.valueOf(isPublic.isChecked()),
                        String.valueOf(AllowResponce.isChecked()));
            }
        });



    }

    @Override
    public void onCardClick(int id_) {
        //initialize Employee_array
        listEmployee = new ArrayList<Employee>();
        DataCreationFromAPI(String.valueOf(id_));
    }

    private void SendDataViaAPI(String id, String title_, String description_, String isPulic_, String AllowResponce_){
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(5000)
                .addPathSegment("add_entry")
                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("employee", id)
                .add("title", title_)
                .add("description", description_)
                .add("isPublic", isPulic_)
                .add("allowResponses", AllowResponce_)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Error...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                myResponse = response.body().string();
                if (myResponse.equals("Entry added successfully.")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Entry added successfully.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Connection Error...",Toast.LENGTH_SHORT).show();
                    }
                });}
            }
        });
    }
}
