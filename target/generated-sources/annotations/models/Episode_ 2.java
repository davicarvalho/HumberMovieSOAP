package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Season;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-04-05T13:46:45")
@StaticMetamodel(Episode.class)
public class Episode_ { 

    public static volatile SingularAttribute<Episode, Season> seasonid;
    public static volatile SingularAttribute<Episode, Integer> id;
    public static volatile SingularAttribute<Episode, String> title;
    public static volatile SingularAttribute<Episode, Integer> episodeNumber;

}