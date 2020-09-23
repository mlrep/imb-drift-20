package strategies.divers;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

class BaggingStrategyTest {

    @Test
    void update() {
        BaggingStrategy bs = (new BaggingStrategy()).setMaxLambda(1.0);

        bs.update(null, null, new HashMap<>(Map.ofEntries(entry("error", 1.0))));
        assertEquals(0.0, bs.lambda);

        bs.update(null, null, new HashMap<>(Map.ofEntries(entry("error", 0.0))));
        assertEquals(1.0, bs.lambda);

        bs.update(null, null, new HashMap<>(Map.ofEntries(entry("error", 0.5))));
        assertEquals(0.5, bs.lambda, 0.1);
    }
}