package com.app.myapplication.Record_management.Fragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.R;
import com.app.myapplication.attendance_management.Fragments.OnClickListener;
import com.app.myapplication.models.Employee;

import java.util.ArrayList;

public class CustomAdapter_RecordEmployees  extends RecyclerView.Adapter<CustomAdapter_RecordEmployees.ViewHolder> {
    private OnClickListener Listener;
    private final Context context;
    private final ArrayList<Employee> employees;

    public CustomAdapter_RecordEmployees(OnClickListener Listener,Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public CustomAdapter_RecordEmployees.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.attendance_employee_recycle_view,parent,false);
        return  new CustomAdapter_RecordEmployees.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fullname =employees.get(position).getName()+" "+employees.get(position).getLastName();
        holder.Employee_FullName.setText(fullname);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCardClick(employees.get(position).getId());
            }
        });
    }



    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Employee_FullName;
        LinearLayout itemLayout;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            Employee_FullName = itemView.findViewById(R.id.AttendanceEmployee_fullname);
            itemLayout = itemView.findViewById(R.id.mainLayout_AttendanceEmployee);
            cardView= itemView.findViewById(R.id.CardView_AttendanceEmployee);


        }
    }
}