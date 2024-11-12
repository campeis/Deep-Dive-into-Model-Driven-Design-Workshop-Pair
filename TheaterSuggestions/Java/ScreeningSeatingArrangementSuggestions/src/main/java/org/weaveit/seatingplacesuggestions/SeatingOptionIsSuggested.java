package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeatingOptionIsSuggested {

    private final PricingCategory pricingCategory;
    private final List<SeatingPlace> seats = new ArrayList<>();
    private final int partyRequested;

    public SeatingOptionIsSuggested(int partyRequested, PricingCategory pricingCategory) {
        this.pricingCategory = pricingCategory;
        this.partyRequested = partyRequested;
    }

    public void addSeat(SeatingPlace seat) {
        seats.add(seat);
    }

    public boolean matchExpectation() {
        return seats.size() == partyRequested;
    }

    public List<SeatingPlace> seats() {
        return seats.stream().sorted(Comparator.comparingInt(SeatingPlace::number)).toList();
    }

    public PricingCategory pricingCategory() { return pricingCategory; }

    public int partyRequested() { return partyRequested; }
}
