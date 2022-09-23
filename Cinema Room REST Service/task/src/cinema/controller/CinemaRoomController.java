package cinema.controller;

import cinema.config.CinemaRoomProperties;
import cinema.exception.AlreadySoldException;
import cinema.exception.OutOfBoundsException;
import cinema.exception.WrongPasswordException;
import cinema.model.*;
import cinema.service.CinemaRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.rmi.AlreadyBoundException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class CinemaRoomController {

    @Autowired
    CinemaRoomService cinemaRoomService;

    @Autowired
    CinemaRoomProperties cinemaRoomProperties;

    @GetMapping("/seats")
    CinemaRoom getAvailableSeats() {
        return cinemaRoomService.getAvailableSeats();
    }

    @PostMapping("/purchase")
    SoldTicketResponse purchase(@RequestBody Seat seat) {
        return cinemaRoomService.purchase(seat);
    }

    @PostMapping("/return")
    Map<String, Object> purchase(@RequestBody ReturnRequest returnRequest) {
        Seat seat = cinemaRoomService.returnTicket(returnRequest.getToken());
        return Map.of("returned_ticket", seat);
    }

    @PostMapping("/stats")
    StatsResponse stats(
            @RequestParam Optional<String> password
    ) {
        log.info("stats with password ={}", password);
        return password.filter(cinemaRoomProperties.getSecret()::equals)
                .map(p -> cinemaRoomService.getStats())
                .orElseThrow(WrongPasswordException::new);
    }
}
