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
import models.Genre;
import models.Season;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.TvShow;

/**
 *
 * @author davicarvalho
 */
public class TvShowJpaController implements Serializable {

    public TvShowJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TvShow tvShow) {
        if (tvShow.getSeasonCollection() == null) {
            tvShow.setSeasonCollection(new ArrayList<Season>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genre genreid = tvShow.getGenreid();
            if (genreid != null) {
                genreid = em.getReference(genreid.getClass(), genreid.getId());
                tvShow.setGenreid(genreid);
            }
            Collection<Season> attachedSeasonCollection = new ArrayList<Season>();
            for (Season seasonCollectionSeasonToAttach : tvShow.getSeasonCollection()) {
                seasonCollectionSeasonToAttach = em.getReference(seasonCollectionSeasonToAttach.getClass(), seasonCollectionSeasonToAttach.getId());
                attachedSeasonCollection.add(seasonCollectionSeasonToAttach);
            }
            tvShow.setSeasonCollection(attachedSeasonCollection);
            em.persist(tvShow);
            if (genreid != null) {
                genreid.getTvShowCollection().add(tvShow);
                genreid = em.merge(genreid);
            }
            for (Season seasonCollectionSeason : tvShow.getSeasonCollection()) {
                TvShow oldShowidOfSeasonCollectionSeason = seasonCollectionSeason.getShowid();
                seasonCollectionSeason.setShowid(tvShow);
                seasonCollectionSeason = em.merge(seasonCollectionSeason);
                if (oldShowidOfSeasonCollectionSeason != null) {
                    oldShowidOfSeasonCollectionSeason.getSeasonCollection().remove(seasonCollectionSeason);
                    oldShowidOfSeasonCollectionSeason = em.merge(oldShowidOfSeasonCollectionSeason);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TvShow tvShow) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TvShow persistentTvShow = em.find(TvShow.class, tvShow.getId());
            Genre genreidOld = persistentTvShow.getGenreid();
            Genre genreidNew = tvShow.getGenreid();
            Collection<Season> seasonCollectionOld = persistentTvShow.getSeasonCollection();
            Collection<Season> seasonCollectionNew = tvShow.getSeasonCollection();
            if (genreidNew != null) {
                genreidNew = em.getReference(genreidNew.getClass(), genreidNew.getId());
                tvShow.setGenreid(genreidNew);
            }
            Collection<Season> attachedSeasonCollectionNew = new ArrayList<Season>();
            for (Season seasonCollectionNewSeasonToAttach : seasonCollectionNew) {
                seasonCollectionNewSeasonToAttach = em.getReference(seasonCollectionNewSeasonToAttach.getClass(), seasonCollectionNewSeasonToAttach.getId());
                attachedSeasonCollectionNew.add(seasonCollectionNewSeasonToAttach);
            }
            seasonCollectionNew = attachedSeasonCollectionNew;
            tvShow.setSeasonCollection(seasonCollectionNew);
            tvShow = em.merge(tvShow);
            if (genreidOld != null && !genreidOld.equals(genreidNew)) {
                genreidOld.getTvShowCollection().remove(tvShow);
                genreidOld = em.merge(genreidOld);
            }
            if (genreidNew != null && !genreidNew.equals(genreidOld)) {
                genreidNew.getTvShowCollection().add(tvShow);
                genreidNew = em.merge(genreidNew);
            }
            for (Season seasonCollectionOldSeason : seasonCollectionOld) {
                if (!seasonCollectionNew.contains(seasonCollectionOldSeason)) {
                    seasonCollectionOldSeason.setShowid(null);
                    seasonCollectionOldSeason = em.merge(seasonCollectionOldSeason);
                }
            }
            for (Season seasonCollectionNewSeason : seasonCollectionNew) {
                if (!seasonCollectionOld.contains(seasonCollectionNewSeason)) {
                    TvShow oldShowidOfSeasonCollectionNewSeason = seasonCollectionNewSeason.getShowid();
                    seasonCollectionNewSeason.setShowid(tvShow);
                    seasonCollectionNewSeason = em.merge(seasonCollectionNewSeason);
                    if (oldShowidOfSeasonCollectionNewSeason != null && !oldShowidOfSeasonCollectionNewSeason.equals(tvShow)) {
                        oldShowidOfSeasonCollectionNewSeason.getSeasonCollection().remove(seasonCollectionNewSeason);
                        oldShowidOfSeasonCollectionNewSeason = em.merge(oldShowidOfSeasonCollectionNewSeason);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tvShow.getId();
                if (findTvShow(id) == null) {
                    throw new NonexistentEntityException("The tvShow with id " + id + " no longer exists.");
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
            TvShow tvShow;
            try {
                tvShow = em.getReference(TvShow.class, id);
                tvShow.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tvShow with id " + id + " no longer exists.", enfe);
            }
            Genre genreid = tvShow.getGenreid();
            if (genreid != null) {
                genreid.getTvShowCollection().remove(tvShow);
                genreid = em.merge(genreid);
            }
            Collection<Season> seasonCollection = tvShow.getSeasonCollection();
            for (Season seasonCollectionSeason : seasonCollection) {
                seasonCollectionSeason.setShowid(null);
                seasonCollectionSeason = em.merge(seasonCollectionSeason);
            }
            em.remove(tvShow);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TvShow> findTvShowEntities() {
        return findTvShowEntities(true, -1, -1);
    }

    public List<TvShow> findTvShowEntities(int maxResults, int firstResult) {
        return findTvShowEntities(false, maxResults, firstResult);
    }

    private List<TvShow> findTvShowEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TvShow.class));
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

    public TvShow findTvShow(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TvShow.class, id);
        } finally {
            em.close();
        }
    }

    public int getTvShowCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TvShow> rt = cq.from(TvShow.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
