package com.example.parcourssup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InscriptionAdapter extends RecyclerView.Adapter<InscriptionAdapter.ViewHolder> {

    private List<InscriptionEcole> inscriptions;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(String ecoleNom);
    }

    public InscriptionAdapter(List<InscriptionEcole> inscriptions, OnDeleteClickListener listener) {
        this.inscriptions = inscriptions;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InscriptionEcole ecole = inscriptions.get(position);

        holder.tvIcone.setText(ecole.getIcone());
        holder.tvNom.setText(ecole.getNom());
        holder.tvDiplome.setText(ecole.getDiplome());
        holder.tvVille.setText(ecole.getVille());
        holder.tvDate.setText("Inscrit le : " + ecole.getDate());

        holder.btnSupprimer.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(ecole.getNom());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inscriptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcone, tvNom, tvDiplome, tvVille, tvDate;
        Button btnSupprimer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcone = itemView.findViewById(R.id.tvIcone);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvDiplome = itemView.findViewById(R.id.tvDiplome);
            tvVille = itemView.findViewById(R.id.tvVille);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}