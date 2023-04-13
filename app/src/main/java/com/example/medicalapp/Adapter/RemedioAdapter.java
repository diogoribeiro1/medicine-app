package com.example.medicalapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalapp.AddNewRemedio;
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
        holder.alarme.setText(item.getAlarme());
        holder.remedio.setText(item.getRemedio());
        holder.remedio.setChecked(toBoolean(item.getStatus()));
    }
    public Context getContext() {
        return this.mainActivity;
    }
    private boolean toBoolean(int n){
        return n!=0;
    }

    public void deleteItem(int position) {
        RemedioModel item = remedioList.get(position);
        remedioList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        RemedioModel item = remedioList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("remedio", item.getRemedio());
        bundle.putString("dose", item.getDose());
        bundle.putString("frequencia", item.getFrequencia());
        bundle.putString("horario", item.getHorarios());
        bundle.putString("alarme", item.getAlarme());

        AddNewRemedio fragment = new AddNewRemedio();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AddNewRemedio.TAG);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox remedio;
        TextView alarme;

        ViewHolder(View view){
            super(view);
            remedio = view.findViewById(R.id.remedioCheckBox);
            alarme = view.findViewById(R.id.dueTime);

        }
    }
}
