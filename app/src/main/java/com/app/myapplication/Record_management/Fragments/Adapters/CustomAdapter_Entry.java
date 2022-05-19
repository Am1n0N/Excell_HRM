package com.app.myapplication.Record_management.Fragments.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.R;
import com.app.myapplication.Record_management.Entry_view;
import com.app.myapplication.models.EntryHistory;

import java.util.ArrayList;

public class CustomAdapter_Entry extends RecyclerView.Adapter<CustomAdapter_Entry.ViewHolder> {
    private Context context;
    private ArrayList<EntryHistory> History;

    public CustomAdapter_Entry(Context context, ArrayList<EntryHistory> History) {
        this.context = context;
        this.History = History;
    }
    @NonNull
    @Override
    public CustomAdapter_Entry.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.entry_recycle_view,parent,false);
        return  new CustomAdapter_Entry.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.day.setText(History.get(position).getDate());
        holder.title.setText(History.get(position).getTitle());
        holder.logger.setText(History.get(position).getLogger());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Entry_view.class);
                intent.putExtra("id",History.get(position).getId().toString());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return History.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day,title,logger,view;
        TableLayout ItemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.Date_entry);
            title = itemView.findViewById(R.id.Title_Entry);
            logger = itemView.findViewById(R.id.Logger_Entry);
            view = itemView.findViewById(R.id.ViewEntry);

        }
    }
}