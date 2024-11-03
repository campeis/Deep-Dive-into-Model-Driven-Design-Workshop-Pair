package org.weaveit.seatingplacesuggestions;

import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumDto;
import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import org.weaveit.externaldependencies.auditoriumlayoutrepository.SeatDto;
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider;
import org.weaveit.externaldependencies.reservationsprovider.ReservedSeatsDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditoriumSeatings {

    private final ReservationsProvider reservedSeatsRepository;
    private final AuditoriumLayoutRepository auditoriumLayoutRepository;


    public AuditoriumSeatings(AuditoriumLayoutRepository auditoriumLayoutRepository, ReservationsProvider reservationsProvider) {
        this.auditoriumLayoutRepository = auditoriumLayoutRepository;
        this.reservedSeatsRepository = reservationsProvider;
    }

    public AuditoriumSeating findByShowId(String showId) {
        return adapt(auditoriumLayoutRepository.findByShowId(showId),
                reservedSeatsRepository.getReservedSeats(showId));

    }

    private AuditoriumSeating adapt(AuditoriumDto auditoriumDto, ReservedSeatsDto reservedSeatsDto) {

        var rows = new HashMap<String, Row>();


        for (Map.Entry<String, List<SeatDto>> rowDto : auditoriumDto.rows().entrySet()) {
            List<SeatingPlace> seats = new ArrayList<>();

            rowDto.getValue().forEach(seatDto -> {
                var rowName = rowDto.getKey();
                var number = extractNumber(seatDto.name());
                var pricingCategory = convertCategory(seatDto.category());

                var isReserved = reservedSeatsDto.reservedSeats().contains(seatDto.name());

                seats.add(new SeatingPlace(rowName, number, pricingCategory, isReserved ? SeatingPlaceAvailability.RESERVED : SeatingPlaceAvailability.AVAILABLE));
            });

            rows.put(rowDto.getKey(), new Row(rowDto.getKey(), seats));
        }

        return new AuditoriumSeating(rows);
    }

    private static PricingCategory convertCategory(int seatDtoCategory) {
        return PricingCategory.valueOf(seatDtoCategory);
    }

    private static int extractNumber(String name) {
        return Integer.parseUnsignedInt(name.substring(1));
    }

}
