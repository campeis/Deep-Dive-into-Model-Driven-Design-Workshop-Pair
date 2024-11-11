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

        for (var seat : seatingPlaces) {
            if (seat.isAvailable() && seat.matchCategory(pricingCategory)) {
                seatAllocation.addSeat(seat);

                if (seatAllocation.matchExpectation())
                    return seatAllocation;

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
