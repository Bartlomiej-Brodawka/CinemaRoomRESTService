package cinema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter @Setter
public class CinemaRoom {
    int totalRows;
    int totalColumns;
    Collection<Seat> availableSeats;
}
