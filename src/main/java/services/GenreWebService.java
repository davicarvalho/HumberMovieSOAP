/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.GenreJpaController;
import db.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Genre;

/**
 *
 * @author davicarvalho
 */
@WebService(serviceName = "GenreWebService")
public class GenreWebService {
    
    @WebMethod(operationName = "addGenre")
    public Genre addGenre(@WebParam(name = "genre") Genre genre) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        GenreJpaController repo = new GenreJpaController(emf);
        repo.create(genre);
        return genre;
    }
    
    @WebMethod(operationName = "findGenre")
    public Genre findGenre(@WebParam(name = "id") Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        GenreJpaController repo = new GenreJpaController(emf);
        return repo.findGenre(id);
    }
    
    @WebMethod(operationName = "listGenres")
    public List<Genre> listGenres() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        GenreJpaController repo = new GenreJpaController(emf);
        return repo.findGenreEntities();
    }
    
    @WebMethod(operationName = "updateGenre")
    public Genre updateGenre(@WebParam(name = "name") Genre g) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            GenreJpaController repo = new GenreJpaController(emf);
            Genre genre = repo.findGenre(g.getId());
            genre.setName(g.getName());
            repo.edit(genre);
            return genre;
        } catch (Exception ex) {
            Logger.getLogger(GenreWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "deleteGenre")
    public Genre deleteGenre(@WebParam(name = "id") Integer id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            GenreJpaController repo = new GenreJpaController(emf);
            Genre genre = repo.findGenre(id);
            repo.destroy(genre.getId());
            return genre;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(GenreWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
