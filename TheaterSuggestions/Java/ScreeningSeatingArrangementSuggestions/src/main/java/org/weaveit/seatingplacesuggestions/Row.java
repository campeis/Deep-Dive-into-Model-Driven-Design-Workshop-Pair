package org.weaveit.seatingplacesuggestions;

import java.util.LinkedList;
import java.util.List;

public record Row (String name, List<SeatingPlace> seatingPlaces){

    public Row(String name, List<SeatingPlace> seatingPlaces) {
        this.name = name;
        this.seatingPlaces = seatingPlaces;
    }

    public SeatingOptionIsSuggested suggestSeatingOption(int partyRequested, PricingCategory pricingCategory) {

        var seatAllocation = new SeatingOptionIsSuggested(partyRequested, pricingCategory);

        float center = ((float) this.seatingPlaces.size())  / 2 + 0.1f;
        var prioritized = seatingPlaces.stream()
            .sorted((a, b) -> Float.compare(Math.abs(center - a.number()), Math.abs(center- b.number()) )).toList();
        var most_center_one = prioritized.get(0);
        var available =  prioritized.stream().filter(place -> place.seatingPlaceAvailability() == SeatingPlaceAvailability.AVAILABLE)
                .filter(place -> place.matchCategory(pricingCategory)).toList();
        if (most_center_one.seatingPlaceAvailability() == SeatingPlaceAvailability.AVAILABLE && most_center_one.matchCategory(pricingCategory)) {
            for (var seat : available.stream().limit(partyRequested).toList()) {
                seatAllocation.addSeat(seat);

                if (seatAllocation.matchExpectation())
                    return seatAllocation;
            }
        } else {
            if(!available.isEmpty()) {
                var chosen = available.get(0);
                for (var seat : available.stream().filter( a -> Math.signum(a.number() - center) == Math.signum(chosen.number() - center)).limit(partyRequested).toList()) {
                    seatAllocation.addSeat(seat);

                    if (seatAllocation.matchExpectation())
                        return seatAllocation;
                }
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public Row allocate(SeatingPlace seatingPlace) {
        var seating_place_to_allocate = seatingPlaces.stream().filter(seatingPlaceLoop -> seatingPlaceLoop.number() == seatingPlace.number()).findFirst();
        if (seating_place_to_allocate.isEmpty()){
            return this;
        }
        var allocated_seating_place = seating_place_to_allocate.get().allocate();


        var allocated_places = this.seatingPlaces.stream().map(place-> place.number() == seatingPlace.number() ? allocated_seating_place : place).toList();;
        return new Row(this.name(), allocated_places);
    }
}
