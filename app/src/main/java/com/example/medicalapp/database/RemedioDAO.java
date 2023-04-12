package com.example.medicalapp.database;

import com.example.medicalapp.model.RemedioModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemedioDAO {

    private static List<RemedioModel> list = new ArrayList<>();

    public List read(){
        RemedioModel remedio = new RemedioModel();
        remedio.setRemedio("name");
        remedio.setStatus(0);
        remedio.setId(1);

        this.list = Stream.generate(() -> remedio)
                .limit(0)
                .collect(Collectors.toList());
        return list;
    }

    public void create(RemedioModel remedio){
        list.add(remedio);
    }

    public void update(RemedioModel model){
        list.stream().findAny().filter(e -> e.getId() == model.getId()).map(remedioToUpdate -> {

            int indice = list.indexOf(remedioToUpdate);
            remedioToUpdate.setRemedio(model.getRemedio());
            remedioToUpdate.setDose(model.getDose());
            remedioToUpdate.setFrequencia(model.getFrequencia());
            remedioToUpdate.setHorarios(model.getHorarios());
            remedioToUpdate.setAlarme(model.getAlarme());

            list.set(indice, remedioToUpdate);
            return null;
        }).orElse(model);
    }

    public Optional<RemedioModel> getById(Integer id){
        return list.stream()
                .findAny().filter(e -> e.getId() == id);
    }
}
