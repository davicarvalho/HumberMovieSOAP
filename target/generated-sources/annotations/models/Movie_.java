package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Genre;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-05T12:09:13")
@StaticMetamodel(Movie.class)
public class Movie_ { 

    public static volatile SingularAttribute<Movie, Genre> genreid;
    public static volatile SingularAttribute<Movie, Integer> year;
    public static volatile SingularAttribute<Movie, Integer> id;
    public static volatile SingularAttribute<Movie, String> title;

}