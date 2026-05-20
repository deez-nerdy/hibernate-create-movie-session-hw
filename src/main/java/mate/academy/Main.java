package mate.academy;

import java.time.LocalDate;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static final LocalDate DATE_EXAMPLE = LocalDate.now();
    private static final int HALL_CAPACITY_EXAMPLE = 100;
    private static final String DESCRIPTION_EXAMPLE = "Cinema hall";

    public static void main(String[] args) {
        final MovieService movieService
                = (MovieService) injector.getInstance(MovieService.class);
        final MovieSessionService movieSessionService
                = (MovieSessionService) injector.getInstance(MovieSessionService.class);
        final CinemaHallService cinemaHallService
                = (CinemaHallService) injector.getInstance(CinemaHallService.class);

        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(HALL_CAPACITY_EXAMPLE);
        cinemaHall.setDescription(DESCRIPTION_EXAMPLE);
        cinemaHallService.add(cinemaHall);

        MovieSession fastAndFuriousSession = new MovieSession();
        fastAndFuriousSession.setMovie(fastAndFurious);
        fastAndFuriousSession.setShowTime(DATE_EXAMPLE);
        fastAndFuriousSession.setCinemaHall(cinemaHall);

        movieSessionService.add(fastAndFuriousSession);

        List<MovieSession> movieSessionList
                = movieSessionService
                .findAvailableSessions(fastAndFuriousSession.getId(), DATE_EXAMPLE);
        for (MovieSession movieSession : movieSessionList) {
            System.out.println(movieSession);
        }
    }
}
