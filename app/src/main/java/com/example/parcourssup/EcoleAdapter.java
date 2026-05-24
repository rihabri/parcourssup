package com.example.parcourssup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EcoleAdapter extends RecyclerView.Adapter<EcoleAdapter.ViewHolder> {

    private List<Ecole> ecoles;
    private Context context;
    private OnInscriptionClickListener inscriptionListener;


    public interface OnInscriptionClickListener {
        void onInscriptionClick(Ecole ecole);
    }

    public EcoleAdapter(List<Ecole> ecoles, Context context, OnInscriptionClickListener listener) {
        this.ecoles = ecoles;
        this.context = context;
        this.inscriptionListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ecole, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ecole ecole = ecoles.get(position);

        holder.tvNomEcole.setText(ecole.getNom());
        holder.tvNomArab.setText(ecole.getNomArab());
        holder.tvType.setText("● " + ecole.getType());
        holder.tvVille.setText(ecole.getVille());
        holder.tvPrix.setText(ecole.getPrix());
        holder.tvDuree.setText(ecole.getDuree());
        holder.tvDiplome.setText(ecole.getDiplome());
        holder.tvFiliere.setText(ecole.getFiliere());


        String dateLimite = ecole.getDateLimite();
        if (dateLimite != null && !dateLimite.isEmpty()) {
            holder.tvDateLimite.setText(dateLimite);
        } else {
            holder.tvDateLimite.setText("—");
        }

        String diplomeText = getDiplomeCount(ecole.getDiplome());
        holder.tvNbDiplomes.setText(diplomeText);

        holder.btnInscrire.setOnClickListener(v -> {
            if (inscriptionListener != null) {
                inscriptionListener.onInscriptionClick(ecole);
            }
        });
    }

    private String getDiplomeCount(String diplome) {
        if (diplome.contains("Doctorat")) return "2 diplômes";
        if (diplome.contains("Master") || diplome.contains("Ingénieur")) return "1 diplôme";
        return "1 diplôme";
    }

    @Override
    public int getItemCount() {
        return ecoles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomEcole, tvNomArab, tvType, tvVille, tvPrix, tvDuree;
        TextView tvDiplome, tvFiliere, tvNbDiplomes;
        TextView tvDateLimite;
        Button btnInscrire;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomEcole   = itemView.findViewById(R.id.tvNomEcole);
            tvNomArab    = itemView.findViewById(R.id.tvNomArab);
            tvType       = itemView.findViewById(R.id.tvType);
            tvVille      = itemView.findViewById(R.id.tvVille);
            tvPrix       = itemView.findViewById(R.id.tvPrix);
            tvDuree      = itemView.findViewById(R.id.tvDuree);
            tvDiplome    = itemView.findViewById(R.id.tvDiplome);
            tvFiliere    = itemView.findViewById(R.id.tvFiliere);
            tvNbDiplomes = itemView.findViewById(R.id.tvNbDiplomes);
            tvDateLimite = itemView.findViewById(R.id.tvDateLimite);
            btnInscrire  = itemView.findViewById(R.id.btnInscrire);
        }
    }
}