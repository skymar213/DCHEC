package com.example.dchec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class usersSearchAdapter extends RecyclerView.Adapter<usersSearchAdapter.usersHolder> {

    private ArrayList<User> users;
    private Context context;
    private usersSearchAdapter.onUserClickListener onUserSearchClickListener;



    public usersSearchAdapter(ArrayList<User> users, Context context, usersSearchAdapter.onUserClickListener onUserSearchClickListener) {
        this.users = users;
        this.context = context;
        this.onUserSearchClickListener = onUserSearchClickListener;
    }

    interface onUserClickListener {
        void onUserClicked(int position);
    }


    @NonNull
    @Override
    public usersSearchAdapter.usersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_search_holder, parent, false);
        return new usersSearchAdapter.usersHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull usersSearchAdapter.usersHolder holder, int position) {
        holder.txtUsername.setText(users.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class usersHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        ImageView imgProfile;

        public usersHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserSearchClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsernameSearch);
            imgProfile = itemView.findViewById(R.id.imgProfileSearch);

        }

    }
}
