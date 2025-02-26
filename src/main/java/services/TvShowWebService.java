/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.TvShowJpaController;
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
import models.TvShow;

/**
 *
 * @author davicarvalho
 */
@WebService(serviceName = "TvShowWebService")
@HandlerChain(file = "TvShowWebService_handler.xml")
public class TvShowWebService {

      @WebMethod(operationName = "addTvShow")
    public TvShow addTvShow(TvShow show) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        TvShowJpaController repo = new TvShowJpaController(emf);
        repo.create(show);
        return show;
    }
    
    @WebMethod(operationName = "findTvShow")
    public TvShow findTvShow(@WebParam(name = "id") Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        TvShowJpaController repo = new TvShowJpaController(emf);
        return repo.findTvShow(id);
    }
    
    @WebMethod(operationName = "listTvShows")
    public List<TvShow> listTvShows() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        TvShowJpaController repo = new TvShowJpaController(emf);
        return repo.findTvShowEntities();
    }
    
    @WebMethod(operationName = "updateTvShow")
    public TvShow updateTvShow(@WebParam(name = "show") TvShow show) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            TvShowJpaController repo = new TvShowJpaController(emf);
            TvShow tvShow = repo.findTvShow(show.getId());
            
            tvShow.setGenreid(show.getGenreid());
            tvShow.setTitle(show.getTitle());
            tvShow.setYearEnd(show.getYearEnd());
            tvShow.setYearStart(show.getYearStart());
            repo.edit(tvShow);
            return tvShow;
        } catch (Exception ex) {
            Logger.getLogger(TvShowWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "deleteTvShow")
    public TvShow deleteTvShow(@WebParam(name = "id") Integer id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            TvShowJpaController repo = new TvShowJpaController(emf);
            TvShow tvShow = repo.findTvShow(id);
            repo.destroy(tvShow.getId());
            return tvShow;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TvShowWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
