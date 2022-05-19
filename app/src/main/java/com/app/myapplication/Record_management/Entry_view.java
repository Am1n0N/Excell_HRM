package com.app.myapplication.Record_management;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.DownloadImageProfile;
import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.Record_management.Fragments.Adapters.CustomAdapter_comment;
import com.app.myapplication.models.Comment;
import com.app.myapplication.models.Entry;
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

public class Entry_view extends AppCompatActivity {
    //API STUFF
    private OkHttpClient client;
    private JSONObject jsnobject,jsonArray_entry;
    private ArrayList<Comment> listComment;
    private JSONArray jsonArray_Comments;
    private  String json;
    private Comment comment_entity;
    private Entry entry_entity;
    private RecyclerView recyclerView_Comments;
    //Setting Server Url
    private static String BASE_URL = "10.0.2.2";
    private String myResponse;
    //EDITTEXTs & Buttons
    private String EntryId;
    private EditText CommentInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);

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
        listComment = new ArrayList<Comment>();
        //Getting id from Intent
        EntryId =getIntent().getStringExtra("id");
        //Displaying Data
        DataCreationFromAPI(EntryId);



    }

    private void DataCreationFromAPI(String id) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_URL)
                .port(5000)
                .addPathSegment("entry")
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
                    jsonArray_entry = jsnobject.getJSONObject("entry");
                    jsonArray_Comments = jsnobject.getJSONArray("comments");
                    System.out.println(jsonArray_Comments);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                //Converting jsonObject string into task object
                for (int i = 0; i < jsonArray_Comments.length(); i++) {
                    try {
                        json = jsonArray_Comments.get(i).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    comment_entity = gson.fromJson(json, Comment.class);
                    //Creating Arraylist of Object task
                    listComment.add(comment_entity);
                }

                json = jsonArray_entry.toString();

                entry_entity = gson.fromJson(json, Entry.class);
                //Displaying Data
                Entry_view.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayDataFromEntryObject(entry_entity);
                        DisplayDataFromCommentArray(listComment);
                    }
                });
            }
        });
    }
    private void DisplayDataFromEntryObject(Entry entry){
        //initialization
        TextView entryTitle = findViewById(R.id.Entry_title);
        TextView entryDescription = findViewById(R.id.Entry_description);
        TextView entryAllowResponses = findViewById(R.id.Entry_allowResponses);
        TextView entryDate = findViewById(R.id.EntryDate);
        TextView entryEmployee = findViewById(R.id.EntryEmployee_fullname);
        TextView entryLogger = findViewById(R.id.EntryLogger);
        TextView entryRequested = findViewById(R.id.Entry_requested);
        TextView entryPublic = findViewById(R.id.Entry_public);
        EditText Comment_input = findViewById(R.id.Comment_input);
        ImageView user_image = findViewById(R.id.CurrentUser_img);
        //Displaying data
        entryTitle.setText(entry.getTitle());
        entryDescription.setText(entry.getDescription());
        entryEmployee.setText(entry.getName());
        entryLogger.setText(entry.getLogger());
        entryAllowResponses.setText(entry.getAllowResponse().toString());
        entryPublic.setText(entry.getIsPublic().toString());
        entryRequested.setText(entry.getWasRequested().toString());
        entryDate.setText(entry.getDate().toString());

        Comment_input.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    SendComment(entry.getId().toString(),Comment_input.getText().toString());
                    Toast.makeText(Entry_view.this, "Comment", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        String imageUrl_String = "http://10.0.2.2:5000/static/pics/" + entry.getCurrentUserId() + ".jpg";
        new DownloadImageProfile(user_image).execute(imageUrl_String);


    }

    private void DisplayDataFromCommentArray(ArrayList<Comment> list){
        //initialization of recycleView
        recyclerView_Comments = findViewById(R.id.recycleView_comments);
        //CustomAdapter for entry recycler_view
        CustomAdapter_comment customAdapterComment = new CustomAdapter_comment(this, list);
        recyclerView_Comments.setAdapter(customAdapterComment);
        recyclerView_Comments.setLayoutManager(new LinearLayoutManager(this));
    }

    private void SendComment(String id, String comment){
        RequestBody formBody = new FormBody.Builder()
                .add("entry_id", id)
                .add("comment", comment)
                .build();

        Request request = new Request.Builder()
                .url( BASE_URL + "/add_comment")
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
                    if(response.equals("Comment added successfully.")){
                        Entry_view.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Entry_view.this, "Comment Sent successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
