/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.MovieJpaController;
import db.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Movie;

/**
 *
 * @author davicarvalho
 */
@WebService(serviceName = "MovieWebService")
@HandlerChain(file = "MovieWebService_handler.xml")
public class MovieWebService {

     @WebMethod(operationName = "addMovie")
    public Movie addMovie(@WebParam(name = "movie") Movie movie) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        MovieJpaController repo = new MovieJpaController(emf);
        repo.create(movie);
        return movie;
    }
    
    @WebMethod(operationName = "findMovie")
    public Movie findMovie(@WebParam(name = "id") Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        MovieJpaController repo = new MovieJpaController(emf);
        return repo.findMovie(id);
    }
    
    @WebMethod(operationName = "listMovies")
    public List<Movie> listMovies() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        MovieJpaController repo = new MovieJpaController(emf);
        return repo.findMovieEntities();
    }
    
    @WebMethod(operationName = "updateMovie")
    public Movie updateMovie(@WebParam(name = "movie") Movie m) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            MovieJpaController repo = new MovieJpaController(emf);
            Movie movie = repo.findMovie(m.getId());
            movie.setTitle(m.getTitle());
            movie.setYear(m.getYear());
            repo.edit(movie);
            return movie;
        } catch (Exception ex) {
            Logger.getLogger(MovieWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "deleteMovie")
    public Movie deleteMovie(@WebParam(name = "id") Integer id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            MovieJpaController repo = new MovieJpaController(emf);
            Movie movie = repo.findMovie(id);
            repo.destroy(movie.getId());
            return movie;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovieWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
