package com.app.myapplication.mainActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.R;
import com.app.myapplication.models.Task;

import java.util.ArrayList;

public class CustomAdapter_Main extends RecyclerView.Adapter<CustomAdapter_Main.MyViewHolder> {

    private Drawable drawable;
    private Context context;
    private ArrayList<Task> tasks;

    public CustomAdapter_Main(Context context,ArrayList<Task> tasks){
        this.context = context;
        this.tasks = tasks;

    }


    @NonNull
    @Override
    public CustomAdapter_Main.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.main_recycle_view_row,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Main.MyViewHolder holder, final int position) {
        holder.task_name.setText(tasks.get(position).getTitle());
        holder.date_start.setText(tasks.get(position).getStartDate());
        holder.date_end.setText(tasks.get(position).getEndDate());
        holder.employee.setText(tasks.get(position).getAssigned());
        holder.status_text.setText(tasks.get(position).getStatus());
        if (tasks.get(position).getStatus().equals("COMPLETED")){
            drawable = AppCompatResources.getDrawable(context,R.drawable.ic_checkmark);
            holder.status_image.setImageDrawable(drawable);
        }else if (tasks.get(position).getStatus().equals("ONGOING")){
            drawable = AppCompatResources.getDrawable(context,R.drawable.ic_loading);
            holder.status_image.setImageDrawable(drawable);
        }else {
            drawable = AppCompatResources.getDrawable(context,R.drawable.ic_pending);
            holder.status_image.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView status_text,task_name,date_start,date_end,employee;
        ImageView status_image;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status_text = itemView.findViewById(R.id.status_text);
            task_name = itemView.findViewById(R.id.Task_name);
            date_start = itemView.findViewById(R.id.date_start);
            date_end = itemView.findViewById(R.id.date_end);
            employee = itemView.findViewById(R.id.employee_name);
            status_image = itemView.findViewById(R.id.status_image);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
