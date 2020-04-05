/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.EpisodeJpaController;
import db.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Episode;

/**
 *
 * @author davicarvalho
 */
@WebService(serviceName = "EpisodeWebService")
public class EpisodeWebService {

     @WebMethod(operationName = "addEpisode")
    public Episode addEpisode(@WebParam(name = "episode") Episode episode) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        EpisodeJpaController repo = new EpisodeJpaController(emf);
        repo.create(episode);
        return episode;
    }
    
    @WebMethod(operationName = "findEpisode")
    public Episode findEpisode(@WebParam(name = "id") Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        EpisodeJpaController repo = new EpisodeJpaController(emf);
        return repo.findEpisode(id);
    }
    
      @WebMethod(operationName = "findEpisodeBySeason")
    public List<Episode> findEpisodeBySeason(@WebParam(name = "id") Integer seasonId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        EpisodeJpaController repo = new EpisodeJpaController(emf);
        return repo.findEpisodeBySeason(seasonId);
    }
    
    @WebMethod(operationName = "listEpisodes")
    public List<Episode> listEpisodes() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
        EpisodeJpaController repo = new EpisodeJpaController(emf);
        return repo.findEpisodeEntities();
    }
    
    @WebMethod(operationName = "updateEpisode")
    public Episode updateEpisode(@WebParam(name = "name") Episode e) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            EpisodeJpaController repo = new EpisodeJpaController(emf);
            Episode episode = repo.findEpisode(e.getId());
            episode.setTitle(e.getTitle());
            episode.setEpisodeNumber(e.getEpisodeNumber());
            episode.setSeasonid(e.getSeasonid());;
            repo.edit(episode);
            return episode;
        } catch (Exception ex) {
            Logger.getLogger(EpisodeWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "deleteEpisode")
    public Episode deleteEpisode(@WebParam(name = "id") Integer id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("DB");
            EpisodeJpaController repo = new EpisodeJpaController(emf);
            Episode episode = repo.findEpisode(id);
            repo.destroy(episode.getId());
            return episode;
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EpisodeWebService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
   
}
