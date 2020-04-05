package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Genre;
import models.Season;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-04-05T13:55:38")
@StaticMetamodel(TvShow.class)
public class TvShow_ { 

    public static volatile SingularAttribute<TvShow, Genre> genreid;
    public static volatile SingularAttribute<TvShow, Integer> yearEnd;
    public static volatile SingularAttribute<TvShow, Integer> id;
    public static volatile SingularAttribute<TvShow, String> title;
    public static volatile SingularAttribute<TvShow, Integer> yearStart;
    public static volatile CollectionAttribute<TvShow, Season> seasonCollection;

}