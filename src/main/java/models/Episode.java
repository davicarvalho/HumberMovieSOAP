/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author davicarvalho
 */
@Entity
@Table(name = "episode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Episode.findAll", query = "SELECT e FROM Episode e"),
    @NamedQuery(name = "Episode.findById", query = "SELECT e FROM Episode e WHERE e.id = :id"),
    @NamedQuery(name = "Episode.findByEpisodeNumber", query = "SELECT e FROM Episode e WHERE e.episodeNumber = :episodeNumber"),
    @NamedQuery(name = "Episode.findByTitle", query = "SELECT e FROM Episode e WHERE e.title = :title")})
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "episode_number")
    private Integer episodeNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @JoinColumn(name = "seasonid", referencedColumnName = "id")
    @ManyToOne
    private Season seasonid;

    public Episode() {
    }

    public Episode(Integer id) {
        this.id = id;
    }

    public Episode(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Season getSeasonid() {
        return seasonid;
    }

    public void setSeasonid(Season seasonid) {
        this.seasonid = seasonid;
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
        if (!(object instanceof Episode)) {
            return false;
        }
        Episode other = (Episode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Episode[ id=" + id + " ]";
    }
    
}
