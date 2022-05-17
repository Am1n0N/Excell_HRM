package com.app.myapplication.staff_management.Fragments.Adapeters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.MycookieJar;
import com.app.myapplication.R;
import com.app.myapplication.models.Contract;
import com.app.myapplication.staff_management.Fragments.ContractsFragment;
import com.app.myapplication.staff_management.Fragments.ShowPdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomAdapter_contracts extends RecyclerView.Adapter<CustomAdapter_contracts.ViewHolder> {
    private final FragmentManager fragmentManager;
    private Context context;
    private ArrayList<Contract> Contracts;
    private String BASE_URL="http://10.0.2.2:5000";

    public CustomAdapter_contracts(FragmentManager fragmentManager, Context context, ArrayList<Contract> Contracts) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.Contracts = Contracts;
    }
    @NonNull
    @Override
    public CustomAdapter_contracts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.contract_recycle_view_row,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.Fullname.setText(Contracts.get(position).getFullname());
        holder.SigningDate.setText(Contracts.get(position).getSigningDate());
        holder.Position.setText(Contracts.get(position).getPosition());
        holder.Status.setText(Contracts.get(position).getStatus().toString());

        //Button onClickListener
        holder.ViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowPdf.class);
                CreatePdf(Contracts.get(position).getId().toString());
                intent.putExtra("id",Contracts.get(position).getId().toString());
                context.startActivity(intent);

            }
        });

        //Recyclerview onClickListener
        holder.ItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContractsFragment fragment = new ContractsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.viewpager, fragment)
                        .addToBackStack(ContractsFragment.class.getSimpleName())
                        .commit();
                Bundle bundle = new Bundle();
                bundle.putInt("id", Contracts.get(position).getId());
                fragment.setArguments(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Contracts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Fullname,SigningDate,Position,Status,ViewFile;
        TableLayout ItemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            Fullname = itemView.findViewById(R.id.fullname);
            SigningDate = itemView.findViewById(R.id.Signing_date);
            Position = itemView.findViewById(R.id.Position);
            Status = itemView.findViewById(R.id.status);
            ViewFile = itemView.findViewById(R.id.ViewFile);
            ItemLayout = itemView.findViewById(R.id.tableLayout);
        }
    }

    public void CreatePdf(String pdf_id){
        OkHttpClient client = new OkHttpClient.Builder()
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

        Request request = new Request.Builder()
                .url(BASE_URL + "/show/contract-pdf/"+ pdf_id)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.getStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                InputStream pdfData = response.body().byteStream();

                //At this point you can do something with the pdf data
                //Below I add it to internal storage

                if (pdfData != null) {

                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+pdf_id+".pdf");
                    fos.write(response.body().bytes());
                    fos.close();
                    System.out.println("File Created Successfully");
                }
            }
        });
    }
}
