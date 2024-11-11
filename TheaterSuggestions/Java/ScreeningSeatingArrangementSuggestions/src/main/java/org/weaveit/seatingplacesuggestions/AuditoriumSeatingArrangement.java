package org.weaveit.seatingplacesuggestions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record AuditoriumSeatingArrangement (Map<String, Row> rows) {

    public SeatingOptionIsSuggested suggestSeatingOptionFor(int partyRequested, PricingCategory pricingCategory) {
        for (Row row : rows.values()) {
            var seatingOptionSuggested = row.suggestSeatingOption(partyRequested, pricingCategory);

            if (seatingOptionSuggested.matchExpectation()) {
                return seatingOptionSuggested;
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public AuditoriumSeatingArrangement allocate(SeatingPlace seatingPlace) {
        var row = rows.get(seatingPlace.rowName());

        if (row == null) {
            return this;
        }

        var allocated_row = row.allocate(seatingPlace);

        var allocated_rows = new HashMap<String, Row>();
        allocated_rows.putAll(rows);
        allocated_rows.put(seatingPlace.rowName(),allocated_row);

        return new AuditoriumSeatingArrangement(allocated_rows);
    }
}
