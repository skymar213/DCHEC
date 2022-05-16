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

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.usersHolder> {

    private ArrayList<User> users;
    private  Context context;
    private onUserClickListener onUserClickListener;

    public usersAdapter(ArrayList<User> users, Context context, usersAdapter.onUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    interface onUserClickListener {
        void onUserClicked(int position);
    }


    @NonNull
    @Override
    public usersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);
        return new usersHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull usersHolder holder, int position) {
        holder.txtUsername.setText(users.get(position).getNom()+""+users.get(position).getPrenom());
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
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);
            imgProfile = itemView.findViewById(R.id.imgProfile);

        }

    }
}
