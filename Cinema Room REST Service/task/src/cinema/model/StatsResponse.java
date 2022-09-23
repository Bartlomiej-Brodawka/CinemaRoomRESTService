package cinema.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatsResponse {
    int currentIncome;
    int numberOfAvailableSeats;
    int numberOfPurchasedTickets;
}
