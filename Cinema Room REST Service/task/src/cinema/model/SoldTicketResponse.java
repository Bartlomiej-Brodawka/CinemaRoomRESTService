package cinema.model;

import lombok.Value;

@Value
public class SoldTicketResponse {
    String token;
    Seat ticket;
}
