package org.weaveit.seatingplacesuggestions;

import java.util.List;

public record AdjacentSeats (Row row, int size, int firstPlace) implements Comparable<AdjacentSeats> {

    public float distanceFromTheCenter () {
        float   firstSeatDistance = this.seats().get(0).distanceFromCenterOfTheRow(row.center());
        float   lastSeatDistance = this.seats().get(size - 1).distanceFromCenterOfTheRow(row.center());
        return (firstSeatDistance + lastSeatDistance) / 2;
    }
    
    public boolean available() {
        return seats().stream().allMatch(s -> s.isAvailable());
    }

//    public PricingCategory pricingCategory() {
//        return PricingCategory.FIRST;   //  TBD
//    }

    public boolean matchesPricingCategory (PricingCategory pricingCategory) {
        return seats().stream().allMatch(s -> s.matchCategory(pricingCategory));
    }
 
    public List<SeatingPlace> seats () {
        return row.seatingPlaces().stream().skip(firstPlace).limit(size).toList();
    }

    @Override
    public int compareTo(AdjacentSeats other) {
        return Float.compare(this.distanceFromTheCenter(), other.distanceFromTheCenter());
    }
    
}
