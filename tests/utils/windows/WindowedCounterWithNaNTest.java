package utils.windows;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowedCounterWithNaNTest extends WindowedCounterWithNaN {

    @Test
    void add() {
        WindowedCounterWithNaN win = new WindowedCounterWithNaN(3);
        assertEquals(0, win.getSize());
        assert Double.isNaN(win.getSum());
        assert Double.isNaN(win.getAverage());

        win.add(1.0);
        assertEquals(1, win.getSize());
        assertEquals(1.0, win.getSum());
        assertEquals(1.0, win.getAverage());

        win.add(Double.NaN);
        assertEquals(1, win.getSize());
        assertEquals(1.0, win.getSum());
        assertEquals(1.0, win.getAverage());

        win.add(0.0);
        assertEquals(2, win.getSize());
        assertEquals(1.0, win.getSum());
        assertEquals(0.5, win.getAverage());

        win.add(1.0);
        win.add(1.0);
        assertEquals(3, win.getSize());
        assertEquals(2.0, win.getSum());
        assertEquals(0.66, win.getAverage(), 0.01);

        win.add(Double.NaN);
        win.add(Double.NaN);
        win.add(Double.NaN);
        assertEquals(0, win.getSize());
        assert Double.isNaN(win.getSum());
        assert Double.isNaN(win.getAverage());

        win.add(1.0);
        win.add(1.0);
        win.add(1.0);
        win.add(Double.NaN);
        assertEquals(2, win.getSize());
        assertEquals(2.0, win.getSum());
        assertEquals(1.0, win.getAverage(), 0.01);

    }
}
