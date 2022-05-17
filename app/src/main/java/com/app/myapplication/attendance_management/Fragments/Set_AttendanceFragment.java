package com.app.myapplication.attendance_management.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.DownloadImageProfile;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.attendance_management.Fragments.Adapters.CustomAdapter_AttendanceEmployees;
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

public class Set_AttendanceFragment extends Fragment implements OnClickListener{
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
    private CustomAdapter_AttendanceEmployees customAdapterEmployee;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    //EDITTEXTs & Buttons & Layouts
    LinearLayout linearLayout;
    TextView name,lastname,signin,position;
    Button btn_Signin, btn_SignOut;
    ImageView image;
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
        view = inflater.inflate(R.layout.fragment_setattendance, container, false);

        //initialization
        linearLayout = view.findViewById(R.id.SetAttendance_linear);
        linearLayout.setVisibility(View.INVISIBLE);
        //initialization of recycleView
        recyclerView_Employees = view.findViewById(R.id.recycleView_SetAttendanceEmployees);
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
        customAdapterEmployee = new CustomAdapter_AttendanceEmployees(this,getActivity().getApplicationContext(), list);
        recyclerView_Employees.setAdapter(customAdapterEmployee);
        recyclerView_Employees.setLayoutManager(new LinearLayoutManager(mContext));
    }
    private void DisplayDataFromHistoryArray(Employee SelectedEmployee){
        linearLayout.setVisibility(View.VISIBLE);
        name = view.findViewById(R.id.setAttendence_name);
        lastname = view.findViewById(R.id.setAttendence_lastname);
        signin = view.findViewById(R.id.setAttendence_Signin);
        position = view.findViewById(R.id.setAttendence_Position);
        image = view.findViewById(R.id.setAttendance_image);
        btn_Signin = view.findViewById(R.id.setAttendence_IN);
        btn_SignOut = view.findViewById(R.id.setAttendence_OUT);

        name.setText(SelectedEmployee.getName());
        lastname.setText(SelectedEmployee.getLastName());
        signin.setText(SelectedEmployee.getIsActive().toString());
        position.setText(SelectedEmployee.getPosition());
        //Getting Profile pic
        String ImageUrl_String="http://10.0.2.2:5000/static/pics/"+SelectedEmployee.getId()+".jpg";
        new DownloadImageProfile(image)
                .execute(ImageUrl_String);

        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sign_in(SelectedEmployee.getId());
            }


        });

        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sign_out(SelectedEmployee.getId());
            }
        });
    }

    @Override
    public void onCardClick(int id_) {
        //initialize Employee_array
        listEmployee = new ArrayList<Employee>();
        DataCreationFromAPI(String.valueOf(id_));
    }
    private void Sign_in(Integer id) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(5000)
                .addPathSegment("sign_in")
                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("id", id.toString())
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
                if (myResponse.equals("Employee has signed in successfully.")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Employee has signed in successfully.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Employee has to sign out before signing in again.",Toast.LENGTH_SHORT).show();
                    }
                });}
            }
        });
    }

    private void Sign_out(Integer id) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(5000)
                .addPathSegment("sign_out")
                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("id", id.toString())
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
                if (myResponse.equals("Employee has signed out successfully.")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Employee has signed out successfully.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if (myResponse.equals("Employee is not Signed in")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Employee is not Signed in",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Connection Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
