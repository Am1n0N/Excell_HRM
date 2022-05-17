package com.app.myapplication.staff_management.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.myapplication.FileUtils;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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

public class Add_employeeFragment extends Fragment {

    //getting activity
    private Activity activity = getActivity();

    //Server URL
    private String Base_url="http://10.0.2.2:5000/";

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private final int READ_REQUEST_CODE  = 42;

    //Uri to store the image uri
    private Uri filePath,picturePath;

    final MediaType MEDIA_TYPE_JPG = MediaType.parse("image");
    final MediaType MEDIA_TYPE_PDF =MediaType.parse("application/pdf");
    private EditText Firstname,Lastname,NIN,Birthday,Position,Hiring_date,Phone_number,Email,Username,Password,Signing_date,Address;
    private Button Add_picture,Add_file,Create_Emp;
    private CheckBox IsAdmin;

    private static OkHttpClient client;

    private Context mContext;
    public Add_employeeFragment() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);
        //Requesting storage permission
        //permission
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        // <-- Initialization of EditText -->
        Firstname = view.findViewById(R.id.Firstname_input);
        Lastname = view.findViewById(R.id.Lastname_input);
        NIN = view.findViewById(R.id.NIN_input);
        Birthday = view.findViewById(R.id.Birthday_input);
        Position = view.findViewById(R.id.Postion_input);
        Address = view.findViewById(R.id.Address_input);
        Hiring_date = view.findViewById(R.id.Hiringdate_input);
        Phone_number = view.findViewById(R.id.Phonenumber_input);
        Email = view.findViewById(R.id.Email_input);
        Username = view.findViewById(R.id.EmployeeUsername_input);
        Password = view.findViewById(R.id.EmployeePassword_input);
        Signing_date = view.findViewById(R.id.Singingdate_input);

        // <-- Initialization of Buttons -->
        Add_picture = view.findViewById(R.id.Add_picture);
        Add_file = view.findViewById(R.id.add_file);
        Create_Emp = view.findViewById(R.id.Create_emp);

        // <-- Initialization of Checkbox -->
        IsAdmin = view.findViewById(R.id.checkBox);

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

        //Defining ActionListeners
        Add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        Add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        Create_Emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });


        return view;
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    // <-- Methods for Updating profile image -->
    private void imageChooser() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && data != null && data.getData() != null) {
            filePath = data.getData();
        }
        else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data.getData() != null){
            picturePath = data.getData();
        }
    }

    public void uploadMultipart() {

        if(picturePath == null || filePath == null){
            Toast.makeText(mContext, "Picture OR File missing...", Toast.LENGTH_LONG).show();
            return;
        }
        final File imageFile = FileUtils.getFile(mContext,picturePath);
        final File pdgFile = FileUtils.getFile(mContext,filePath);

        String imageName = imageFile.getName();
        String PDFname = pdgFile.getName();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",Firstname.getText().toString().trim())
                .addFormDataPart("last_name",Lastname.getText().toString().trim())
                .addFormDataPart("NIN",NIN.getText().toString().trim())
                .addFormDataPart("email",Email.getText().toString().trim())
                .addFormDataPart("phone_number",Phone_number.getText().toString().trim())
                .addFormDataPart("address",Address.getText().toString().trim())
                .addFormDataPart("birthday",Birthday.getText().toString().trim())
                .addFormDataPart("position",Position.getText().toString().trim())
                .addFormDataPart("hiring_date",Hiring_date.getText().toString().trim())
                .addFormDataPart("username",Username.getText().toString().trim())
                .addFormDataPart("password",Password.getText().toString().trim())
                .addFormDataPart("signing_date",Signing_date.getText().toString().trim())
                .addFormDataPart("isAdmin", String.valueOf(IsAdmin.isChecked()))
                .addFormDataPart("picture", imageName, RequestBody.create(MEDIA_TYPE_JPG, imageFile ))
                .addFormDataPart("pdf_file", PDFname, RequestBody.create(MEDIA_TYPE_PDF,pdgFile))
                .build();

        Request request = new Request.Builder()
                .addHeader("Content-type", String.valueOf(MEDIA_TYPE_JPG))
                .url(Base_url + "/signup")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String myResponse = response.body().string();

                if (myResponse.equals("Name can't be empty")) {
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Name can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Last name can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Last name can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Email can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Email can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("NIN can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "NIN can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Phone number can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Phone number can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Address can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Address can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Birthday can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Birthday can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Hiring date can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Hiring date can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Username can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Username can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Password can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Password can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Picture can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Picture can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("File can't be empty")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "File can't be empty...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(myResponse.equals("Username already exists")){
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Username already exists...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Last name can't be empty...", Toast.LENGTH_LONG).show();
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