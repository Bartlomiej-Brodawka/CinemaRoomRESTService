package cinema.repository;

import cinema.config.CinemaRoomProperties;
import cinema.model.Seat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Repository
public class CinemaRoomRepository {
    @Autowired
    CinemaRoomProperties cinemaRoomProperties;

    Set<Seat> availableSeats;

    @PostConstruct
    void initAvailableSeats() {
        availableSeats = new LinkedHashSet<>();
        for(int i = 1; i <= cinemaRoomProperties.getTotalRows(); i++) {
            for (int j = 1; j <= cinemaRoomProperties.getTotalColumns(); j++) {
                availableSeats.add(new Seat(i, j, 0));
            }
        }
    }

    public Seat addSeat(Seat seat) {
        availableSeats.add(seat);
        return seat;
    }

    public boolean delete(Seat seat) {
        return availableSeats.remove(seat);
    }

    public int count() {
        return availableSeats.size();
    }
}
