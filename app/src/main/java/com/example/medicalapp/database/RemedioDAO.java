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
        remedio.setRemedio("Sei la");
        remedio.setStatus(0);
        remedio.setId(1);

        this.list = Stream.generate(() -> remedio)
                .limit(2)
                .collect(Collectors.toList());
        return list;
    }

    public void create(RemedioModel remedio){
        list.add(remedio);
    }

    public Optional<RemedioModel> getById(Integer id){
        return list.stream()
                .findAny().filter(e -> e.getId() == id);
    }
}
