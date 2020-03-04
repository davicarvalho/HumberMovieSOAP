/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Episode;
import models.Season;

/**
 *
 * @author davicarvalho
 */
public class EpisodeJpaController implements Serializable {

    public EpisodeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Episode episode) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Season seasonid = episode.getSeasonid();
            if (seasonid != null) {
                seasonid = em.getReference(seasonid.getClass(), seasonid.getId());
                episode.setSeasonid(seasonid);
            }
            em.persist(episode);
            if (seasonid != null) {
                seasonid.getEpisodeCollection().add(episode);
                seasonid = em.merge(seasonid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Episode episode) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Episode persistentEpisode = em.find(Episode.class, episode.getId());
            Season seasonidOld = persistentEpisode.getSeasonid();
            Season seasonidNew = episode.getSeasonid();
            if (seasonidNew != null) {
                seasonidNew = em.getReference(seasonidNew.getClass(), seasonidNew.getId());
                episode.setSeasonid(seasonidNew);
            }
            episode = em.merge(episode);
            if (seasonidOld != null && !seasonidOld.equals(seasonidNew)) {
                seasonidOld.getEpisodeCollection().remove(episode);
                seasonidOld = em.merge(seasonidOld);
            }
            if (seasonidNew != null && !seasonidNew.equals(seasonidOld)) {
                seasonidNew.getEpisodeCollection().add(episode);
                seasonidNew = em.merge(seasonidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = episode.getId();
                if (findEpisode(id) == null) {
                    throw new NonexistentEntityException("The episode with id " + id + " no longer exists.");
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
            Episode episode;
            try {
                episode = em.getReference(Episode.class, id);
                episode.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The episode with id " + id + " no longer exists.", enfe);
            }
            Season seasonid = episode.getSeasonid();
            if (seasonid != null) {
                seasonid.getEpisodeCollection().remove(episode);
                seasonid = em.merge(seasonid);
            }
            em.remove(episode);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Episode> findEpisodeEntities() {
        return findEpisodeEntities(true, -1, -1);
    }

    public List<Episode> findEpisodeEntities(int maxResults, int firstResult) {
        return findEpisodeEntities(false, maxResults, firstResult);
    }

    private List<Episode> findEpisodeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Episode.class));
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

    public Episode findEpisode(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Episode.class, id);
        } finally {
            em.close();
        }
    }

    public int getEpisodeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Episode> rt = cq.from(Episode.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
