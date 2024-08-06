package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GenerateDB {

    private final Session session;

    public GenerateDB(Session session) {
        this.session = session;
    }

    public void initializeDatabase() {
        try {
            // Generate Employees
            generateEmployees();

            // Generate Registered Users
            generateRegisteredUsers();

            // Generate Theater Managers
            generateTheaterManagers();

            // Generate Theaters and Assign Managers
            generateTheatersAndAssignManagers();

            generateMovies();


            generatePriceRequests();
            generateHallsAndSeats();
            generateMovieInstances();
            generatePurchases();
            generateComplaints();
        }
        catch (Exception e)
        {
            if (session != null)
            {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            e.printStackTrace();
        }

    }

    private void generateRegisteredUsers() {
        List<RegisteredUser> users = session.createQuery("from RegisteredUser", RegisteredUser.class).list();
        if (users.isEmpty()) {
            users = List.of(
                    new RegisteredUser("318111222","Alice Smith", true, "alice@example.com", 0),
                    new RegisteredUser("284619537","Bob Johnson", false, "bob@example.com", 0),
                    new RegisteredUser("719305846","Charlie Brown", true, "charlie@example.com", 0),
                    new RegisteredUser("562890143","David Wilson", false, "david@example.com", 0),
                    new RegisteredUser("938472615","Eva Davis", true, "eva@example.com", 0),
                    new RegisteredUser("451298763","Frank Harris", false, "frank@example.com", 0),
                    new RegisteredUser("895631247","Grace Lee", true, "grace@example.com", 0),
                    new RegisteredUser("603815927","Henry King", false, "henry@example.com", 1),
                    new RegisteredUser("748596321","Ivy Martin", true, "ivy@example.com", 8),
                    new RegisteredUser("106753829","Jack White", false, "jack@example.com", 20)
            );

            Transaction transaction = session.beginTransaction();
            for (RegisteredUser user : users) {
                session.save(user);
                session.flush();
                System.out.println("Saved registered user: " + user.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Registered Users table is already populated.");
        }
    }

    private void generateEmployees() {
        List<Employee> employees = session.createQuery("from Employee", Employee.class).list();
        if (employees.isEmpty()) {
            employees = List.of(
                    new Employee("705182943","John Doe", true, "password10", Employee.EmployeeType.COMPANY_MANAGER),
                    new Employee("238947615","Jane Smith", false, "password120", Employee.EmployeeType.CUSTOMER_SERVICE),
                    new Employee("864205739","Michael Johnson", true, "password1230", Employee.EmployeeType.CONTENT_MANAGER)
            );

            Transaction transaction = session.beginTransaction();
            for (Employee employee : employees) {
                session.save(employee);
                session.flush();
                System.out.println("Saved employee: " + employee.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Employees table is already populated.");
        }
    }

    private void generateTheaterManagers() {
        List<TheaterManager> managers = session.createQuery("from TheaterManager", TheaterManager.class).list();
        if (managers.isEmpty()) {
            managers = List.of(
                    new TheaterManager("847365912","Josh Bee", true, "password1", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("512089637","Mili Tik", false, "password12", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("396824571","Bob Back", true, "password123", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("483944123","Emily Davis", false, "password1234", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("591736842","Daniel Brown", true, "password1235", Employee.EmployeeType.THEATER_MANAGER, null)
            );

            Transaction transaction = session.beginTransaction();
            for (TheaterManager manager : managers) {
                session.save(manager);
                session.flush();
                System.out.println("Saved Theater Manager: " + manager.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Theater Managers table is already populated.");
        }
    }

    private void generateTheatersAndAssignManagers() {
        List<Theater> theaters = session.createQuery("from Theater", Theater.class).list();
        if (theaters.isEmpty()) {
            List<String> cities = List.of("Tel Aviv", "Jerusalem", "Haifa", "Beer Sheva", "Eilat");

            // Fetch all TheaterManagers
            List<TheaterManager> managers = session.createQuery("from TheaterManager", TheaterManager.class).list();

            Transaction transaction = session.beginTransaction();
            for (int i = 0; i < cities.size(); i++) {
                if (i >= managers.size()) {
                    break;
                }
                TheaterManager manager = managers.get(i);

                // Create a Theater and associate it with the manager
                Theater theater = new Theater();
                theater.setLocation(cities.get(i));
                theater.setManager(manager);
                session.save(theater);
                session.flush();

                // Update manager to set the theater
                manager.setTheater(theater);
                session.update(manager);
                session.flush();

                System.out.println("Saved theater: " + theater.getId() + " with manager: " + manager.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Theaters table is already populated.");
        }
    }

    private void generateMovies() {
        List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
        if (movies.isEmpty()) {
            movies = List.of(
                    new Movie("הסנדק", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", "Albert S. Ruddy", "The Godfather", List.of("Marlon Brando", "Al Pacino", "James Caan"), "godfather.jpg", Movie.StreamingType.BOTH, 175, 50,60,"action"),
                    new Movie("חומות של תקווה", "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", "Niki Marvin", "The Shawshank Redemption", List.of("Tim Robbins", "Morgan Freeman", "Bob Gunton"), "shawshank.jpg", Movie.StreamingType.BOTH, 142, 50, 60,"drama"),
                    new Movie("האביר האפל", "When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.", "Christopher Nolan", "The Dark Knight", List.of("Christian Bale", "Heath Ledger", "Aaron Eckhart"), "dark_knight.jpg", Movie.StreamingType.THEATER_VIEWING, 152,50 , 0,"action"),
                    new Movie("להציל את טוראי ריאן", "Following the Normandy Landings, a group of U.S. soldiers go behind enemy lines to retrieve a paratrooper whose brothers have been killed in action.", "Steven Spielberg", "Saving Private Ryan", List.of("Tom Hanks", "Matt Damon", "Tom Sizemore"), "saving_private_ryan.jpg", Movie.StreamingType.THEATER_VIEWING, 169, 50, 0,"action"),
                    new Movie("מועדון קרב", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.", "Art Linson", "Fight Club", List.of("Brad Pitt", "Edward Norton", "Meat Loaf"), "fight_club.jpg", Movie.StreamingType.BOTH, 139, 50, 60,"action"),
                    new Movie("פורסט גאמפ", "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.", "Wendy Finerman", "Forrest Gump", List.of("Tom Hanks", "Robin Wright", "Gary Sinise"), "forrest_gump.jpg", Movie.StreamingType.BOTH, 142, 50, 60,"kids"),
                    new Movie("הסנדק חלק ב'", "The early life and career of Vito Corleone in 1920s New York City is portrayed, while his son, Michael, expands and tightens his grip on his crime syndicate.", "Francis Ford Coppola", "The Godfather Part II", List.of("Al Pacino", "Robert De Niro", "Robert Duvall"), "godfather_2.jpg", Movie.StreamingType.BOTH, 202, 50, 60,"drama"),
                    new Movie("הטוב, הרע והמכוער", "A bounty hunting scam joins two men in an uneasy alliance against a third in a race to find a fortune in gold buried in a remote cemetery.", "Sergio Leone", "The Good, the Bad and the Ugly", List.of("Clint Eastwood", "Eli Wallach", "Lee Van Cleef"), "good_bad_ugly.jpg", Movie.StreamingType.THEATER_VIEWING, 161, 50, 60,"drama"),
                    new Movie("מלחמת הכוכבים: פרק 4 - תקווה חדשה", "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a Wookiee and two droids to save the galaxy from the Empire's world-destroying battle station.", "Gary Kurtz", "Star Wars: Episode IV - A New Hope", List.of("Mark Hamill", "Harrison Ford", "Carrie Fisher"), "star_wars_iv.jpg", Movie.StreamingType.HOME_VIEWING, 121, 0, 60,"drama"),
                    new Movie("שר הטבעות: אחוות הטבעת", "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.", "Peter Jackson", "The Lord of the Rings: The Fellowship of the Ring", List.of("Elijah Wood", "Ian McKellen", "Orlando Bloom"), "lord_of_the_rings.jpg", Movie.StreamingType.THEATER_VIEWING, 178, 50 , 60,"anima")
            );

            Transaction transaction = session.beginTransaction();
            for (Movie movie : movies) {
                session.save(movie);
                session.flush();
                System.out.println("Saved movie: " + movie.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Movies table is already populated.");
        }
    }

    private void generatePriceRequests() {
        List<PriceRequest> priceRequests = session.createQuery("from PriceRequest", PriceRequest.class).list();
        if (priceRequests.isEmpty()) {
            List<Movie> movies = session.createQuery("from Movie", Movie.class).list();

            // Ensure there are movies in the database
            if (movies.isEmpty()) {
                System.out.println("No movies available to create price requests.");
                return;
            }

            priceRequests = List.of(
                    new PriceRequest(20, movies.get(0), Movie.StreamingType.HOME_VIEWING),
                    new PriceRequest(25, movies.get(1), Movie.StreamingType.HOME_VIEWING),
                    new PriceRequest(18, movies.get(2), Movie.StreamingType.THEATER_VIEWING),
                    new PriceRequest(22, movies.get(3), Movie.StreamingType.THEATER_VIEWING)
            );

            Transaction transaction = session.beginTransaction();
            for (PriceRequest priceRequest : priceRequests) {
                session.save(priceRequest);
                session.flush();
                System.out.println("Saved price request: " + priceRequest.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Price requests table is already populated.");
        }
    }

    private void generateHallsAndSeats() {
        List<Theater> theaters = session.createQuery("from Theater", Theater.class).list();
        if (!theaters.isEmpty()) {
            List<Hall> halls = session.createQuery("from Hall", Hall.class).list();
            if (halls.isEmpty()) {
                Transaction transaction = session.beginTransaction();
                for (Theater theater : theaters) {
                    for (int i = 1; i <= 2; i++) {
                        Hall hall = new Hall();
                        hall.setName(theater.getLocation() + " Hall " + i);
                        hall.setCapacity(100 );
                        hall.setTheater(theater);
                        session.save(hall);
                        generateSeats(hall);
                        session.flush();
                    }
                }
                transaction.commit();
            } else {
                System.out.println("Halls table is already populated.");
            }
        } else {
            System.out.println("No theaters found.");
        }
    }

    private void generateSeats(Hall hall) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 5; col++) {
                Seat seat = new Seat(row, col);
                seat.setHall(hall);
                seats.add(seat);
                session.save(seat);
            }
        }
        hall.setSeats(seats);
        session.update(hall);
        session.flush();
        System.out.println("Saved seats for hall: " + hall.getId());
    }

    private void generateMovieInstances() {
        List<MovieInstance> movieInstances = session.createQuery("from MovieInstance", MovieInstance.class).list();
        if (movieInstances.isEmpty()) {
            List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
            List<Hall> halls = session.createQuery("from Hall", Hall.class).list();

            if (movies.isEmpty() || halls.isEmpty()) {
                System.out.println("No movies or halls found.");
                return;
            }

            movieInstances = List.of(
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

            Transaction transaction = session.beginTransaction();
            for (MovieInstance movieInstance : movieInstances) {
                session.save(movieInstance);
                session.flush();
                System.out.println("Saved movie instance: " + movieInstance.getId());
            }
            transaction.commit();
        } else {
            System.out.println("Movie Instances table is already populated.");
        }
    }

    private void generatePurchases() {
        List<Purchase> existingPurchases = session.createQuery("from Purchase", Purchase.class).list();
        if (!existingPurchases.isEmpty()) {
            System.out.println("Purchases table is already populated.");
            return;
        }
        List<RegisteredUser> users = session.createQuery("from RegisteredUser", RegisteredUser.class).list();
        if (users.isEmpty()) {
            System.out.println("No registered users found.");
            return;
        }

        List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
        List<MovieInstance> movieInstances = session.createQuery("from MovieInstance", MovieInstance.class).list();
        List<Seat> seats = session.createQuery("from Seat", Seat.class).list();

        if (movies.isEmpty() || movieInstances.isEmpty() || seats.isEmpty()) {
            System.out.println("No movies, movie instances, or seats found.");
            return;
        }

        Transaction transaction = session.beginTransaction();

        try {
            for (int i = 0; i < 5; i++) {
                RegisteredUser user = users.get(i % users.size());

                HomeViewingPackageInstance homePackage = new HomeViewingPackageInstance(
                        LocalDateTime.now(),
                        user,
                        "validation" + i,
                        movies.get(i % movies.size()),
                        LocalDateTime.now().plusDays(1),
                        100 + i
                );
                session.save(homePackage);

                MovieTicket movieTicket = new MovieTicket(
                        LocalDateTime.now(),
                        user,
                        "validation" + (i + 5),
                        movieInstances.get(i % movieInstances.size()),
                        seats.get(i % seats.size())
                );
                session.save(movieTicket);

                // Add the movie instance to the taken list of the seat
                Seat seat = movieTicket.getSeat();
                seat.addMovieInstance(movieTicket.getMovieInstance());
                session.update(seat);


                MultiEntryTicket multiEntryTicket = new MultiEntryTicket(
                        LocalDateTime.now(),
                        user,
                        "validation" + (i + 10)
                );
                session.save(multiEntryTicket);

                session.flush();
            }

            transaction.commit();
            System.out.println("15 purchases (5 of each kind) have been created and associated with registered users.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("An error occurred while generating purchases, changes have been rolled back.");
            e.printStackTrace();
        }
    }

    private void generateComplaints() {
        List<Complaint> existingComplaints = session.createQuery("from Complaint ", Complaint.class).list();
        if (!existingComplaints.isEmpty()) {
            System.out.println("Complaints table is already populated.");
            return;
        }
        List<RegisteredUser> users = session.createQuery("from RegisteredUser", RegisteredUser.class).list();
        if (users.isEmpty()) {
            System.out.println("No registered users found.");
            return;
        }

        List<Purchase> purchases = session.createQuery("from Purchase", Purchase.class).list();
        if (purchases.isEmpty()) {
            System.out.println("No purchases found.");
            return;
        }

        Transaction transaction = session.beginTransaction();

        try {
            for (int i = 0; i < 10; i++) {
                Purchase purchase = purchases.get(i % purchases.size());
                RegisteredUser user = purchase.getOwner();  // Get the user who made the purchase

                Complaint complaint = new Complaint(
                        "Complaint info " + i,
                        LocalDateTime.now(),
                        purchase,
                        false,
                        user
                );
                session.save(complaint);
                session.flush();
                System.out.println("Saved complaint: " + complaint.getId());
            }

            transaction.commit();
            System.out.println("10 complaints have been created and associated with registered users and purchases.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("An error occurred while generating complaints, changes have been rolled back.");
            e.printStackTrace();
        }
    }



}
