package com.example.parcourssup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SimpleAdapter<T> extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {


    public interface OnDeleteClickListener<T> {
        void onDelete(T item, int position);
    }

    public interface OnItemClickListener<T> {
        void onClick(T item, int position);
    }


    private final List<T>                  items;
    private final Context                  context;
    private       OnDeleteClickListener<T> deleteListener;
    private       OnItemClickListener<T>   clickListener;


    public SimpleAdapter(Context context, List<T> items) {
        this.context = context;
        this.items   = items;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener<T> listener) {
        this.deleteListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_simple, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = items.get(position);

        if (item instanceof User) {
            User u = (User) item;
            holder.tvTitle.setText(u.getFullName());
            holder.tvSubtitle.setText(u.getSummary());

        } else if (item instanceof Order) {
            Order o = (Order) item;
            holder.tvTitle.setText(o.getPack() + " — " + o.getPrix());
            holder.tvSubtitle.setText(o.getSummary());

        } else if (item instanceof Ecole) {
            Ecole e = (Ecole) item;
            holder.tvTitle.setText(e.getNom());
            holder.tvSubtitle.setText(e.getSummary());
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item, holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(item, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }


    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView    tvTitle, tvSubtitle;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tvItemTitle);
            tvSubtitle = itemView.findViewById(R.id.tvItemSubtitle);
            btnDelete  = itemView.findViewById(R.id.btnItemDelete);
        }
    }
}