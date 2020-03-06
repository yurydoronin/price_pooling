package ru.job4j.test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Class Price.
 *
 * @author Yury Doronin (doronin.ltd@gmail.com)
 * @version 1.0
 * @since 25.02.2020
 */
public class Price {

    private long id;

    /**
     * A product code.
     */
    private final String productCode;

    /**
     * A price number.
     */
    private final int number;

    /**
     * A department number.
     */
    private final int depart;

    /**
     * A price time in the moment it was set up.
     */
    private LocalDate begin;

    /**
     * A price time in the moment it was ended up.
     */
    private LocalDate endTime;

    /**
     * A price.
     */
    private int value;

    public Price(long id, String productCode, int number, int depart,
                 LocalDate begin, LocalDate endTime, int value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.endTime = endTime;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (String.format(
                "Price id %s, code %s, number %s, depart %s, %s - %s, value %s%n",
                this.id,
                this.productCode,
                this.number,
                this.depart,
                this.begin.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                this.endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                this.value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return depart == price.depart
                && Objects.equals(productCode, price.productCode);
    }

    @Override
    public int hashCode() {
        return ((productCode != null ? productCode.hashCode() : 0) << 5) - Integer.hashCode(this.depart);
    }
}