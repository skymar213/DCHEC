package com.example.dchec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.postsHolder>  {

    List<Posts> data;
    Context context;
    int selectedItemPosition =0;
    onPostClickListener onPostClickListener;



    public postsAdapter(List<Posts> data, Context context) {
        this.data = data;
        this.context = context;
    }

    interface onPostClickListener {
        void onPostClicked(int position);
    }

    @NonNull
    @Override
    public postsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.posts_view_holder,parent,false);
        return new postsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postsHolder holder, int position) {
        holder.txtUsername.setText(data.get(position).getUserName());
        holder.imgPost.setBackgroundResource(Integer.parseInt(data.get(position).getPostImage()));

        if (selectedItemPosition == position){
            holder.txtUsername.setTextColor(Color.WHITE);
            holder.cardPost.animate().scaleX(1.1f);
            holder.cardPost.animate().scaleY(1.1f);
            holder.ll.setBackgroundResource(R.drawable.post_holder_back);

        }else{
            holder.txtUsername.setTextColor(Color.BLACK);
            holder.cardPost.animate().scaleX(1f);
            holder.cardPost.animate().scaleY(1f);
            holder.ll.setBackgroundResource(R.color.white);

        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class postsHolder extends RecyclerView.ViewHolder{
        ImageView imgPost;
        TextView txtUsername;
        CardView cardPost;
        LinearLayout ll;

        public postsHolder(@NonNull View itemView) {
            super(itemView);

            imgPost = itemView.findViewById(R.id.imgPost);
            txtUsername = itemView.findViewById(R.id.txtUsernameProfile);
            cardPost = itemView.findViewById(R.id.cardPost);
            ll = itemView.findViewById(R.id.llBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPostClickListener.onPostClicked(getAdapterPosition());
                }
            });

            cardPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemPosition = getAdapterPosition();
                    notifyDataSetChanged();


                }
            });
            imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });


        }
    }
}
