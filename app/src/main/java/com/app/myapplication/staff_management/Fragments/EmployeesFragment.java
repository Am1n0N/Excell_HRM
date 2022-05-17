package com.app.myapplication.staff_management.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.models.Employee;
import com.app.myapplication.staff_management.Fragments.Adapeters.CustomAdapter_employees;
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

public class EmployeesFragment extends Fragment implements OnCardClickListener {
    View view;
    //API STUFF
    private static OkHttpClient client;
    private static JSONObject jsnobject;
    private static ArrayList<Employee> listdata;
    private static JSONArray jsonArray;
    private static String json;
    private static Employee entity;
    //RECYCLE VIEW STUFF
    private static RecyclerView recyclerView;
    private Context mContext;
    private CustomAdapter_employees customAdapter;
    //EDITTEXTS & BUTTONS INPUTS
    private EditText name, lastName, email, position, address, phoneNumber;
    private Button Modify, Delete, Archive;
    //Setting Server Url
    private static String BASE_URL = "http://10.0.2.2:5000/";
    private String myResponse;

    String selectedEmployee_ID, selectedEmployee_Name, selectedEmployee_LastName, selectedEmployee_Address, selectedEmployee_Position, selectedEmployee_PhoneNumber, selectedEmployee_Email;

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
        view = inflater.inflate(R.layout.fragment_employees, container, false);

        //initialization of recycleView
        recyclerView = view.findViewById(R.id.recycleView_employees);
        //initialization of EDITEXTS & BUTTONS
        name = view.findViewById(R.id.EmployeeName_input);
        lastName = view.findViewById(R.id.EmployeeLastName_input);
        email = view.findViewById(R.id.EmployeeEmail_input);
        position = view.findViewById(R.id.EmployeePosition_input);
        address = view.findViewById(R.id.EmployeeAddress_input);
        phoneNumber = view.findViewById(R.id.EmployeeNumber_input);

        Modify = view.findViewById(R.id.Employee_Modify);
        Delete = view.findViewById(R.id.Employee_DELETE);
        Archive = view.findViewById(R.id.Employee_Archive);

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
        listdata = new ArrayList<Employee>();

        //Displaying Employees
        EmployeeObjectCreationFromAPI();

        name.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        position.setEnabled(false);
        address.setEnabled(false);
        phoneNumber.setEnabled(false);

        Modify.setEnabled(false);
        Delete.setEnabled(false);
        Archive.setEnabled(false);

        return view;
    }

    public void EmployeeObjectCreationFromAPI() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/listEmployees")
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
                    jsonArray = jsnobject.getJSONArray("Employees");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                //Converting jsonObject string into task object
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        json = jsonArray.get(i).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    entity = gson.fromJson(json, Employee.class);
                    //Creating Arraylist of Object task
                    listdata.add(entity);
                }
                //Displaying Data
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayDataFromTaskArray(listdata);
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }
        });
    }

    public void DisplayDataFromTaskArray(ArrayList<Employee> listdata) {
        //CustomAdapter for task recycler_view
        customAdapter = new CustomAdapter_employees(this,getActivity().getApplicationContext(), listdata);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @Override
    public void onCardClick(int id_, String name_, String lastname_, String email_, String Address_, String position_,String phoneNumber_) {
        //Enabling EditText and Buttons
        name.setEnabled(true);
        lastName.setEnabled(true);
        email.setEnabled(true);
        position.setEnabled(true);
        address.setEnabled(true);
        phoneNumber.setEnabled(true);

        Modify.setEnabled(true);
        Delete.setEnabled(true);
        Archive.setEnabled(true);


        //Filling EditText From RecycleView
        name.setText(name_);
        lastName.setText(lastname_);
        email.setText(email_);
        position.setText(position_);
        address.setText(Address_);
        phoneNumber.setText(phoneNumber_);

        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyEmployeeFromServeur(id_, name.getText().toString().trim(),
                        lastName.getText().toString().trim(),
                        email.getText().toString().trim(),
                        address.getText().toString().trim(),
                        position.getText().toString().trim(),
                        phoneNumber.getText().toString().trim());
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteEmployeeFromServeur(id_);
            }
        });

        Archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArchiveEmployeeFromServeur(id_);
            }
        });
    }

    private void ModifyEmployeeFromServeur(int id_, String name_, String lastname_, String email_, String Address_, String position_,String phoneNumber_){
        RequestBody formBody = new FormBody.Builder()
                .add("id",String.valueOf(id_))
                .add("name", name_)
                .add("last_name",lastname_)
                .add("email", email_)
                .add("phone_number",phoneNumber_ )
                .add("address", Address_)
                .add("position", position_)
                .build();

        Request request = new Request.Builder()
                .url( BASE_URL + "/modify")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {

                    myResponse = response.body().string();

                    if (myResponse.equals("Employee modified successfully")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Employee modified successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void DeleteEmployeeFromServeur(int id_){
        RequestBody formBody = new FormBody.Builder()
                .add("id",String.valueOf(id_))
                .build();

        Request request = new Request.Builder()
                .url( BASE_URL + "/delete")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {

                    myResponse = response.body().string();

                    if (myResponse.equals("Employee removed successfully")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Employee modified successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void ArchiveEmployeeFromServeur(int id_){
        RequestBody formBody = new FormBody.Builder()
                .add("id",String.valueOf(id_))
                .build();

        Request request = new Request.Builder()
                .url( BASE_URL + "/archive")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {

                    myResponse = response.body().string();

                    if (myResponse.equals("Employee archived successfully")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Employee modified successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}


























