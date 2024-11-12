package org.weaveit.seatssuggestionsacceptancetests;

import org.junit.jupiter.api.Test;
import org.weaveit.seatingplacesuggestions.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SeatingPlaceTest {

    @Test
    void seating_place_is_immutable(){
        var seating_place1= new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        var seating_place2= new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

        assertThat(seating_place1).isEqualTo(seating_place2);

        seating_place1.allocate();

        assertThat(seating_place1).isEqualTo(seating_place2);
    }

    @Test
    void seating_place_can_be_allovated(){
        var seating_place= new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);


        var allocated_seating_place = seating_place.allocate();
        assertThat(allocated_seating_place.seatingPlaceAvailability()).isEqualTo(SeatingPlaceAvailability.ALLOCATED);

    }

    @Test
    void row_is_immutable(){
        var row1= new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)));
        var row2= new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)));

        assertThat(row1).isEqualTo(row2);

        var place_to_allocate= new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        var allocated_row_to_find= new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.ALLOCATED)));

        var allocated_arrangement = row1.allocate(place_to_allocate);

        assertThat(row1).isEqualTo(row2);
        assertThat(allocated_arrangement).isEqualTo(allocated_row_to_find);
    }

    @Test
    void arrangement_is_immutable(){
        var arrangement1= new AuditoriumSeatingArrangement(Map.of("A", new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)))));
        var arrangement2= new AuditoriumSeatingArrangement(Map.of("A", new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE)))));

        assertThat(arrangement1).isEqualTo(arrangement2);

        var place_to_allocate= new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);

        var allocated_arrangement_to_have= new AuditoriumSeatingArrangement(Map.of("A", new Row("A", List.of(new SeatingPlace("A", 1,PricingCategory.FIRST, SeatingPlaceAvailability.ALLOCATED)))));

        var allocated_arrangement = arrangement1.allocate(place_to_allocate);

        assertThat(arrangement1).isEqualTo(arrangement2);
        assertThat(allocated_arrangement).isEqualTo(allocated_arrangement_to_have);

    }

}
