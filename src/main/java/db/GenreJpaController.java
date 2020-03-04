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
import models.Movie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import models.Genre;
import models.TvShow;

/**
 *
 * @author davicarvalho
 */
public class GenreJpaController implements Serializable {

    public GenreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genre genre) {
        if (genre.getMovieCollection() == null) {
            genre.setMovieCollection(new ArrayList<Movie>());
        }
        if (genre.getTvShowCollection() == null) {
            genre.setTvShowCollection(new ArrayList<TvShow>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Movie> attachedMovieCollection = new ArrayList<Movie>();
            for (Movie movieCollectionMovieToAttach : genre.getMovieCollection()) {
                movieCollectionMovieToAttach = em.getReference(movieCollectionMovieToAttach.getClass(), movieCollectionMovieToAttach.getId());
                attachedMovieCollection.add(movieCollectionMovieToAttach);
            }
            genre.setMovieCollection(attachedMovieCollection);
            Collection<TvShow> attachedTvShowCollection = new ArrayList<TvShow>();
            for (TvShow tvShowCollectionTvShowToAttach : genre.getTvShowCollection()) {
                tvShowCollectionTvShowToAttach = em.getReference(tvShowCollectionTvShowToAttach.getClass(), tvShowCollectionTvShowToAttach.getId());
                attachedTvShowCollection.add(tvShowCollectionTvShowToAttach);
            }
            genre.setTvShowCollection(attachedTvShowCollection);
            em.persist(genre);
            for (Movie movieCollectionMovie : genre.getMovieCollection()) {
                Genre oldGenreidOfMovieCollectionMovie = movieCollectionMovie.getGenreid();
                movieCollectionMovie.setGenreid(genre);
                movieCollectionMovie = em.merge(movieCollectionMovie);
                if (oldGenreidOfMovieCollectionMovie != null) {
                    oldGenreidOfMovieCollectionMovie.getMovieCollection().remove(movieCollectionMovie);
                    oldGenreidOfMovieCollectionMovie = em.merge(oldGenreidOfMovieCollectionMovie);
                }
            }
            for (TvShow tvShowCollectionTvShow : genre.getTvShowCollection()) {
                Genre oldGenreidOfTvShowCollectionTvShow = tvShowCollectionTvShow.getGenreid();
                tvShowCollectionTvShow.setGenreid(genre);
                tvShowCollectionTvShow = em.merge(tvShowCollectionTvShow);
                if (oldGenreidOfTvShowCollectionTvShow != null) {
                    oldGenreidOfTvShowCollectionTvShow.getTvShowCollection().remove(tvShowCollectionTvShow);
                    oldGenreidOfTvShowCollectionTvShow = em.merge(oldGenreidOfTvShowCollectionTvShow);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genre genre) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genre persistentGenre = em.find(Genre.class, genre.getId());
            Collection<Movie> movieCollectionOld = persistentGenre.getMovieCollection();
            Collection<Movie> movieCollectionNew = genre.getMovieCollection();
            Collection<TvShow> tvShowCollectionOld = persistentGenre.getTvShowCollection();
            Collection<TvShow> tvShowCollectionNew = genre.getTvShowCollection();
            Collection<Movie> attachedMovieCollectionNew = new ArrayList<Movie>();
            for (Movie movieCollectionNewMovieToAttach : movieCollectionNew) {
                movieCollectionNewMovieToAttach = em.getReference(movieCollectionNewMovieToAttach.getClass(), movieCollectionNewMovieToAttach.getId());
                attachedMovieCollectionNew.add(movieCollectionNewMovieToAttach);
            }
            movieCollectionNew = attachedMovieCollectionNew;
            genre.setMovieCollection(movieCollectionNew);
            Collection<TvShow> attachedTvShowCollectionNew = new ArrayList<TvShow>();
            for (TvShow tvShowCollectionNewTvShowToAttach : tvShowCollectionNew) {
                tvShowCollectionNewTvShowToAttach = em.getReference(tvShowCollectionNewTvShowToAttach.getClass(), tvShowCollectionNewTvShowToAttach.getId());
                attachedTvShowCollectionNew.add(tvShowCollectionNewTvShowToAttach);
            }
            tvShowCollectionNew = attachedTvShowCollectionNew;
            genre.setTvShowCollection(tvShowCollectionNew);
            genre = em.merge(genre);
            for (Movie movieCollectionOldMovie : movieCollectionOld) {
                if (!movieCollectionNew.contains(movieCollectionOldMovie)) {
                    movieCollectionOldMovie.setGenreid(null);
                    movieCollectionOldMovie = em.merge(movieCollectionOldMovie);
                }
            }
            for (Movie movieCollectionNewMovie : movieCollectionNew) {
                if (!movieCollectionOld.contains(movieCollectionNewMovie)) {
                    Genre oldGenreidOfMovieCollectionNewMovie = movieCollectionNewMovie.getGenreid();
                    movieCollectionNewMovie.setGenreid(genre);
                    movieCollectionNewMovie = em.merge(movieCollectionNewMovie);
                    if (oldGenreidOfMovieCollectionNewMovie != null && !oldGenreidOfMovieCollectionNewMovie.equals(genre)) {
                        oldGenreidOfMovieCollectionNewMovie.getMovieCollection().remove(movieCollectionNewMovie);
                        oldGenreidOfMovieCollectionNewMovie = em.merge(oldGenreidOfMovieCollectionNewMovie);
                    }
                }
            }
            for (TvShow tvShowCollectionOldTvShow : tvShowCollectionOld) {
                if (!tvShowCollectionNew.contains(tvShowCollectionOldTvShow)) {
                    tvShowCollectionOldTvShow.setGenreid(null);
                    tvShowCollectionOldTvShow = em.merge(tvShowCollectionOldTvShow);
                }
            }
            for (TvShow tvShowCollectionNewTvShow : tvShowCollectionNew) {
                if (!tvShowCollectionOld.contains(tvShowCollectionNewTvShow)) {
                    Genre oldGenreidOfTvShowCollectionNewTvShow = tvShowCollectionNewTvShow.getGenreid();
                    tvShowCollectionNewTvShow.setGenreid(genre);
                    tvShowCollectionNewTvShow = em.merge(tvShowCollectionNewTvShow);
                    if (oldGenreidOfTvShowCollectionNewTvShow != null && !oldGenreidOfTvShowCollectionNewTvShow.equals(genre)) {
                        oldGenreidOfTvShowCollectionNewTvShow.getTvShowCollection().remove(tvShowCollectionNewTvShow);
                        oldGenreidOfTvShowCollectionNewTvShow = em.merge(oldGenreidOfTvShowCollectionNewTvShow);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = genre.getId();
                if (findGenre(id) == null) {
                    throw new NonexistentEntityException("The genre with id " + id + " no longer exists.");
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
            Genre genre;
            try {
                genre = em.getReference(Genre.class, id);
                genre.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genre with id " + id + " no longer exists.", enfe);
            }
            Collection<Movie> movieCollection = genre.getMovieCollection();
            for (Movie movieCollectionMovie : movieCollection) {
                movieCollectionMovie.setGenreid(null);
                movieCollectionMovie = em.merge(movieCollectionMovie);
            }
            Collection<TvShow> tvShowCollection = genre.getTvShowCollection();
            for (TvShow tvShowCollectionTvShow : tvShowCollection) {
                tvShowCollectionTvShow.setGenreid(null);
                tvShowCollectionTvShow = em.merge(tvShowCollectionTvShow);
            }
            em.remove(genre);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genre> findGenreEntities() {
        return findGenreEntities(true, -1, -1);
    }

    public List<Genre> findGenreEntities(int maxResults, int firstResult) {
        return findGenreEntities(false, maxResults, firstResult);
    }

    private List<Genre> findGenreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genre.class));
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

    public Genre findGenre(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genre.class, id);
        } finally {
            em.close();
        }
    }

    public int getGenreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genre> rt = cq.from(Genre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
