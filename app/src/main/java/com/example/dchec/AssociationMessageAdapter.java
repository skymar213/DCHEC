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

public class AssociationMessageAdapter extends RecyclerView.Adapter<AssociationMessageAdapter.associationHolder> {

    private ArrayList<Association> associations;
    private Context context;
    private onAssociationClickListener onAssociationClickListener;

    public AssociationMessageAdapter(ArrayList<Association> associations, Context context, AssociationMessageAdapter.onAssociationClickListener onAssociationClickListener) {
        this.associations =associations ;
        this.context = context;
        this.onAssociationClickListener = onAssociationClickListener;
    }
    interface onAssociationClickListener {
        void onAssociationClicked(int position);
    }

    @NonNull
    @Override
    public AssociationMessageAdapter.associationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_message_holder, parent, false);
        return new associationHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AssociationMessageAdapter.associationHolder holder, int position) {
        holder.txtUsername.setText(associations.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return associations.size();
    }


    class associationHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        ImageView imgProfile;

        public associationHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAssociationClickListener.onAssociationClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);
            imgProfile = itemView.findViewById(R.id.imgProfile);

        }

    }

}