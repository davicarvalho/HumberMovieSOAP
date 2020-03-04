/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import db.MovieJpaController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jdk.nashorn.internal.objects.NativeArray;
import models.Movie;

/**
 *
 * @author davicarvalho
 */
public class NewClass {
    
    public static void main(String[] args) {
         try {
             EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
             MovieJpaController repo = new MovieJpaController(emf);
             
             List<Movie> movies = repo.findMovieEntities();
             for(Movie m : movies){
             }
        }catch(Exception e){
            e.printStackTrace();
           
        }
    }
    
}
