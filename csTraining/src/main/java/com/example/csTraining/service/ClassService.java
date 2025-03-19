package com.example.csTraining.service;

import com.example.csTraining.entity.Class;
import com.example.csTraining.entity.Oposion;

import java.util.List;

public interface ClassService {
    List<Class> getAllClasses();
    List<Class> getClassesByOposicion(Oposion oposicion);
    Class getClassById(Long id);
    Class createClass(Class classEntity);
    Class updateClass(Long id, Class classEntity);
    void deleteClass(Long id);
}
