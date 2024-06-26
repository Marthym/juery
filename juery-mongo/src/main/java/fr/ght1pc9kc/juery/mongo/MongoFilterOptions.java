package fr.ght1pc9kc.juery.mongo;

import lombok.experimental.UtilityClass;

/**
 * <p>Options allowed to the {@link fr.ght1pc9kc.juery.mongo.filter.MongoFilterVisitor}</p>
 *
 * <ul>
 *     <li><strong>DEFAULT_OPTIONS</strong>: Cover the most frequent usage, {@link MongoFilterOptions#USE_DEFAULT_VALUES}</li>
 *     <li><strong>USE_DEFAULT_VALUES</strong>: If the property name is not found un the mapping, use it as mongo field name</li>
 * </ul>
 */
@UtilityClass
public class MongoFilterOptions {
    public static final int NONE = 0;
    public static final int DEFAULT_OPTIONS = 1;
    public static final int USE_DEFAULT_VALUES = 1;
}
