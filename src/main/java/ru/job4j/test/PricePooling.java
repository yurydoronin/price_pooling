package ru.job4j.test;

import java.time.LocalDate;
import java.util.*;

/**
 * Class PricePooling.
 *
 * @author Yury Doronin (doronin.ltd@gmail.com)
 * @version 3.0
 * @since 27.02.2020
 */
public class PricePooling {

    /**
     * Checks up if there are time crossing between current and new prices.
     *
     * @param current, a collection with current prices.
     * @param is,      a collection with new prices.
     * @return true if there are no time crossing, otherwise false.
     */

    private int range(Price current, List<Price> is) {
        int result = 0;
        for (Price newPrice : is) {
            if (current.getProductCode().equals(newPrice.getProductCode())) {
                if (newPrice.getBegin().isAfter(current.getBegin())
                        && newPrice.getEndTime().isBefore(current.getEndTime())) {
                    result = 1;
                    break;
                } else if (newPrice.getBegin().isAfter(current.getBegin())
                        && newPrice.getEndTime().isAfter(current.getEndTime())) {
                    result = 2;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Unites two collections with prices into one, updating data in DB.
     *
     * @param was, a collection with current prices.
     * @param is,  a collection with new prices.
     * @return a collection with updated data.
     */
    public List<Price> unite(List<Price> was, List<Price> is) {
        int count = 1;
        List<Price> result = new ArrayList<>();
        for (Price current : was) {
            int condition = this.range(current, is);
            if (current.getValue() == 0 && condition != 1) {
                for (Price newPrice : is) {
                    if (current.getProductCode().equals(newPrice.getProductCode())) {
                        current.setValue(newPrice.getValue());
                        current.setId(count++);
                        result.add(current);
                        break;
                    }
                }
            }
            if (current.getValue() != 0 && condition == 1) {
                for (Price newPrice : is) {
                    LocalDate save = current.getEndTime();
                    if (current.getProductCode().equals(newPrice.getProductCode())) {
                        count = this.init(count, result, current, newPrice);

                        current.setBegin(newPrice.getEndTime());
                        current.setEndTime(save);
                        current.setId(count++);
                        result.add(current);
                        break;
                    }
                }
            } else {
                for (Price newPrice : is) {
                    if (current.getValue() != newPrice.getValue() && current.getNumber() == newPrice.getNumber()) {
                        count = this.init(count, result, current, newPrice);
                        break;
                    } else if (current.getNumber() == newPrice.getNumber()) {
                        current.setEndTime(newPrice.getBegin());
                        current.setId(count++);
                        result.add(current);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private int init(int count, List<Price> result, Price current, Price newPrice) {
        current.setEndTime(newPrice.getBegin());
        current.setId(count++);
        result.add(current);
        newPrice.setId(count++);
        result.add(newPrice);
        return count;
    }
}