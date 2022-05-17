package com.app.myapplication.staff_management.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.FileUtils;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.models.Contract;
import com.app.myapplication.staff_management.Fragments.Adapeters.CustomAdapter_contracts;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContractsFragment extends Fragment {
    private static OkHttpClient client;
    private static JSONObject jsnobject;
    private static ArrayList<Contract> listdata;
    private static JSONArray jsonArray;
    private static String json;
    private static Contract entity;
    private static RecyclerView recyclerView;
    private static String selected_contractID;
    private CustomAdapter_contracts customAdapter;
    private Context mContext;
    private Button add_pdf,Contract_button;
    private EditText Signing_date;
    public ContractsFragment() {
        // Required empty public constructor
    }
    //Setting Server Url
    private static String BASE_URL="http://10.0.2.2:5000/";
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    final MediaType MEDIA_TYPE_PDF =MediaType.parse("application/pdf");
    //Uri to store the image uri
    private Uri filePath;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);

        //initialization of recycleView
        recyclerView = view.findViewById(R.id.contract_recycleView);

        //initialization of buttons
        add_pdf= view.findViewById(R.id.add_pdf);
        Contract_button= view.findViewById(R.id.contract_change);

        //initialization of buttons
        Signing_date=view.findViewById(R.id.SingingDate_inputo);

        //Permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

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

        //initialize Task_array
        listdata = new ArrayList<Contract>();

        //Displaying Contracts
        ContractObjectCreationFromAPI();

        //add pdf button actionListener
        add_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        //Getting Selected Contract Id
        if(!(getArguments() ==null)) {
            System.out.println(getArguments().getInt("id"));
            selected_contractID = String.valueOf(getArguments().getInt("id"));
        }

        Contract_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });

        return view;
    }

    public void ContractObjectCreationFromAPI(){
        Request request = new Request.Builder()
                .url(BASE_URL + "/contracts")
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
                    jsonArray = jsnobject.getJSONArray("contracts");
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
                    entity = gson.fromJson(json, Contract.class);
                    //Creating Arraylist of Object task
                    listdata.add(entity);
                }
                //Displaying Data
                DisplayDataFromTaskArray(listdata);
            }
            public void onFailure(Call call, IOException e) {
                e.getStackTrace();
            }
        });
    }
    public void DisplayDataFromTaskArray(ArrayList<Contract> listdata){
        //CustomAdapter for task recycler_view
        customAdapter = new CustomAdapter_contracts(getFragmentManager(), getActivity().getApplicationContext(),listdata);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }
    //handling the file chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }

    public void uploadMultipart() {

        if(Signing_date.getText()==null){
            Toast.makeText(mContext, "Please Enter Signing Date...", Toast.LENGTH_LONG).show();
            return;
        }
        if (selected_contractID == null){
            Toast.makeText(mContext, "Please select a contract...", Toast.LENGTH_LONG).show();
            return;
        }
        if(filePath == null){
            Toast.makeText(mContext, "File missing...", Toast.LENGTH_LONG).show();
            return;
        }
        final File pdgFile = FileUtils.getFile(mContext,filePath);
        String PDFname = pdgFile.getName();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id",selected_contractID)
                .addFormDataPart("signing_date",Signing_date.getText().toString().trim())
                .addFormDataPart("file", PDFname, RequestBody.create(MEDIA_TYPE_PDF,pdgFile))
                .build();

        Request request = new Request.Builder()
                .addHeader("Content-type", String.valueOf(MEDIA_TYPE_PDF))
                .url(BASE_URL + "/change_contract")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String myResponse = response.body().string();

                if (myResponse.equals("Contract changed successfully.")) {
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Contract changed successfully.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "ERROR", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Connection ERROR...", Toast.LENGTH_LONG).show();
                    }
                });
                System.out.println(e.getMessage());
            }
        });

    }

}
