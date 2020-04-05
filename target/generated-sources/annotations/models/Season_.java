package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Episode;
import models.TvShow;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-04-05T13:55:38")
@StaticMetamodel(Season.class)
public class Season_ { 

    public static volatile SingularAttribute<Season, TvShow> showid;
    public static volatile SingularAttribute<Season, Integer> year;
    public static volatile CollectionAttribute<Season, Episode> episodeCollection;
    public static volatile SingularAttribute<Season, Integer> id;
    public static volatile SingularAttribute<Season, String> title;

}