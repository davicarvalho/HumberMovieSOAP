/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author davicarvalho
 */
@Entity
@Table(name = "season")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Season.findAll", query = "SELECT s FROM Season s"),
    @NamedQuery(name = "Season.findById", query = "SELECT s FROM Season s WHERE s.id = :id"),
    @NamedQuery(name = "Season.findByTitle", query = "SELECT s FROM Season s WHERE s.title = :title"),
    @NamedQuery(name = "Season.findByYear", query = "SELECT s FROM Season s WHERE s.year = :year")})
public class Season implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    private int year;
    @JoinColumn(name = "showid", referencedColumnName = "id")
    @ManyToOne
    private TvShow showid;
    @OneToMany(mappedBy = "seasonid")
    private Collection<Episode> episodeCollection;

    public Season() {
    }

    public Season(Integer id) {
        this.id = id;
    }

    public Season(Integer id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public TvShow getShowid() {
        return showid;
    }

    public void setShowid(TvShow showid) {
        this.showid = showid;
    }

    @XmlTransient
    public Collection<Episode> getEpisodeCollection() {
        return episodeCollection;
    }

    public void setEpisodeCollection(Collection<Episode> episodeCollection) {
        this.episodeCollection = episodeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Season)) {
            return false;
        }
        Season other = (Season) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Season[ id=" + id + " ]";
    }
    
}
