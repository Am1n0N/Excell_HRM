package com.app.myapplication.staff_management.Fragments.Adapeters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.DownloadImageProfile;
import com.app.myapplication.R;
import com.app.myapplication.models.Employee;
import com.app.myapplication.staff_management.Fragments.OnCardClickListener;

import java.util.ArrayList;

public class CustomAdapter_employees extends RecyclerView.Adapter<CustomAdapter_employees.ViewHolder> {
    private OnCardClickListener Listener;
    private final Context context;
    private final ArrayList<Employee> employees;
    public CustomAdapter_employees(OnCardClickListener Listener,Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public CustomAdapter_employees.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.employee_recycle_view_row,parent,false);
        return  new CustomAdapter_employees.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String fullname =employees.get(position).getName()+" "+employees.get(position).getLastName();
        holder.Employee_FullName.setText(fullname);
        holder.Employee_Email.setText(employees.get(position).getEmail());
        holder.Employee_Address.setText(employees.get(position).getAddress());
        holder.Employee_NIN.setText(employees.get(position).getNin());
        holder.Employee_Position.setText(employees.get(position).getPosition());
        //Getting Profile pic
        String ImageUrl_String="http://10.0.2.2:5000/static/pics/"+employees.get(position).getId()+".jpg";
        new DownloadImageProfile(holder.Employee_image).execute(ImageUrl_String);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCardClick(employees.get(position).getId(),
                        employees.get(position).getName(),
                        employees.get(position).getLastName(),
                        employees.get(position).getEmail(),
                        employees.get(position).getAddress(),
                        employees.get(position).getPosition(),
                        employees.get(position).getPhoneNumber());
            }
        });

    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Employee_FullName,Employee_NIN,Employee_Email,Employee_Position,Employee_Address;
        ImageView Employee_image;
        LinearLayout itemLayout;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            Employee_FullName = itemView.findViewById(R.id.Employee_fullname);
            Employee_NIN = itemView.findViewById(R.id.Employee_NIN);
            Employee_Email = itemView.findViewById(R.id.Employee_email);
            Employee_Position = itemView.findViewById(R.id.Employee_position);
            Employee_Address = itemView.findViewById(R.id.Employee_address);
            Employee_image = itemView.findViewById(R.id.Employee_image);
            itemLayout = itemView.findViewById(R.id.mainLayout_employee);
            cardView= itemView.findViewById(R.id.CardView_employee);


        }
    }
}
