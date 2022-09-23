package cinema.service;

import cinema.model.CinemaRoom;
import cinema.model.Seat;
import cinema.model.SoldTicketResponse;
import cinema.model.StatsResponse;

public interface CinemaRoomService {
    public CinemaRoom getAvailableSeats();

    SoldTicketResponse purchase(Seat seat);

    Seat returnTicket(String token);

    StatsResponse getStats();
}
