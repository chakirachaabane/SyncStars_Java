package org.example.services;

import java.sql.SQLException;
import java.util.List;

public interface IEventService<T> {

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(T t) throws SQLException;
    public void supprimerParId(int i) throws SQLException;
    public List<T> afficher() throws SQLException;
}