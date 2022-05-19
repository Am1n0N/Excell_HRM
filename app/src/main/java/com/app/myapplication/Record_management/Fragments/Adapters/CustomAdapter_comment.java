package com.app.myapplication.Record_management.Fragments.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myapplication.DownloadImageProfile;
import com.app.myapplication.R;
import com.app.myapplication.models.Comment;

import java.util.ArrayList;

public class CustomAdapter_comment extends RecyclerView.Adapter<CustomAdapter_comment.ViewHolder> {
    private Context context;
    private ArrayList<Comment> Comments;


    public CustomAdapter_comment(Context context, ArrayList<Comment> Comments) {
        this.context = context;
        this.Comments = Comments;
    }

    @NonNull
    @Override
    public CustomAdapter_comment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.comments_recycle_view_row,parent,false);
        return  new CustomAdapter_comment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.comment_age.setText(Comments.get(position).getAge());
        holder.comment_content.setText(Comments.get(position).getComment());
        holder.comment_fullname.setText(Comments.get(position).getEmployeeFullname());
        String imageUrl_String = "http://10.0.2.2:5000/static/pics/" + Comments.get(position).getEmployeeId()+ ".jpg";
        new DownloadImageProfile(holder.comment_image).execute(imageUrl_String);
    }



    @Override
    public int getItemCount() {
        return Comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment_content,comment_age,comment_fullname;
        ImageView comment_image;

        public ViewHolder(View itemView) {
            super(itemView);
            comment_content = itemView.findViewById(R.id.Comment_content);
            comment_age = itemView.findViewById(R.id.Comment_age);
            comment_fullname = itemView.findViewById(R.id.Comment_Fullname);
            comment_image = itemView.findViewById(R.id.Comment_image);

        }
    }
}
