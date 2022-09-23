package cinema.repository;

import cinema.model.Seat;
import cinema.model.SoldTicketResponse;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SoldTicketsRepository {
    Map<String, Seat> soldTickets = new HashMap<String, Seat>();

    public void save(String token, Seat seat) {
        soldTickets.put(token, seat);
    }

    public Optional<Seat> findByToken(String token) {
        return Optional.ofNullable(soldTickets.remove(token));
    }

    public int totalIncome() {
        return soldTickets.values().stream().mapToInt(Seat::getPrice).sum();
    }

    public int count() {
        return soldTickets.size();
    }
}
