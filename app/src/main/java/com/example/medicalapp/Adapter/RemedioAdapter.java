package com.example.medicalapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.MainActivity;
import com.example.medicalapp.R;
import com.example.medicalapp.model.RemedioModel;

import java.util.List;

public class RemedioAdapter extends RecyclerView.Adapter<RemedioAdapter.ViewHolder> {
    private List<RemedioModel> remedioList;
    private MainActivity mainActivity;

    public RemedioAdapter(MainActivity activity){
        this.mainActivity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.remedios_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return remedioList.size();
    }

    public void setRemedioList(List<RemedioModel> remedioList) {
        this.remedioList = remedioList;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        RemedioModel item = remedioList.get(position);
        holder.remedio.setText(item.getRemedio());
        holder.remedio.setChecked(toBoolean(item.getStatus()));
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox remedio;

        ViewHolder(View view){
            super(view);
            remedio = view.findViewById(R.id.remedioCheckBox);
        }
    }
}
