package ru.job4j.csi;

import java.time.LocalDate;
import java.util.*;

/**
 * Class PricePooling.
 *
 * @author Yury Doronin (doronin.ltd@gmail.com)
 * @version 4.0
 * @since 27.02.2020
 */
public class PricePooling {

    /**
     * Checks up if new begin-price is after old begin-price.
     *
     * @param current, a collection with current prices.
     * @param is,      a collection with new prices.
     * @return true if there are no time crossing, otherwise false.
     */
    private boolean isBeginDateAfter(Price current, List<Price> is) {
        boolean result = false;
        for (Price newPrice : is) {
            if (current.getProductCode().equals(newPrice.getProductCode())) {
                if (newPrice.getBegin().isAfter(current.getBegin())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Checks up if new end-price is after old end-price.
     *
     * @param current, a collection with current prices.
     * @param is,      a collection with new prices.
     * @return true if there are no time crossing, otherwise false.
     */
    private boolean isEndDateAfter(Price current, List<Price> is) {
        boolean result = false;
        for (Price newPrice : is) {
            if (current.getProductCode().equals(newPrice.getProductCode())) {
                if (newPrice.getEndTime().isAfter(current.getEndTime())) {
                    result = true;
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
        List<Price> result = new ArrayList<>();
        int count = 1;
        for (Price current : was) {
            if (current.getValue() == 0 && this.isBeginDateAfter(current, is)
                    && this.isEndDateAfter(current, is)) {
                for (Price newPrice : is) {
                    if (current.getProductCode().equals(newPrice.getProductCode())) {
                        current.setValue(newPrice.getValue());
                        current.setId(count++);
                        result.add(current);
                        break;
                    }
                }
            }
            if (current.getValue() != 0 && this.isBeginDateAfter(current, is)
                    && !this.isEndDateAfter(current, is)) {
                for (Price newPrice : is) {
                    LocalDate saveCurrentBegin = newPrice.getEndTime();
                    LocalDate saveCurrentEnd = current.getEndTime();
                    if (current.getProductCode().equals(newPrice.getProductCode())) {
                        count = this.init(count, result, current, newPrice); // 4th and 5th elements.
                        result.add(current); // 6th element
                        Price lastElement = result.get(result.size() - 1);
                        lastElement.setId(count++);
                        lastElement.setBegin(saveCurrentBegin);
                        lastElement.setEndTime(saveCurrentEnd);
                        break;
                    }
                }
            } else {
                for (Price newPrice : is) {
                    if (current.getValue() != newPrice.getValue() && current.getNumber() == newPrice.getNumber()) {
                        count = this.init(count, result, current, newPrice);
                        break;
                    } else if (current.getNumber() == newPrice.getNumber()) {
                        current.setEndTime(newPrice.getEndTime());
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