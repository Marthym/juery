package fr.ght1pc9kc.juery.mongo;

import com.mongodb.client.model.Sorts;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import lombok.experimental.UtilityClass;
import org.bson.conversions.Bson;

@UtilityClass
public class MongoPagination {
    public static Bson toSortBson(Pagination page) {
        return Sorts.orderBy(
                page.sort().orders().stream()
                        .map(sorting -> {
                            if (sorting.direction().equals(Direction.DESC)) {
                                return Sorts.descending(sorting.property());
                            } else {
                                return Sorts.ascending(sorting.property());
                            }
                        }).toList());
    }
}
