package com.app.myapplication.Record_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.models.RequestObj;
import com.google.gson.Gson;

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

public class Request_view extends AppCompatActivity {
    //API STUFF
    private OkHttpClient client;
    private JSONObject jsnobject,jsonArray_request;
    private  String json;
    private RequestObj entity;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    //EDITTEXTs & Buttons
    private String RequestId;
    private Button Accept_button,Decline_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_activity);

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

        //Getting id from Intent
        RequestId =getIntent().getStringExtra("id");
        //Displaying Data
        DataCreationFromAPI(RequestId);



    }

    private void DataCreationFromAPI(String id) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(5000)
                .addPathSegment("request")
                .addQueryParameter("id",id)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {e.printStackTrace();}

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                myResponse = response.body().string();
                //Converting jsonData string into JSON object
                try {
                    jsnobject = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Getting tasks JSON array from the JSON object
                try {
                    jsonArray_request = jsnobject.getJSONObject("request");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                //Converting jsonObject string into task object
                json = jsonArray_request.toString();

                entity = gson.fromJson(json, RequestObj.class);
                //Displaying Data
                Request_view.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayDataFromEntryObject(entity);
                    }
                });
            }
        });
    }
    private void DisplayDataFromEntryObject(RequestObj request){
        //initialization
        TextView requestTitle = findViewById(R.id.Request_title);
        TextView requestDescription = findViewById(R.id.Request_description);
        TextView requestDate = findViewById(R.id.RequestDate);
        TextView requestEmployee = findViewById(R.id.RequestEmployee_fullname);
        TextView Requester = findViewById(R.id.Request_requester);
        TextView requestClosed = findViewById(R.id.Request_closed);
        Accept_button = findViewById(R.id.accept_requesterr);
        Decline_button = findViewById(R.id.decline_requesterr);
        //Displaying data
        requestTitle.setText(request.getTitle());
        requestDescription.setText(request.getDescription());
        requestDate.setText(request.getDate());
        requestEmployee.setText(request.getName());
        Requester.setText(request.getRequester().toString());
        requestClosed.setText(request.getIsClosed().toString());


        Accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accept_request(request.getId().toString());
            }
        });
        Decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Decline_request(request.getId().toString());
            }
        });
    }




    private void Accept_request(String id){
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/approve_request")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    myResponse= response.body().string();
                    if (response.equals("Request approved successfully.")){
                        Request_view.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Request_view.this,"Request approved successfully.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void Decline_request(String id){
        RequestBody formBody = new FormBody.Builder()
                .add("id", id)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/decline_request")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    myResponse= response.body().string();
                    if (response.equals("Request declined successfully.")){
                        Request_view.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Request_view.this,"Request declined successfully.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }


}
