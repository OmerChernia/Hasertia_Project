package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Messages.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Messages.MovieMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MovieHandler extends MessageHandler
{
    private MovieMessage message;

    public MovieHandler(MovieMessage message, ConnectionToClient client, Session session)
    {
        super(client,session);
        this.message = message;
    }

    public void handleMessage()
    {
        switch (message.requestType)
        {
            case ADD_MOVIE -> add_movie();
            case DEACTIVATE_MOVIE -> deactivate_movie();
            case GET_ALL_MOVIES -> get_all_movies();
            case UPDATE_MOVIE -> update_movie();
            case GET_MOVIES_PRESENTED_IN_THEATER -> getMoviesPresentedInTheater();
            case GET_MOVIES_PRESENTED_IN_HOME_VIEWING -> getMoviesPresentedInHomeViewing();
            case GET_MOVIES_FILTERED_BY_SCREENING_TYPE_AND_GENRE -> getMoviesFilteredByScreeningTypeAndGenre();
        }
    }

    private void getMoviesFilteredByScreeningTypeAndGenre() {
        System.out.println("filter by " + message.Screening + " and " + message.Genre);

        try {
            // Start building the HQL query
            StringBuilder hql = new StringBuilder("FROM Movie WHERE isActive = :active");

            // Conditionally append screening type filtering
            if (message.Screening != Movie.StreamingType.BOTH) {
                hql.append(" AND (streamingType = :type OR streamingType = :both)");
            }

            // Conditionally append genre filtering
            if (message.Genre != null && !message.Genre.equals("all")) {
                hql.append(" AND genre = :genre");
            }

            // Create the query with the constructed HQL
            Query<Movie> query = session.createQuery(hql.toString(), Movie.class);

            // Set common parameters
            query.setParameter("available", Movie.Availability.AVAILABLE);

            // Set screening type parameters if applicable
            if (message.Screening != Movie.StreamingType.BOTH) {
                query.setParameter("type", message.Screening);
                query.setParameter("both", Movie.StreamingType.BOTH);
            }

            // Set genre parameter if applicable
            if (message.Genre != null && !message.Genre.equals("all")) {
                query.setParameter("genre", message.Genre);
            }

            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieMessage.ResponseType.RETURN_MOVIES;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }


    private void getMoviesPresentedInHomeViewing() {
        try {
            // Create an HQL query to fetch movies with streamingType HOME_VIEWING or BOTH
            Query<Movie> query = session.createQuery(
                    "FROM Movie WHERE (streamingType = :home OR streamingType = :both) AND available = :available",
                    Movie.class
            );
            query.setParameter("home", Movie.StreamingType.HOME_VIEWING);
            query.setParameter("both", Movie.StreamingType.BOTH);
            query.setParameter("available", Movie.Availability.AVAILABLE);


            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieMessage.ResponseType.RETURN_MOVIES;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    private void getMoviesPresentedInTheater() {
        try {
            // Create an HQL query to fetch movies with streamingType THEATER_VIEWING or BOTH
            Query<Movie> query = session.createQuery(
                    "FROM Movie WHERE (streamingType = :theater OR streamingType = :both) AND available = :available",
                    Movie.class
            );
            query.setParameter("theater", Movie.StreamingType.THEATER_VIEWING);
            query.setParameter("both", Movie.StreamingType.BOTH);
            query.setParameter("available", Movie.Availability.AVAILABLE);

            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieMessage.ResponseType.RETURN_MOVIES;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }


    @Override
    public void setMessageTypeToResponse()
    {
        message.messageType= Message.MessageType.RESPONSE;
    }

    private void add_movie()
    {
        if(message.movies.getFirst() != null)
        {
            // Create an HQL query to fetch all complaints
            // Searching if the movie is existed in DB
            Query<Movie> query = session.createQuery("FROM Movie WHERE englishName = :_englishName or hebrewName = :_hebrewName", Movie.class);
            query.setParameter("_englishName", message.movies.getFirst().getEnglishName());
            query.setParameter("_hebrewName", message.movies.getFirst().getHebrewName());

            List<Movie> movies = query.list();

            if(movies == null) {

                session.save(message.movies.getFirst());
                session.flush();
                message.responseType = MovieMessage.ResponseType.MOVIE_ADDED;
            }
            else // if movie already existed , don't add it
                message.responseType = MovieMessage.ResponseType.MOVIE_NOT_ADDED;
        }
        else // if we don't have any movie to add
            message.responseType = MovieMessage.ResponseType.MOVIE_NOT_ADDED;
    }
    private void deactivate_movie()
    {
        Query<Movie> query = session.createQuery("FROM Movie where id= :_id", Movie.class);
        query.setParameter("_id", message.id);

        Movie movie = query.uniqueResult();

        if(movie != null)
        {
            movie.setActive(Movie.Availability.NOT_AVAILABLE);
            session.update(message.movies.getFirst());
            session.flush();
            message.responseType = MovieMessage.ResponseType.MOVIE_UPDATED;
        }
        else
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;


    }
    private void get_all_movies()
    {
        try {
            // Create an HQL query to fetch all complaints
            Query<Movie> query = session.createQuery("FROM Movie where available= :_available", Movie.class);
            query.setParameter("_available", Movie.Availability.AVAILABLE);
            // Execute the query and get the result list
            message.movies = query.getResultList();

            // Set the response type
            message.responseType = MovieMessage.ResponseType.RETURN_MOVIES;

        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }
    private void update_movie() {
        try {
            // Get the movie ID from the incoming message
            int movieId = message.movies.getFirst().getId();

            // Retrieve the current persistent instance of the movie from the session
            Movie persistentMovie = session.get(Movie.class, movieId);

            if (persistentMovie != null) {
                // Update the persistent movie with the new values from the message
                persistentMovie.setEnglishName(message.movies.getFirst().getEnglishName());
                persistentMovie.setHebrewName(message.movies.getFirst().getHebrewName());
                persistentMovie.setProducer(message.movies.getFirst().getProducer());
                persistentMovie.setDuration(message.movies.getFirst().getDuration());
                persistentMovie.setTheaterPrice(message.movies.getFirst().getTheaterPrice());
                persistentMovie.setHomeViewingPrice(message.movies.getFirst().getHomeViewingPrice());
                persistentMovie.setGenre(message.movies.getFirst().getGenre());
                persistentMovie.setStreamingType(message.movies.getFirst().getStreamingType());
                persistentMovie.setInfo(message.movies.getFirst().getInfo());
                String joinedString = String.join("_", message.movies.getFirst().getMainActors());
                persistentMovie.setMainActors(joinedString);

                // Save the changes
                session.update(persistentMovie);
                session.flush();
                message.responseType = MovieMessage.ResponseType.MOVIE_UPDATED;
            } else {
                message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.responseType = MovieMessage.ResponseType.MOVIE_MESSAGE_FAILED;
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

}