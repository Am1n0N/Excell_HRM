package com.app.myapplication.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.app.myapplication.DownloadImageProfile;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.models.Employee;
import com.google.gson.Gson;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView NIN,Email,phone,hiring_date,fullname,birthday,adresse,username;
    private ImageView profile_img;
    private String BASE_URL;
    private Uri uri;
    private Bitmap bmp;
    private final int READ_REQUEST_CODE  = 42;
    private String ImageUrl_String;
    private Button Change_pic,change_password;
    private static String myResponse;
    private static OkHttpClient client;
    private static Employee employee;
    final MediaType MEDIA_TYPE_JPG = MediaType.parse("image");

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText current_password,password,cpassword;
    private TextView ok_button,cancel_button;
    private EditText phone_number,cphone_number;
    private EditText email,cemail;
    private EditText username_input,cusername_input;
    private TextView Addemail,Addphone,Addusername;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        //permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        //ActionBar Settings
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Profile");

        //Setting Server Url
        BASE_URL="http://10.0.2.2:5000/";

        //Buttons Setting
        Change_pic = findViewById(R.id.Change_button);
        change_password = findViewById(R.id.Change_Password);

        Addphone = findViewById(R.id.Add_phone);
        Addusername = findViewById(R.id.Add_username);
        Addemail = findViewById(R.id.Add_email);

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
        //Displaying Profile Data
        EmployeeObjectCreationFromApi();

        //Change_pic button actionListener
        Change_pic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewPasswordDialog();
            }
        });
        Addphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewPhoneNumberDialog();
            }
        });
        Addemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewEmailDialog();
            }
        });
        Addusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewUsernameDialog();
            }
        });

    }

    // <-- Methods for Displaying profile Data -->

    private void EmployeeObjectCreationFromApi(){
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
                employee = gson.fromJson(myResponse, Employee.class);
                DisplayDataFromEmployeeObject(employee);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void DisplayDataFromEmployeeObject(Employee current_user){

        //Getting Textview from activity
        NIN = findViewById(R.id.NIN);
        Email = findViewById(R.id.Email);
        phone = findViewById(R.id.phone);
        hiring_date = findViewById(R.id.hiring_date);
        fullname = findViewById(R.id.Fullname);
        birthday = findViewById(R.id.birthday);
        adresse = findViewById(R.id.adresse);
        profile_img = findViewById(R.id.profile_img);
        username = findViewById(R.id.username);

        //Changing the content of Textview
        NIN.setText("NIN: "+current_user.getNin());
        Email.setText(current_user.getEmail());
        phone.setText("+216 "+current_user.getPhoneNumber());
        hiring_date.setText("Hiring date: "+current_user.getHiringDate());
        fullname.setText(current_user.getName()+" "+current_user.getLastName());
        birthday.setText("Birthday: "+current_user.getBirthday());
        adresse.setText("Adresse: "+current_user.getAddress());
        //TODO ADD USERNAME HERE

        ImageUrl_String="http://10.0.2.2:5000/static/pics/"+current_user.getId()+".jpg";
        new DownloadImageProfile((ImageView) findViewById(R.id.profile_img))
            .execute(ImageUrl_String);
    }


    // <-- Methods for Updating profile image -->
    private void imageChooser() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();
                profile_img.setImageURI(uri);
                UploadPicture();


            }
        }
    }

    public void UploadPicture()  {

        if(uri == null){
            return;
        }
        final File imageFile = new File(getPath(uri));


        String imageName = imageFile.getName();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("picture", imageName,
                        RequestBody.create(MEDIA_TYPE_JPG, imageFile ))
                .build();


        Request request = new Request.Builder()
                .addHeader("Content-type", String.valueOf(MEDIA_TYPE_JPG))
                .url(BASE_URL + "/change_picture")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("Http Response:");
                System.out.println(response.body().string());
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    // <-- Dialog Popup for Updating Profile information -->
    public void CreateNewPasswordDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View passwordPopupView = getLayoutInflater().inflate(R.layout.newpassword_popup,null);

        //Getting Edittexts
        current_password = (EditText) passwordPopupView.findViewById(R.id.current_password);
        password = (EditText) passwordPopupView.findViewById(R.id.password);
        cpassword = (EditText) passwordPopupView.findViewById(R.id.cpassword);

        //Getting buttons
        ok_button = (TextView) passwordPopupView.findViewById(R.id.ok_button);
        cancel_button = (TextView) passwordPopupView.findViewById(R.id.cancel_button);

        dialogBuilder.setView(passwordPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(current_password.getText().toString().trim())){
                    Toast.makeText(ProfileActivity.this,"Enter your current password...",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody formBody = new FormBody.Builder()
                        .add("current_pass", current_password.getText().toString().trim())
                        .add("password", password.getText().toString().trim())
                        .add("cpassword", cpassword.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url( BASE_URL + "/change_password")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){

                            myResponse = response.body().string();

                            if (myResponse.equals("Password don't match")) {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Password don't match. ",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(myResponse.equals("Please check your current password and try again.")){
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Please check your current password and try again. ",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Password changed successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    public void onFailure(Call call, IOException e) {
                        ProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this,"Connection ERROR. ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void CreateNewPhoneNumberDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View passwordPopupView = getLayoutInflater().inflate(R.layout.newphonenumber_popup,null);

        //Getting Edittexts
        phone_number = (EditText) passwordPopupView.findViewById(R.id.phone_number);
        cphone_number = (EditText) passwordPopupView.findViewById(R.id.cphone_number);

        //Getting buttons
        ok_button = (TextView) passwordPopupView.findViewById(R.id.ok_button);
        cancel_button = (TextView) passwordPopupView.findViewById(R.id.cancel_button);

        dialogBuilder.setView(passwordPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(phone_number.getText().toString().trim())){
                    Toast.makeText(ProfileActivity.this,"Enter your phone number...",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody formBody = new FormBody.Builder()
                        .add("phone_number", phone_number.getText().toString().trim())
                        .add("cphone_number", cphone_number.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url( BASE_URL + "/edit_phone_number")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){

                            myResponse = response.body().string();

                            if (myResponse.equals("Phone numbers don't match.")) {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Phone numbers don't match.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(myResponse.equals("Phone number already exists.")){
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Phone number already exists.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Phone number changed successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    public void onFailure(Call call, IOException e) {
                        ProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this,"Connection ERROR. ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void CreateNewEmailDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View passwordPopupView = getLayoutInflater().inflate(R.layout.newemail_popup,null);

        //Getting Edittexts
        email = (EditText) passwordPopupView.findViewById(R.id.email_input);
        cemail = (EditText) passwordPopupView.findViewById(R.id.cemail_input);

        //Getting buttons
        ok_button = (TextView) passwordPopupView.findViewById(R.id.ok_button);
        cancel_button = (TextView) passwordPopupView.findViewById(R.id.cancel_button);

        dialogBuilder.setView(passwordPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(email.getText().toString().trim())){
                    Toast.makeText(ProfileActivity.this,"Enter your email...",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody formBody = new FormBody.Builder()
                        .add("phone_number", phone_number.getText().toString().trim())
                        .add("cphone_number", cphone_number.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url( BASE_URL + "/edit_email")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){

                            myResponse = response.body().string();

                            if (myResponse.equals("Email addresses don't match.")) {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Email addresses don't match.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(myResponse.equals("Email Address already exists.")){
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Email Address already exists.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Email Address changed successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    public void onFailure(Call call, IOException e) {
                        ProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this,"Connection ERROR. ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void CreateNewUsernameDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View passwordPopupView = getLayoutInflater().inflate(R.layout.newusername_popup,null);

        //Getting Edittexts
        username_input = (EditText) passwordPopupView.findViewById(R.id.username_input);
        cusername_input = (EditText) passwordPopupView.findViewById(R.id.cusername_input);

        //Getting buttons
        ok_button = (TextView) passwordPopupView.findViewById(R.id.ok_button);
        cancel_button = (TextView) passwordPopupView.findViewById(R.id.cancel_button);

        dialogBuilder.setView(passwordPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(username_input.getText().toString().trim())){
                    Toast.makeText(ProfileActivity.this,"Enter your username...",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username_input.getText().toString().trim())
                        .add("cusername", cusername_input.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url( BASE_URL + "/edit_username")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){

                            myResponse = response.body().string();

                            if (myResponse.equals("Usernames don't match.")) {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Usernames don't match.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(myResponse.equals("Username already exists.")){
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Username already exists.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this,"Username changed successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    public void onFailure(Call call, IOException e) {
                        ProfileActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this,"Connection ERROR. ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



}
