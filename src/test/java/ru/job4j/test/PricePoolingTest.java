package ru.job4j.test;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test.
 *
 * @author Yury Doronin (doronin.ltd@gmail.com)
 * @version 2.0
 * @since 27.02.2020
 */
public class PricePoolingTest {

    /**
     * A formatter.
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * A collection with current prices.
     */
    private List<Price> currentPrice;

    /**
     * A collection with new prices.
     */
    private List<Price> newPrice;

    /**
     * A collection with united prices and prices' time of action.
     */
    private List<Price> united;

    @Before
    public void beforeTest() {
        currentPrice = List.of(
                new Price(1, "122856", 1, 1,
                        LocalDate.parse("01-01-2013", formatter),
                        LocalDate.parse("31-01-2013", formatter), 11),
                new Price(2, "122856", 2, 1,
                        LocalDate.parse("10-01-2013", formatter),
                        LocalDate.parse("20-01-2013", formatter), 99),
                new Price(3, "6654", 1, 2,
                        LocalDate.parse("01-01-2013", formatter),
                        LocalDate.parse("31-01-2013", formatter), 5));

        newPrice = List.of(
                new Price(1, "122856", 1, 1,
                        LocalDate.parse("20-01-2013", formatter),
                        LocalDate.parse("20-02-2013", formatter), 11),
                new Price(2, "122856", 2, 1,
                        LocalDate.parse("15-01-2013", formatter),
                        LocalDate.parse("25-01-2013", formatter), 92),
                new Price(3, "6654", 1, 2,
                        LocalDate.parse("12-01-2013", formatter),
                        LocalDate.parse("13-01-2013", formatter), 4));

        united = List.of(
                new Price(1, "122856", 1, 1,
                        LocalDate.parse("01-01-2013", formatter),
                        LocalDate.parse("20-02-2013", formatter), 11),
                new Price(2, "122856", 2, 1,
                        LocalDate.parse("10-01-2013", formatter),
                        LocalDate.parse("15-01-2013", formatter), 99),
                new Price(3, "122856", 2, 1,
                        LocalDate.parse("15-01-2013", formatter),
                        LocalDate.parse("25-01-2013", formatter), 92),
                new Price(4, "6654", 1, 2,
                        LocalDate.parse("01-01-2013", formatter),
                        LocalDate.parse("12-01-2013", formatter), 5),
                new Price(5, "6654", 1, 2,
                        LocalDate.parse("12-01-2013", formatter),
                        LocalDate.parse("13-01-2013", formatter), 4),
                new Price(6, "6654", 1, 2,
                        LocalDate.parse("13-01-2013", formatter),
                        LocalDate.parse("31-01-2013", formatter), 5));
    }

    private boolean compare(List<Price> united, List<Price> result) {
        boolean rst = true;
        if (united.size() != result.size()) {
            rst = false;
        }
        for (int index = 0; index < united.size(); index++) {
            if (united.get(index) == result.get(index) && united.get(index).getId() != result.get(index).getId()) {
                rst = false;
                break;
            }
        }
        return rst;
    }

    @Test
    public void whenTwoCollectionsThenUniteIntoOne() {
        List<Price> result = new PricePooling().unite(currentPrice, newPrice);
        assertThat(this.compare(united, result), is(true));
        united.forEach(System.out::print);
        System.out.println("");
        result.forEach(System.out::print);
    }
}