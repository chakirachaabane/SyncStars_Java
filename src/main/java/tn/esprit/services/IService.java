package tn.esprit.services;


import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    void add(T t) throws SQLException;


    void addExercice(T t,int id) throws SQLException;
    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;

    List<T>  getByIdEntrainemnt(int id) throws SQLException;

    List<T>  getByTypeEntrainemnt(String id) throws SQLException;







}
