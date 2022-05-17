package com.app.myapplication.login;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.myapplication.MainActivity;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;

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

public class LoginActivity extends AppCompatActivity {
    OkHttpClient client;
    EditText Password_input,Username_input;
    Button button;
    String myResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));

        Password_input = findViewById(R.id.Password_input);
        Username_input = findViewById(R.id.Username_input);
        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    whenSendPostRequest_thenCorrect(Username_input.getText().toString().trim(),Password_input.getText().toString().trim());
                } catch (IOException e) {
                    System.out.println(e.getStackTrace());
                }
            }
        });

    }
    public void whenSendPostRequest_thenCorrect(String username, String password) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/login")
                .post(formBody)
                .build();

        //Http client
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


        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){

                    myResponse = response.body().string();

                    if (!myResponse.equals("Authentication Failed")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"Auth ERROR. ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"Auth ERROR. ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
