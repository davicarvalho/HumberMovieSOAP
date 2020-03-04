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
@Table(name = "tv_show")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TvShow.findAll", query = "SELECT t FROM TvShow t"),
    @NamedQuery(name = "TvShow.findById", query = "SELECT t FROM TvShow t WHERE t.id = :id"),
    @NamedQuery(name = "TvShow.findByTitle", query = "SELECT t FROM TvShow t WHERE t.title = :title"),
    @NamedQuery(name = "TvShow.findByYearStart", query = "SELECT t FROM TvShow t WHERE t.yearStart = :yearStart"),
    @NamedQuery(name = "TvShow.findByYearEnd", query = "SELECT t FROM TvShow t WHERE t.yearEnd = :yearEnd")})
public class TvShow implements Serializable {

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
    @Column(name = "year_start")
    private int yearStart;
    @Column(name = "year_end")
    private Integer yearEnd;
    @JoinColumn(name = "genreid", referencedColumnName = "id")
    @ManyToOne
    private Genre genreid;
    @OneToMany(mappedBy = "showid")
    private Collection<Season> seasonCollection;

    public TvShow() {
    }

    public TvShow(Integer id) {
        this.id = id;
    }

    public TvShow(Integer id, String title, int yearStart) {
        this.id = id;
        this.title = title;
        this.yearStart = yearStart;
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

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
    }

    public Genre getGenreid() {
        return genreid;
    }

    public void setGenreid(Genre genreid) {
        this.genreid = genreid;
    }

    @XmlTransient
    public Collection<Season> getSeasonCollection() {
        return seasonCollection;
    }

    public void setSeasonCollection(Collection<Season> seasonCollection) {
        this.seasonCollection = seasonCollection;
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
        if (!(object instanceof TvShow)) {
            return false;
        }
        TvShow other = (TvShow) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.TvShow[ id=" + id + " ]";
    }
    
}
