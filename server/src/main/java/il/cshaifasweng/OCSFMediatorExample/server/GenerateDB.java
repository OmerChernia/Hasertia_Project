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
                    new RegisteredUser("318111222","Alice Smith", false, "alice@example.com", 0),
                    new RegisteredUser("284619537","Bob Johnson", false, "bob@example.com", 0),
                    new RegisteredUser("719305846","Charlie Brown", false, "charlie@example.com", 0),
                    new RegisteredUser("562890143","David Wilson", false, "david@example.com", 0),
                    new RegisteredUser("938472615","Eva Davis", false, "eva@example.com", 0),
                    new RegisteredUser("451298763","Frank Harris", false, "frank@example.com", 0),
                    new RegisteredUser("895631247","Grace Lee", false, "grace@example.com", 0),
                    new RegisteredUser("603815927","Henry King", false, "henry@example.com", 1),
                    new RegisteredUser("748596321","Ivy Martin", false, "ivy@example.com", 8),
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
                    new Employee("705182943","John Doe", false, "password10", Employee.EmployeeType.COMPANY_MANAGER),
                    new Employee("238947615","Jane Smith", false, "password120", Employee.EmployeeType.CUSTOMER_SERVICE),
                    new Employee("864205739","Michael Johnson", false, "password1230", Employee.EmployeeType.CONTENT_MANAGER)
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
                    new TheaterManager("847365912","Josh Bee", false, "password1", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("512089637","Mili Tik", false, "password12", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("396824571","Bob Back", false, "password123", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("483944123","Emily Davis", false, "password1234", Employee.EmployeeType.THEATER_MANAGER, null),
                    new TheaterManager("591736842","Daniel Brown", false, "password1235", Employee.EmployeeType.THEATER_MANAGER, null)
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
                    new Movie( "קפטן אמריקה", "Steve Rogers, a rejected military soldier transforms into Captain America after taking a dose of a 'Super-Soldier serum'.", "Marvel Studios", "Captain America", "Chris Evans_Sebastian Stan_Hayley Atwell", "captainamerica.jpg", Movie.StreamingType.THEATER_VIEWING, 124, 50, 60, "action",Movie.Availability.AVAILABLE),
                    new Movie( "צ'לנג'רס", "A drama centered around three friends, their ambitions, and the competitive tennis circuit.", "MGM", "Challengers", "Zendaya_Mike Faist_Josh O'Connor", "challengers.jpg", Movie.StreamingType.THEATER_VIEWING, 120, 50, 60, "drama",Movie.Availability.AVAILABLE),
                    new Movie( "דדפול וולברין", "Deadpool teams up with Wolverine for a new mission that involves time travel.", "Marvel Studios", "Deadpool & Wolverine", "Ryan Reynolds_Hugh Jackman_Morena Baccarin", "deadpool-wolverine.jpg", Movie.StreamingType.BOTH, 115, 50, 60, "action",Movie.Availability.AVAILABLE),
                    new Movie( "גלדיאטור 2", "The story continues with Maximus's son seeking revenge against the Roman Empire.", "Universal Pictures", "Gladiator II", "Russell Crowe_Joaquin Phoenix_Connie Nielsen", "gladiator_ii.jpg", Movie.StreamingType.BOTH, 155, 50, 60, "action",Movie.Availability.AVAILABLE),
                    new Movie( "הקול בראש 2", "The emotions inside Riley's mind return for a new adventure.", "Pixar Animation Studios", "Inside Out 2", "Amy Poehler_Bill Hader_Lewis Black", "inside_out_two.jpg", Movie.StreamingType.BOTH, 95, 50, 60, "animation",Movie.Availability.AVAILABLE),
                    new Movie( "לה לה לנד", "A jazz musician and an aspiring actress fall in love while pursuing their dreams in Los Angeles.", "Lionsgate", "La La Land", "Ryan Gosling_Emma Stone_John Legend", "LaLaLand.jpg", Movie.StreamingType.BOTH, 128, 50, 60, "musical",Movie.Availability.AVAILABLE),
                    new Movie( "מופאסה: מלך האריות", "A prequel to The Lion King focusing on Mufasa's journey to become king.", "Walt Disney Pictures", "Mufasa: The Lion King", "James Earl Jones_Donald Glover_Beyoncé", "mufasa-the-lion-king.jpg", Movie.StreamingType.HOME_VIEWING, 120, 50, 60, "animation",Movie.Availability.AVAILABLE),
                    new Movie( "אין רגשות קשים", "A comedy about a couple trying to balance their careers and relationship.", "Columbia Pictures", "No Hard Feelings", "Jennifer Lawrence_Andrew Barth Feldman_Laura Benanti", "no-hard-feelings.jpg", Movie.StreamingType.BOTH, 103, 50, 60, "comedy",Movie.Availability.AVAILABLE),
                    new Movie( "אופנהיימר", "A drama about J. Robert Oppenheimer, the father of the atomic bomb.", "Universal Pictures", "Oppenheimer", "Cillian Murphy_Emily Blunt_Robert Downey Jr.", "oppenheimer.jpg", Movie.StreamingType.BOTH, 180, 50, 60, "drama",Movie.Availability.AVAILABLE),
                    new Movie( "פינוקיו", "A live-action adaptation of the classic tale of a wooden puppet who wants to become a real boy.", "Walt Disney Pictures", "Pinocchio", "Tom Hanks_Benjamin Evan Ainsworth_Joseph Gordon-Levitt", "pinocchio.jpg", Movie.StreamingType.HOME_VIEWING, 105, 50, 60, "fantasy",Movie.Availability.AVAILABLE),
                    new Movie( "סמייל 2", "A sequel to the horror film 'Smile', where the curse continues to haunt new victims.", "Paramount Pictures", "Smile 2", "Sosie Bacon_Kyle Gallner_Caitlin Stasey", "smile_two.jpg", Movie.StreamingType.BOTH, 115, 50, 60, "horror",Movie.Availability.AVAILABLE),
                    new Movie( "מלחמת הכוכבים: פרק 1 - אימת הפאנטום", "The origin story of Anakin Skywalker and the rise of the Sith.", "Lucasfilm", "Star Wars: Episode I - The Phantom Menace", "Liam Neeson_Ewan McGregor_Natalie Portman", "Star_Wars_Episode_1.jpg", Movie.StreamingType.THEATER_VIEWING, 136, 50, 60, "sci-fi",Movie.Availability.AVAILABLE),
                    new Movie( "רובוטריקים", "The story of the war between Autobots and Decepticons on Earth.", "Paramount Pictures", "Transformers","Shia LaBeouf_Megan Fox_Josh Duhamel", "transformers_one.jpg", Movie.StreamingType.BOTH, 144, 50, 60, "action",Movie.Availability.AVAILABLE),
                    new Movie( "מרושעת", "A prequel to The Wizard of Oz, focusing on the story of the Wicked Witch of the West.", "Universal Pictures", "Wicked", "Idina Menzel_Kristin Chenoweth_Ariana Grande", "wicked.jpg", Movie.StreamingType.BOTH, 130, 50, 60, "fantasy",Movie.Availability.AVAILABLE),
                    new Movie( "מלך האריות", "After the murder of his father, a young lion prince flees his kingdom only to learn the true meaning of responsibility and bravery.", "Walt Disney Pictures", "The Lion King", "Donald Glover_Beyoncé_Seth Rogen", "TheLionKing.jpg", Movie.StreamingType.BOTH, 118, 50, 60, "animation",Movie.Availability.AVAILABLE),
                    new Movie( "ברבי", "To live in Barbie Land is to be a perfect being in a perfect place. Unless you have a full-on existential crisis. Or you’re a Ken.", "Warner Bros.", "Barbie", "Margot Robbie_Ryan Gosling_Simu Liu", "Barbie.jpg", Movie.StreamingType.THEATER_VIEWING, 114, 50, 60, "comedy",Movie.Availability.AVAILABLE),
                    new Movie( "סיפור צעצוע 4", "The adventures of Woody, Buzz Lightyear, and the gang as they encounter new toys.", "Pixar Animation Studios", "Toy Story 4", "Tom Hanks_Tim Allen_Annie Potts", "toy_story4.jpg", Movie.StreamingType.BOTH, 100, 50, 60, "animation",Movie.Availability.AVAILABLE),
                    new Movie( "וונדר וומן ", "Diana, princess of the Amazons, discovers her full powers and true destiny as Wonder Woman.", "Warner Bros.", "Wonder Woman", "Gal Gadot_Chris Pine_Robin Wright", "wonder_woman2017.jpg", Movie.StreamingType.BOTH, 141, 50, 60, "action",Movie.Availability.AVAILABLE),
                    new Movie( "המיניונים: עלייתו של גרו", "The untold story of one twelve-year-old's dream to become the world's greatest supervillain.", "Universal Pictures", "Minions: The Rise of Gru", "Steve Carell_Pierre Coffin_Taraji P. Henson", "minions_the_rise_of_gru.jpg", Movie.StreamingType.BOTH, 90, 50, 60, "animation",Movie.Availability.AVAILABLE),
                    new Movie( "הג'וקר 2", "A dark origin story about the Joker's transformation from struggling comedian to a criminal mastermind.", "Warner Bros.", "The Joker 2", "Joaquin Phoenix_Robert De Niro_Lady Gaga", "the_joker2024.jpg", Movie.StreamingType.BOTH, 122, 50, 60, "drama",Movie.Availability.COMING_SOON)
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

            // Set the time to include only up to minutes, with seconds and nanoseconds set to 0
            LocalDateTime startDate = LocalDateTime.of(2024, 8, 18, 12, 0);
            //.plusHours(3)
            movieInstances = List.of(
                    new MovieInstance(movies.get(0), startDate.plusDays(0).plusHours(0), halls.get(0)),
                    new MovieInstance(movies.get(1), startDate.plusDays(1).plusHours(2), halls.get(1)),
                    new MovieInstance(movies.get(2), startDate.plusDays(2).plusHours(4), halls.get(2)),
                    new MovieInstance(movies.get(3), startDate.plusDays(3).plusHours(2), halls.get(3)),
                    new MovieInstance(movies.get(4), startDate.plusDays(4).plusHours(1), halls.get(4)),
                    new MovieInstance(movies.get(5), startDate.plusDays(5).plusHours(6), halls.get(5)),
                    new MovieInstance(movies.get(6), startDate.plusDays(6).plusHours(8), halls.get(6)),
                    new MovieInstance(movies.get(7), startDate.plusDays(7).plusHours(10), halls.get(7)),
                    new MovieInstance(movies.get(8), startDate.plusDays(8).plusHours(4), halls.get(8)),
                    new MovieInstance(movies.get(9), startDate.plusDays(9).plusHours(10), halls.get(9)),
                    new MovieInstance(movies.get(0), startDate.plusDays(2).plusHours(8), halls.get(0)),
                    new MovieInstance(movies.get(0), startDate.plusDays(3).plusHours(6), halls.get(8))
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
            for (int i = 0; i < 150; i++) {
                RegisteredUser user = users.get(i % users.size());

                // Generate a different date and time for each purchase
                LocalDateTime purchaseTime = LocalDateTime.of(2024, 6 + (i % 3), 1 + (i % 30), 0, 0);

                if (i < 40) {
                    HomeViewingPackageInstance homePackage = new HomeViewingPackageInstance(
                            purchaseTime,
                            user,
                            "validation" + i,
                            movies.get(i % movies.size()),
                            LocalDateTime.now().plusDays(1)
                    );
                    session.save(homePackage);
                } else if (i < 60) {
                    MultiEntryTicket multiEntryTicket = new MultiEntryTicket(
                            purchaseTime,
                            user,
                            "validation" + i
                    );
                    session.save(multiEntryTicket);
                } else {
                    MovieTicket movieTicket = new MovieTicket(
                            purchaseTime,
                            user,
                            "validation" + i,
                            movieInstances.get(i % movieInstances.size()),
                            seats.get(i % seats.size())
                    );
                    session.save(movieTicket);

                    // Add the movie instance to the taken list of the seat
                    Seat seat = movieTicket.getSeat();
                    seat.addMovieInstanceId(movieTicket.getMovieInstance());
                    session.update(seat);
                }

                session.flush();
            }

            transaction.commit();
            System.out.println("150 purchases (40 HomeViewingPackageInstances, 20 MultiEntryTickets, 90 MovieTickets) have been created and associated with registered users.");
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

        List<MovieTicket> movieTickets = session.createQuery("from MovieTicket", MovieTicket.class).list();
        List<HomeViewingPackageInstance> homePackages = session.createQuery("from HomeViewingPackageInstance", HomeViewingPackageInstance.class).list();
        List<MultiEntryTicket> multiEntryTickets = session.createQuery("from MultiEntryTicket", MultiEntryTicket.class).list();

        if (movieTickets.isEmpty() || homePackages.isEmpty() || multiEntryTickets.isEmpty()) {
            System.out.println("No movie tickets, home viewing packages, or multi-entry tickets found.");
            return;
        }

        Transaction transaction = session.beginTransaction();

        try {
            for (int i = 0; i < 30; i++) { // Increase the limit here
                RegisteredUser user = users.get(i % users.size());
                Purchase purchase;

                if (i < 10) {
                    purchase = movieTickets.get(i);
                } else if (i < 20) {
                    purchase = homePackages.get(i - 10);
                } else {
                    purchase = multiEntryTickets.get(i - 20);
                }

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
            System.out.println("30 complaints have been created and associated with registered users and purchases.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("An error occurred while generating complaints, changes have been rolled back.");
            e.printStackTrace();
        }
    }


}
