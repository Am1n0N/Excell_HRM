package com.app.myapplication.Record_management.Fragments.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.Record_management.Request_view;
import com.app.myapplication.models.RequestObj;

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

public class CustomAdapter_Request extends RecyclerView.Adapter<CustomAdapter_Request.ViewHolder> {
    private Context context;
    private ArrayList<RequestObj> History;
    private String BASE_URL = "http://127.0.0.1:5000";
    private Activity activity;

    public CustomAdapter_Request(Activity activity, Context context, ArrayList<RequestObj> History) {
        this.context = context;
        this.activity = activity;
        this.History = History;
    }

    @NonNull
    @Override
    public CustomAdapter_Request.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_recycle_view_row, parent, false);
        return new CustomAdapter_Request.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.day.setText(History.get(position).getDate());
        holder.title.setText(History.get(position).getTitle());
        holder.Requester.setText(History.get(position).getRequester());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Create request view and put here
                Intent intent = new Intent(context, Request_view.class);
                intent.putExtra("id", History.get(position).getId().toString());
                context.startActivity(intent);
            }
        });
        holder.Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Approce_request(History.get(position).getId().toString());
            }
        });
        holder.Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Decline_request(History.get(position).getId().toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return History.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, title, Requester, view, Approve, Decline;

        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.Date_Request);
            title = itemView.findViewById(R.id.Title_Request);
            Requester = itemView.findViewById(R.id.Requester_Request);
            view = itemView.findViewById(R.id.ViewRequest);
            Approve = itemView.findViewById(R.id.ApproveRequest);
            Decline = itemView.findViewById(R.id.DeclineRequest);
        }
    }

    private void Approce_request(String id) {
        OkHttpClient client = new OkHttpClient.Builder()
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
                if (response.isSuccessful()) {
                    String Myresponse = response.body().string();
                    if (Myresponse.equals("Request approved successfully.")) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Request approved successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });


    }

    private void Decline_request(String id) {
        {
            OkHttpClient client = new OkHttpClient.Builder()
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
                    if (response.isSuccessful()) {
                        String Myresponse = response.body().string();
                        if (Myresponse.equals("Request declined successfully.")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Request declined successfully.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });


        }
    }
}