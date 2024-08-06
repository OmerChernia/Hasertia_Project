package il.cshaifasweng.OCSFMediatorExample.client.util.generators;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBGenerate {

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

    public DBGenerate() {
        initializeDatabase();
    }

    public void initializeDatabase() {
        try {
            // Initialize lists
            employees = new ArrayList<>();
            registeredUsers = new ArrayList<>();
            theaterManagers = new ArrayList<>();
            theaters = new ArrayList<>();
            movies = new ArrayList<>();
            priceRequests = new ArrayList<>();
            halls = new ArrayList<>();
            seats = new ArrayList<>();
            movieInstances = new ArrayList<>();
            purchases = new ArrayList<>();
            complaints = new ArrayList<>();

            // Generate data
            employees = generateEmployees();
            registeredUsers = generateRegisteredUsers();
            theaterManagers = generateTheaterManagers();
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

        for (int i = 0; i < cities.size(); i++) {
            if (i >= theaterManagers.size()) {
                break;
            }
            TheaterManager manager = theaterManagers.get(i);

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
                new Movie(1, "קפטן אמריקה", "Steve Rogers, a rejected military soldier transforms into Captain America after taking a dose of a 'Super-Soldier serum'.", "Marvel Studios", "Captain America", List.of("Chris Evans", "Sebastian Stan", "Hayley Atwell"), "captainamerica.jpg", Movie.StreamingType.BOTH, 124, 50, 60, "action"),
                new Movie(2, "צ'לנג'רס", "A drama centered around three friends, their ambitions, and the competitive tennis circuit.", "MGM", "Challengers", List.of("Zendaya", "Mike Faist", "Josh O'Connor"), "challengers.jpg", Movie.StreamingType.BOTH, 120, 50, 60, "drama"),
                new Movie(3, "דדפול וולברין", "Deadpool teams up with Wolverine for a new mission that involves time travel.", "Marvel Studios", "Deadpool & Wolverine", List.of("Ryan Reynolds", "Hugh Jackman", "Morena Baccarin"), "deadpool-wolverine.jpg", Movie.StreamingType.BOTH, 115, 50, 60, "action"),
                new Movie(4, "גלדיאטור 2", "The story continues with Maximus's son seeking revenge against the Roman Empire.", "Universal Pictures", "Gladiator II", List.of("Russell Crowe", "Joaquin Phoenix", "Connie Nielsen"), "gladiator_ii.jpg", Movie.StreamingType.BOTH, 155, 50, 60, "action"),
                new Movie(5, "הקול בראש 2", "The emotions inside Riley's mind return for a new adventure.", "Pixar Animation Studios", "Inside Out 2", List.of("Amy Poehler", "Bill Hader", "Lewis Black"), "inside_out_two.jpg", Movie.StreamingType.BOTH, 95, 50, 60, "animation"),
                new Movie(6, "לה לה לנד", "A jazz musician and an aspiring actress fall in love while pursuing their dreams in Los Angeles.", "Lionsgate", "La La Land", List.of("Ryan Gosling", "Emma Stone", "John Legend"), "LaLaLand.jpg", Movie.StreamingType.BOTH, 128, 50, 60, "musical"),
                new Movie(7, "מופאסה: מלך האריות", "A prequel to The Lion King focusing on Mufasa's journey to become king.", "Walt Disney Pictures", "Mufasa: The Lion King", List.of("James Earl Jones", "Donald Glover", "Beyoncé"), "mufasa-the-lion-king.jpg", Movie.StreamingType.BOTH, 120, 50, 60, "animation"),
                new Movie(8, "אין רגשות קשים", "A comedy about a couple trying to balance their careers and relationship.", "Columbia Pictures", "No Hard Feelings", List.of("Jennifer Lawrence", "Andrew Barth Feldman", "Laura Benanti"), "no-hard-feelings.jpg", Movie.StreamingType.BOTH, 103, 50, 60, "comedy"),
                new Movie(9, "אופנהיימר", "A drama about J. Robert Oppenheimer, the father of the atomic bomb.", "Universal Pictures", "Oppenheimer", List.of("Cillian Murphy", "Emily Blunt", "Robert Downey Jr."), "oppenheimer.jpg", Movie.StreamingType.BOTH, 180, 50, 60, "drama"),
                new Movie(10, "פינוקיו", "A live-action adaptation of the classic tale of a wooden puppet who wants to become a real boy.", "Walt Disney Pictures", "Pinocchio", List.of("Tom Hanks", "Benjamin Evan Ainsworth", "Joseph Gordon-Levitt"), "pinocchio.jpg", Movie.StreamingType.BOTH, 105, 50, 60, "fantasy"),
                new Movie(11, "סמייל 2", "A sequel to the horror film 'Smile', where the curse continues to haunt new victims.", "Paramount Pictures", "Smile 2", List.of("Sosie Bacon", "Kyle Gallner", "Caitlin Stasey"), "smile_two.jpg", Movie.StreamingType.BOTH, 115, 50, 60, "horror"),
                new Movie(12, "מלחמת הכוכבים: פרק 1 - אימת הפאנטום", "The origin story of Anakin Skywalker and the rise of the Sith.", "Lucasfilm", "Star Wars: Episode I - The Phantom Menace", List.of("Liam Neeson", "Ewan McGregor", "Natalie Portman"), "Star_Wars_Episode_1.jpg", Movie.StreamingType.BOTH, 136, 50, 60, "sci-fi"),
                new Movie(13, "רובוטריקים", "The story of the war between Autobots and Decepticons on Earth.", "Paramount Pictures", "Transformers", List.of("Shia LaBeouf", "Megan Fox", "Josh Duhamel"), "transformers_one.jpg", Movie.StreamingType.BOTH, 144, 50, 60, "action"),
                new Movie(14, "מרושעת", "A prequel to The Wizard of Oz, focusing on the story of the Wicked Witch of the West.", "Universal Pictures", "Wicked", List.of("Idina Menzel", "Kristin Chenoweth", "Ariana Grande"), "wicked.jpg", Movie.StreamingType.BOTH, 130, 50, 60, "fantasy"),
                new Movie(15, "מלך האריות", "After the murder of his father, a young lion prince flees his kingdom only to learn the true meaning of responsibility and bravery.", "Walt Disney Pictures", "The Lion King", List.of("Donald Glover", "Beyoncé", "Seth Rogen"), "TheLionKing.jpg", Movie.StreamingType.BOTH, 118, 50, 60, "animation"),
                new Movie(16, "ברבי", "To live in Barbie Land is to be a perfect being in a perfect place. Unless you have a full-on existential crisis. Or you’re a Ken.", "Warner Bros.", "Barbie", List.of("Margot Robbie", "Ryan Gosling", "Simu Liu"), "Barbie.jpg", Movie.StreamingType.BOTH, 114, 50, 60, "comedy"),
                new Movie(17, "סיפור צעצוע 4", "The adventures of Woody, Buzz Lightyear, and the gang as they encounter new toys.", "Pixar Animation Studios", "Toy Story 4", List.of("Tom Hanks", "Tim Allen", "Annie Potts"), "toy_story4.jpg", Movie.StreamingType.BOTH, 100, 50, 60, "animation"),
                new Movie(18, "וונדר וומן ", "Diana, princess of the Amazons, discovers her full powers and true destiny as Wonder Woman.", "Warner Bros.", "Wonder Woman", List.of("Gal Gadot", "Chris Pine", "Robin Wright"), "wonder_woman2017.jpg", Movie.StreamingType.BOTH, 141, 50, 60, "action"),
                new Movie(19, "המיניונים: עלייתו של גרו", "The untold story of one twelve-year-old's dream to become the world's greatest supervillain.", "Universal Pictures", "Minions: The Rise of Gru", List.of("Steve Carell", "Pierre Coffin", "Taraji P. Henson"), "minions_the_rise_of_gru.jpg", Movie.StreamingType.BOTH, 90, 50, 60, "animation"),
                new Movie(20, "הג'וקר 2", "A dark origin story about the Joker's transformation from struggling comedian to a criminal mastermind.", "Warner Bros.", "The Joker 2", List.of("Joaquin Phoenix", "Robert De Niro", "Lady Gaga"), "the_joker2024.jpg", Movie.StreamingType.BOTH, 122, 50, 60, "drama")
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
            for (int i = 1; i <= 5; i++) {
                Hall hall = new Hall();
                hall.setName( " Hall " + i);
                hall.setCapacity(100 + (i * 20)); // Different sizes for halls
                hall.setTheater(theater);
                halls.add(hall);
                seats.addAll(generateSeats(hall, hall.getCapacity()));
            }
        }
        return halls;
    }

    private List<Seat> generateSeats(Hall hall, int capacity) {
        List<Seat> seats = new ArrayList<>();
        int rows = capacity / 20; // Example: Adjust rows based on capacity
        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= 20; col++) {
                Seat seat = new Seat(row, col);
                seat.setHall(hall);

                seats.add(seat);
            }
        }
        hall.setSeats(seats);
        return seats;
    }

    private List<MovieInstance> generateMovieInstances() {
        List<MovieInstance> instances = new ArrayList<>();
        for (Hall hall : halls) {
            for (Movie movie : movies) {
                for (int i = 0; i < 3; i++) { // Adding 3 showtimes for each movie
                    LocalDateTime showtime = LocalDateTime.now().plusDays(instances.size() + 1).plusHours(i * 3);
                    instances.add(new MovieInstance(movie, showtime, hall));
                }
            }
        }
        return instances;
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

        // Add expired purchases
        for (int i = 0; i < 5; i++) {
            RegisteredUser user = registeredUsers.get(i % registeredUsers.size());

            HomeViewingPackageInstance expiredHomePackage = new HomeViewingPackageInstance(
                    LocalDateTime.now().minusMonths(1),
                    user,
                    "expired_validation" + i,
                    movies.get(i % movies.size()),
                    LocalDateTime.now().minusMonths(1).plusDays(1),
                    100 + i
            );
            purchases.add(expiredHomePackage);

            MovieTicket expiredMovieTicket = new MovieTicket(
                    LocalDateTime.now().minusMonths(1),
                    user,
                    "expired_validation" + (i + 5),
                    movieInstances.get(i % movieInstances.size()),
                    seats.get(i % seats.size())
            );
            purchases.add(expiredMovieTicket);

            Seat expiredSeat = expiredMovieTicket.getSeat();
            expiredSeat.addMovieInstance(expiredMovieTicket.getMovieInstance());

            MultiEntryTicket expiredMultiEntryTicket = new MultiEntryTicket(
                    LocalDateTime.now().minusMonths(1),
                    user,
                    "expired_validation" + (i + 10)
            );
            purchases.add(expiredMultiEntryTicket);
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
