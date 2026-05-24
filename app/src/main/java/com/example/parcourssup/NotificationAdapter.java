package com.example.parcourssup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationItem> notificationList;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem notification);
    }

    public NotificationAdapter(List<NotificationItem> notificationList, OnNotificationClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notif = notificationList.get(position);


        String icon;
        switch (notif.getType()) {
            case "concours":
                icon = "🏆";
                break;
            case "inscription":
                icon = "📝";
                break;
            case "actualite":
                icon = "📰";
                break;
            case "rappel":
                icon = "⏰";
                break;
            case "ecole":
                break;
            default:
                icon = "🔔";
        }
        holder.tvNotifTitle.setText(notif.getTitre());
        holder.tvNotifMessage.setText(notif.getMessage());
        holder.tvNotifTime.setText(notif.getTemps());

        if (notif.isLu()) {
            holder.viewNotifDot.setVisibility(View.GONE);
        } else {
            holder.viewNotifDot.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notif);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateList(List<NotificationItem> newList) {
        this.notificationList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotifIcon, tvNotifTitle, tvNotifMessage, tvNotifTime;
        View viewNotifDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotifIcon = itemView.findViewById(R.id.tvNotifIcon);
            tvNotifTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvNotifMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvNotifTime = itemView.findViewById(R.id.tvNotifTime);
            viewNotifDot = itemView.findViewById(R.id.viewNotifDot);
        }
    }
}