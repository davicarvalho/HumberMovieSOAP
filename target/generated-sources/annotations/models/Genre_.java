package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Movie;
import models.TvShow;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-05T12:09:13")
@StaticMetamodel(Genre.class)
public class Genre_ { 

    public static volatile CollectionAttribute<Genre, Movie> movieCollection;
    public static volatile CollectionAttribute<Genre, TvShow> tvShowCollection;
    public static volatile SingularAttribute<Genre, String> name;
    public static volatile SingularAttribute<Genre, Integer> id;

}