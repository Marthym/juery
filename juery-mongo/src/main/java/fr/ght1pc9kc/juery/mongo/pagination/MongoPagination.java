package fr.ght1pc9kc.juery.mongo.pagination;

import com.mongodb.client.model.Sorts;
import fr.ght1pc9kc.juery.api.Pagination;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import lombok.experimental.UtilityClass;
import org.bson.conversions.Bson;

import java.util.Map;

@UtilityClass
public class MongoPagination {
    public static Bson toSortBson(Pagination page, Map<String, String> propertiesMapping) {
        return Sorts.orderBy(
                page.sort().orders().stream()
                        .map(sorting -> {
                            if (sorting.direction().equals(Direction.DESC)) {
                                return Sorts.descending(propertiesMapping.getOrDefault(sorting.property(), sorting.property()));
                            } else {
                                return Sorts.ascending(propertiesMapping.getOrDefault(sorting.property(), sorting.property()));
                            }
                        }).toList());
    }
}
