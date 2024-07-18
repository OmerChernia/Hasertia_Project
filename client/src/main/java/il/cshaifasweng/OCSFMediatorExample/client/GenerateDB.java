package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GenerateDB {

    private List<RegisteredUser> registeredUsers;
    private List<Employee> employees;
    private List<TheaterManager> theaterManagers;
    private List<Theater> theaters;
    private List<Movie> movies;
    private List<PriceRequest> priceRequests;
    private List<Hall> halls;
    private List<Seat> seats;
    private List<MovieInstance> movieInstances;
    private List<Purchase> purchases;
    private List<Complaint> complaints;

    public GenerateDB() {
        initializeDatabase();
    }

    public void initializeDatabase() {
        try {
            // Generate Employees
            employees = generateEmployees();

            // Generate Registered Users
            registeredUsers = generateRegisteredUsers();

            // Generate Theater Managers
            theaterManagers = generateTheaterManagers();

            // Generate Theaters and Assign Managers
            theaters = generateTheatersAndAssignManagers();

            movies = generateMovies();

            priceRequests = generatePriceRequests();
            halls = generateHallsAndSeats();
            movieInstances = generateMovieInstances();
            purchases = generatePurchases();
            complaints = generateComplaints();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<RegisteredUser> generateRegisteredUsers() {
        return List.of(
                new RegisteredUser("318111222", "Alice Smith", true, "alice@example.com", 0),
                new RegisteredUser("284619537", "Bob Johnson", false, "bob@example.com", 0),
                new RegisteredUser("719305846", "Charlie Brown", true, "charlie@example.com", 0),
                new RegisteredUser("562890143", "David Wilson", false, "david@example.com", 0),
                new RegisteredUser("938472615", "Eva Davis", true, "eva@example.com", 0),
                new RegisteredUser("451298763", "Frank Harris", false, "frank@example.com", 0),
                new RegisteredUser("895631247", "Grace Lee", true, "grace@example.com", 0),
                new RegisteredUser("603815927", "Henry King", false, "henry@example.com", 1),
                new RegisteredUser("748596321", "Ivy Martin", true, "ivy@example.com", 8),
                new RegisteredUser("106753829", "Jack White", false, "jack@example.com", 20)
        );
    }

    private List<Employee> generateEmployees() {
        return List.of(
                new Employee("705182943", "John Doe", true, "password10", Employee.EmployeeType.COMPANY_MANAGER),
                new Employee("238947615", "Jane Smith", false, "password120", Employee.EmployeeType.CUSTOMER_SERVICE),
                new Employee("864205739", "Michael Johnson", true, "password1230", Employee.EmployeeType.CONTENT_MANAGER)
        );
    }

    private List<TheaterManager> generateTheaterManagers() {
        return List.of(
                new TheaterManager("847365912", "Josh Bee", true, "password1", Employee.EmployeeType.THEATER_MANAGER, null),
                new TheaterManager("512089637", "Mili Tik", false, "password12", Employee.EmployeeType.THEATER_MANAGER, null),
                new TheaterManager("396824571", "Bob Back", true, "password123", Employee.EmployeeType.THEATER_MANAGER, null),
                new TheaterManager("483944123", "Emily Davis", false, "password1234", Employee.EmployeeType.THEATER_MANAGER, null),
                new TheaterManager("591736842", "Daniel Brown", true, "password1235", Employee.EmployeeType.THEATER_MANAGER, null)
        );
    }

    private List<Theater> generateTheatersAndAssignManagers() {
        List<Theater> theaters = new ArrayList<>();
        List<String> cities = List.of("Tel Aviv", "Jerusalem", "Haifa", "Beer Sheva", "Eilat");

        List<TheaterManager> managers = generateTheaterManagers();

        for (int i = 0; i < cities.size(); i++) {
            if (i >= managers.size()) {
                break;
            }
            TheaterManager manager = managers.get(i);

            Theater theater = new Theater();
            theater.setLocation(cities.get(i));
            theater.setManager(manager);

            manager.setTheater(theater);
            theaters.add(theater);
        }
        return theaters;
    }

    private List<Movie> generateMovies() {
        return List.of(
                new Movie("מלך האריות", "After the murder of his father, a young lion prince flees his kingdom only to learn the true meaning of responsibility and bravery.", "Walt Disney Pictures", "The Lion King", List.of("Donald Glover", "Beyoncé", "Seth Rogen"), "TheLionKing.jpg", Movie.StreamingType.BOTH, 118, 50, 60, "animation"),
                new Movie("ילדות רעות", "Cady Heron is a hit with The Plastics, the A-list girl clique at her new school, until she makes the mistake of falling for Aaron Samuels, the ex-boyfriend of alpha Plastic Regina George.", "Paramount Pictures", "Mean Girls", List.of("Lindsay Lohan", "Rachel McAdams", "Tina Fey"), "MeanGirls.jpeg", Movie.StreamingType.BOTH, 97, 50, 60, "comedy"),
                new Movie("ברבי", "To live in Barbie Land is to be a perfect being in a perfect place. Unless you have a full-on existential crisis. Or you’re a Ken.", "Warner Bros.", "Barbie", List.of("Margot Robbie", "Ryan Gosling", "Simu Liu"), "Barbie.jpg", Movie.StreamingType.BOTH, 114, 50, 60, "comedy"),
                new Movie("הקול בראש", "After young Riley is uprooted from her Midwest life and moved to San Francisco, her emotions - Joy, Fear, Anger, Disgust and Sadness - conflict on how best to navigate a new city, house, and school.", "Pixar Animation Studios", "Inside Out", List.of("Amy Poehler", "Bill Hader", "Lewis Black"), "InsideOut.jpg", Movie.StreamingType.BOTH, 95, 50, 60, "animation"),
                new Movie("האביר האפל", "When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.", "Warner Bros.", "The Dark Knight", List.of("Christian Bale", "Heath Ledger", "Aaron Eckhart"), "DarkKnight.jpg", Movie.StreamingType.THEATER_VIEWING, 152, 50, 0, "action"),
                new Movie("גלדיאטור", "A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.", "Universal Pictures", "Gladiator", List.of("Russell Crowe", "Joaquin Phoenix", "Connie Nielsen"), "Gladiator.jpg", Movie.StreamingType.BOTH, 155, 50, 60, "action"),
                new Movie("מלחמת הכוכבים: פרק 4 - תקווה חדשה", "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station.", "Lucasfilm", "Star Wars: Episode IV - A New Hope", List.of("Mark Hamill", "Harrison Ford", "Carrie Fisher"), "StarWars.jpg", Movie.StreamingType.HOME_VIEWING, 121, 0, 60, "sci-fi"),
                new Movie("הסנדק", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", "Paramount Pictures", "The Godfather", List.of("Marlon Brando", "Al Pacino", "James Caan"), "Godfather.jpg", Movie.StreamingType.BOTH, 175, 50, 60, "crime")
        );
    }


    private List<PriceRequest> generatePriceRequests() {
        return List.of(
                new PriceRequest(20, movies.get(0), Movie.StreamingType.HOME_VIEWING),
                new PriceRequest(25, movies.get(1), Movie.StreamingType.HOME_VIEWING),
                new PriceRequest(18, movies.get(2), Movie.StreamingType.THEATER_VIEWING),
                new PriceRequest(22, movies.get(3), Movie.StreamingType.THEATER_VIEWING)
        );
    }

    private List<Hall> generateHallsAndSeats() {
        List<Hall> halls = new ArrayList<>();
        for (Theater theater : theaters) {
            for (int i = 1; i <= 2; i++) {
                Hall hall = new Hall();
                hall.setName(theater.getLocation() + " Hall " + i);
                hall.setCapacity(100);
                hall.setTheater(theater);
                halls.add(hall);
                seats.addAll(generateSeats(hall));
            }
        }
        return halls;
    }

    private List<Seat> generateSeats(Hall hall) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 5; col++) {
                Seat seat = new Seat(row, col);
                seat.setHall(hall);
                seats.add(seat);
            }
        }
        hall.setSeats(seats);
        return seats;
    }

    private List<MovieInstance> generateMovieInstances() {
        return List.of(
                new MovieInstance(movies.get(0), LocalDateTime.now().plusDays(1), halls.get(0)),
                new MovieInstance(movies.get(1), LocalDateTime.now().plusDays(2), halls.get(1)),
                new MovieInstance(movies.get(2), LocalDateTime.now().plusDays(3), halls.get(2)),
                new MovieInstance(movies.get(3), LocalDateTime.now().plusDays(4), halls.get(3)),
                new MovieInstance(movies.get(4), LocalDateTime.now().plusDays(5), halls.get(4)),
                new MovieInstance(movies.get(5), LocalDateTime.now().plusDays(6), halls.get(5)),
                new MovieInstance(movies.get(6), LocalDateTime.now().plusDays(7), halls.get(6)),
                new MovieInstance(movies.get(7), LocalDateTime.now().plusDays(8), halls.get(7)),
                new MovieInstance(movies.get(8), LocalDateTime.now().plusDays(9), halls.get(8)),
                new MovieInstance(movies.get(9), LocalDateTime.now().plusDays(10), halls.get(9))
        );
    }

    private List<Purchase> generatePurchases() {
        List<Purchase> purchases = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RegisteredUser user = registeredUsers.get(i % registeredUsers.size());

            HomeViewingPackageInstance homePackage = new HomeViewingPackageInstance(
                    LocalDateTime.now(),
                    user,
                    "validation" + i,
                    movies.get(i % movies.size()),
                    LocalDateTime.now().plusDays(1),
                    100 + i
            );
            purchases.add(homePackage);

            MovieTicket movieTicket = new MovieTicket(
                    LocalDateTime.now(),
                    user,
                    "validation" + (i + 5),
                    movieInstances.get(i % movieInstances.size()),
                    seats.get(i % seats.size())
            );
            purchases.add(movieTicket);

            Seat seat = movieTicket.getSeat();
            seat.addMovieInstance(movieTicket.getMovieInstance());

            MultiEntryTicket multiEntryTicket = new MultiEntryTicket(
                    LocalDateTime.now(),
                    user,
                    "validation" + (i + 10)
            );
            purchases.add(multiEntryTicket);
        }
        return purchases;
    }

    private List<Complaint> generateComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Purchase purchase = purchases.get(i % purchases.size());
            RegisteredUser user = purchase.getOwner();

            Complaint complaint = new Complaint(
                    "Complaint info " + i,
                    LocalDateTime.now(),
                    purchase,
                    false,
                    user
            );
            complaints.add(complaint);
        }
        return complaints;
    }

    public List<RegisteredUser> getRegisteredUsers() {
        return registeredUsers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<TheaterManager> getTheaterManagers() {
        return theaterManagers;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<PriceRequest> getPriceRequests() {
        return priceRequests;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<MovieInstance> getMovieInstances() {
        return movieInstances;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }
}
