package org.weaveit.seatingplacesuggestions;

public record SeatingPlace(String rowName, int number, PricingCategory pricingCategory,SeatingPlaceAvailability seatingPlaceAvailability) {

    public boolean isAvailable() {
        return seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE;
    }

    public boolean matchCategory(PricingCategory pricingCategory) {
        return (pricingCategory == PricingCategory.MIXED) ? true : this.pricingCategory == pricingCategory;
    }

    public SeatingPlace allocate() {
        if (seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE)
            return new SeatingPlace(this.rowName, this.number, this.pricingCategory, SeatingPlaceAvailability.ALLOCATED);
        return this;
    }

    @Override
    public String toString() {
        return rowName + number;
    }
}
