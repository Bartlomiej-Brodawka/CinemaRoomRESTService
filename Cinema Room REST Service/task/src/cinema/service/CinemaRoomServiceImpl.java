package cinema.service;

import cinema.config.CinemaRoomProperties;
import cinema.exception.AlreadySoldException;
import cinema.exception.OutOfBoundsException;
import cinema.exception.WrongTokenException;
import cinema.model.CinemaRoom;
import cinema.model.Seat;
import cinema.model.SoldTicketResponse;
import cinema.model.StatsResponse;
import cinema.repository.CinemaRoomRepository;
import cinema.repository.SoldTicketsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CinemaRoomServiceImpl implements CinemaRoomService {

    CinemaRoomRepository cinemaRoomRepository;
    CinemaRoomProperties cinemaRoomProperties;
    SoldTicketsRepository soldTicketsRepository;

    @Override
    public CinemaRoom getAvailableSeats() {
        var res = new CinemaRoom();
        res.setTotalRows(cinemaRoomProperties.getTotalRows());
        res.setTotalColumns(cinemaRoomProperties.getTotalColumns());
        var seats = cinemaRoomRepository.getAvailableSeats();
        seats.forEach(this::setPrice);
        res.setAvailableSeats(cinemaRoomRepository.getAvailableSeats());
        return res;
    }

    private void setPrice(Seat seat) {
        seat.setPrice(calcPrice(seat));
    }

    private int calcPrice(Seat seat) {
        return seat.getRow() <= cinemaRoomProperties.getFirstRows()
            ? cinemaRoomProperties.getFirstRowsPrice()
            : cinemaRoomProperties.getLastRowsPrice();
    }

    @Override
    public SoldTicketResponse purchase(Seat seat) {
        checkIfSeatIsCorrect(seat);
        setPrice(seat);
        String token = UUID.randomUUID().toString();
        var res = new SoldTicketResponse(token, seat);
        soldTicketsRepository.save(token, seat);
        return res;
    }

    private void checkIfSeatIsCorrect(Seat seat) {
        if (!(1 <= seat.getRow() &&
                seat.getRow() <= cinemaRoomProperties.getTotalRows() &&
                1 <= seat.getColumn() &&
                seat.getColumn() <= cinemaRoomProperties.getTotalColumns())
        ) {
            throw new OutOfBoundsException();
        }
        if (!cinemaRoomRepository.delete(seat)) {
            throw new AlreadySoldException();
        }
    }

    @Override
    public Seat returnTicket(String token) {
        return soldTicketsRepository.findByToken(token)
                .map(seat -> cinemaRoomRepository.addSeat(seat))
                .orElseThrow(() -> new WrongTokenException());
    }

    @Override
    public StatsResponse getStats() {
        return StatsResponse.builder()
                .currentIncome(soldTicketsRepository.totalIncome())
                .numberOfAvailableSeats(cinemaRoomRepository.count())
                .numberOfPurchasedTickets(soldTicketsRepository.count())
                .build();
    }
}
