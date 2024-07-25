package com.example.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {
    private List<Password> passwords;

    public PasswordAdapter(List<Password> passwords) {
        this.passwords = passwords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Password password = passwords.get(position);
        holder.accountNameTextView.setText(password.getAccountName());
        holder.usernameTextView.setText(password.getUsername());
        holder.passwordTextView.setText(password.getPassword());
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView accountNameTextView;
        public TextView usernameTextView;
        public TextView passwordTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            accountNameTextView = itemView.findViewById(R.id.accountNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
        }
    }
}