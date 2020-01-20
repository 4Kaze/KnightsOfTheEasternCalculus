package lambda.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static String capitalize(final String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
