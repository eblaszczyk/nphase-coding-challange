package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.math.RoundingMode.HALF_UP;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    @Test
    public void task1_calculatesPrice() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 1),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(12.0));
    }

    @Test
    public void task2_calculatesPriceWith10PercentageDiscountPerQuantity_shouldAddDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 4),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2)
        ));

        BigDecimal result = service.calculateTotalPriceWithPercentageDiscountPerQuantityBiggerThat(
                cart, 10, 3);

        Assertions.assertEquals(result.setScale(1, HALF_UP), BigDecimal.valueOf(25.0));
    }

    @Test
    public void task2_calculatesPriceWith10PercentageDiscountPerQuantity_shouldNotAddDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 1),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3)
        ));

        BigDecimal result = service.calculateTotalPriceWithPercentageDiscountPerQuantityBiggerThat(
                cart, 10, 3);

        Assertions.assertEquals(result, BigDecimal.valueOf(15.5));
    }

    @Test
    public void task3_calculatesPriceWith10PercentageDiscountPerQuantity_shouldAddDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2, "drinks"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2, "drinks"),
                new Product("Cheese", BigDecimal.valueOf(8), 2, "food")
        ));

        BigDecimal result = service.calculateTotalPriceWithPercentageDiscountPerCategory(
                cart, 10, 3);

        Assertions.assertEquals(result, BigDecimal.valueOf(31.84));
    }

    @Test
    public void task4_calculatesPriceWithPercentageDiscountPerQuantity_shouldAddDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 4, "drinks"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 4, "drinks"),
                new Product("Cheese", BigDecimal.valueOf(8), 4, "food")
        ));

        BigDecimal result = service.calculateTotalPriceWithPercentageDiscountPerCategory(
                cart, 50, 2);

        Assertions.assertEquals(result.setScale(1, HALF_UP), BigDecimal.valueOf(33.6));
    }

}