/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.TvShow;
import models.Episode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Season;

/**
 *
 * @author davicarvalho
 */
public class SeasonJpaController implements Serializable {

    public SeasonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Season season) {
        if (season.getEpisodeCollection() == null) {
            season.setEpisodeCollection(new ArrayList<Episode>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TvShow showid = season.getShowid();
            if (showid != null) {
                showid = em.getReference(showid.getClass(), showid.getId());
                season.setShowid(showid);
            }
            Collection<Episode> attachedEpisodeCollection = new ArrayList<Episode>();
            for (Episode episodeCollectionEpisodeToAttach : season.getEpisodeCollection()) {
                episodeCollectionEpisodeToAttach = em.getReference(episodeCollectionEpisodeToAttach.getClass(), episodeCollectionEpisodeToAttach.getId());
                attachedEpisodeCollection.add(episodeCollectionEpisodeToAttach);
            }
            season.setEpisodeCollection(attachedEpisodeCollection);
            em.persist(season);
            if (showid != null) {
                showid.getSeasonCollection().add(season);
                showid = em.merge(showid);
            }
            for (Episode episodeCollectionEpisode : season.getEpisodeCollection()) {
                Season oldSeasonidOfEpisodeCollectionEpisode = episodeCollectionEpisode.getSeasonid();
                episodeCollectionEpisode.setSeasonid(season);
                episodeCollectionEpisode = em.merge(episodeCollectionEpisode);
                if (oldSeasonidOfEpisodeCollectionEpisode != null) {
                    oldSeasonidOfEpisodeCollectionEpisode.getEpisodeCollection().remove(episodeCollectionEpisode);
                    oldSeasonidOfEpisodeCollectionEpisode = em.merge(oldSeasonidOfEpisodeCollectionEpisode);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Season season) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Season persistentSeason = em.find(Season.class, season.getId());
            TvShow showidOld = persistentSeason.getShowid();
            TvShow showidNew = season.getShowid();
            Collection<Episode> episodeCollectionOld = persistentSeason.getEpisodeCollection();
            Collection<Episode> episodeCollectionNew = season.getEpisodeCollection();
            if (showidNew != null) {
                showidNew = em.getReference(showidNew.getClass(), showidNew.getId());
                season.setShowid(showidNew);
            }
            Collection<Episode> attachedEpisodeCollectionNew = new ArrayList<Episode>();
            for (Episode episodeCollectionNewEpisodeToAttach : episodeCollectionNew) {
                episodeCollectionNewEpisodeToAttach = em.getReference(episodeCollectionNewEpisodeToAttach.getClass(), episodeCollectionNewEpisodeToAttach.getId());
                attachedEpisodeCollectionNew.add(episodeCollectionNewEpisodeToAttach);
            }
            episodeCollectionNew = attachedEpisodeCollectionNew;
            season.setEpisodeCollection(episodeCollectionNew);
            season = em.merge(season);
            if (showidOld != null && !showidOld.equals(showidNew)) {
                showidOld.getSeasonCollection().remove(season);
                showidOld = em.merge(showidOld);
            }
            if (showidNew != null && !showidNew.equals(showidOld)) {
                showidNew.getSeasonCollection().add(season);
                showidNew = em.merge(showidNew);
            }
            for (Episode episodeCollectionOldEpisode : episodeCollectionOld) {
                if (!episodeCollectionNew.contains(episodeCollectionOldEpisode)) {
                    episodeCollectionOldEpisode.setSeasonid(null);
                    episodeCollectionOldEpisode = em.merge(episodeCollectionOldEpisode);
                }
            }
            for (Episode episodeCollectionNewEpisode : episodeCollectionNew) {
                if (!episodeCollectionOld.contains(episodeCollectionNewEpisode)) {
                    Season oldSeasonidOfEpisodeCollectionNewEpisode = episodeCollectionNewEpisode.getSeasonid();
                    episodeCollectionNewEpisode.setSeasonid(season);
                    episodeCollectionNewEpisode = em.merge(episodeCollectionNewEpisode);
                    if (oldSeasonidOfEpisodeCollectionNewEpisode != null && !oldSeasonidOfEpisodeCollectionNewEpisode.equals(season)) {
                        oldSeasonidOfEpisodeCollectionNewEpisode.getEpisodeCollection().remove(episodeCollectionNewEpisode);
                        oldSeasonidOfEpisodeCollectionNewEpisode = em.merge(oldSeasonidOfEpisodeCollectionNewEpisode);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = season.getId();
                if (findSeason(id) == null) {
                    throw new NonexistentEntityException("The season with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Season season;
            try {
                season = em.getReference(Season.class, id);
                season.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The season with id " + id + " no longer exists.", enfe);
            }
            TvShow showid = season.getShowid();
            if (showid != null) {
                showid.getSeasonCollection().remove(season);
                showid = em.merge(showid);
            }
            Collection<Episode> episodeCollection = season.getEpisodeCollection();
            for (Episode episodeCollectionEpisode : episodeCollection) {
                episodeCollectionEpisode.setSeasonid(null);
                episodeCollectionEpisode = em.merge(episodeCollectionEpisode);
            }
            em.remove(season);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Season> findSeasonEntities() {
        return findSeasonEntities(true, -1, -1);
    }

    public List<Season> findSeasonEntities(int maxResults, int firstResult) {
        return findSeasonEntities(false, maxResults, firstResult);
    }

    private List<Season> findSeasonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Season.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Season findSeason(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Season.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeasonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Season> rt = cq.from(Season.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
