/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.SeasonJpaController;
import db.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Season;

/**
 *
 * @author davicarvalho
 */
@WebService(serviceName = "SeasonWebService")
public class SeasonWebService {

    @WebMethod(operationName = "addSeason")
    public Season addSeason(@WebParam(name = "season") Season season) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        SeasonJpaController repo = new SeasonJpaController(emf);
        repo.create(season);
        return season;
    }
    
    @WebMethod(operationName = "findSeason")
    public Season findSeason(@WebParam(name = "id") Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        SeasonJpaController repo = new SeasonJpaController(emf);
        return repo.findSeason(id);
    }
    
    @WebMethod(operationName = "listSeasons")
    public List<Season> listSeasons() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        SeasonJpaController repo = new SeasonJpaController(emf);
        return repo.findSeasonEntities();
    }
    
    @WebMethod(operationName = "updateSeason")
    public Season updateSeason(@WebParam(name = "name") Season s) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            SeasonJpaController repo = new SeasonJpaController(emf);
            Season season = repo.findSeason(s.getId());
            season.setShowid(s.getShowid());
            season.setTitle(s.getTitle());
            season.setYear(s.getYear());
            repo.edit(season);
            return season;
        } catch (Exception ex) {
            Logger.getLogger(SeasonWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "deleteSeason")
    public Season deleteSeason(@WebParam(name = "id") Integer id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            SeasonJpaController repo = new SeasonJpaController(emf);
            Season season = repo.findSeason(id);
            repo.destroy(season.getId());
            return season;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SeasonWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
