package com.app.myapplication.attendance_management.Fragments.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.R;
import com.app.myapplication.models.AttendanceHistory;

import java.util.ArrayList;

public class CustomAdapter_History extends RecyclerView.Adapter<CustomAdapter_History.ViewHolder> {
    private Context context;
    private ArrayList<AttendanceHistory> History;

    public CustomAdapter_History(Context context, ArrayList<AttendanceHistory> History) {
        this.context = context;
        this.History = History;
    }
    @NonNull
    @Override
    public CustomAdapter_History.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.history_recycle_view,parent,false);
        return  new CustomAdapter_History.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter_History.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.day.setText(History.get(position).getDay());
        holder.arrival.setText(History.get(position).getArrival());
        holder.departure.setText(History.get(position).getDeparture());
        if((History.get(position).getNoteStart().contentEquals("CHECK"))) {
            holder.note_start.setText("Punctual");
            holder.note_start.setTextColor(Color.parseColor("#4f9a3b"));
        }
        else {
            holder.note_start.setText(History.get(position).getNoteStart());
        }
        if((History.get(position).getNoteStart().contentEquals("CHECK"))) {
            holder.note_end.setText("Punctual");
            holder.note_end.setTextColor(Color.parseColor("#4f9a3b"));
        }
        else {
            holder.note_end.setText(History.get(position).getNoteStart());
        }
    }



    @Override
    public int getItemCount() {
        return History.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day,arrival,departure,note_start,note_end;
        TableLayout ItemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            arrival = itemView.findViewById(R.id.arrival);
            departure = itemView.findViewById(R.id.departure);
            note_start = itemView.findViewById(R.id.note_start);
            note_end = itemView.findViewById(R.id.note_end);
            ItemLayout = itemView.findViewById(R.id.tableLayout_history);
        }
    }
}