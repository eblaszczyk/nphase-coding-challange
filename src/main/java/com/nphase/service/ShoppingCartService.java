package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts().stream().map(product -> product.getPricePerUnit()
                .multiply(BigDecimal.valueOf(product.getQuantity()))).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceWithPercentageDiscountPerQuantityBiggerThat(
            ShoppingCart shoppingCart, int percentageDiscount, int quantity) {

        double factorDiscount  = (100 - percentageDiscount) / 100.0;

        return shoppingCart.getProducts().stream().map(product -> {
            if (product.getQuantity() > quantity) {
                return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
                        .multiply(BigDecimal.valueOf(factorDiscount));
            } else {
                return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
            }
        }).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceWithPercentageDiscountPerCategory(
            ShoppingCart shoppingCart, int percentageDiscount, int quantity) {
        Map<String, List<Product>> productsPerCategoryMap = shoppingCart.getProducts().stream()
                .collect(groupingBy(Product::getCategory));

        double factorDiscount = (100 - percentageDiscount) / 100.0;

        Map<String, BigDecimal> factorsPerCategoryMap =
                calculateDiscountFactorsPerCategory(quantity, productsPerCategoryMap, factorDiscount);

        return shoppingCart.getProducts().stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()))
                        .multiply(factorsPerCategoryMap.get(product.getCategory())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static Map<String, BigDecimal> calculateDiscountFactorsPerCategory(
            int quantity, Map<String, List<Product>> productsPerCategoryMap, double factorDiscount) {
        Map<String, BigDecimal> factorsPerCategoryMap = new HashMap<>();

        productsPerCategoryMap.entrySet().forEach((entry -> {
            if (getSum(entry.getValue()) > quantity) {
                factorsPerCategoryMap.put(entry.getKey(), BigDecimal.valueOf(factorDiscount));
            } else {
                factorsPerCategoryMap.put(entry.getKey(), BigDecimal.ONE);
            }
        }));
        return factorsPerCategoryMap;
    }

    private static int getSum(List<Product> productsPerCategory) {
        return productsPerCategory.stream().mapToInt(Product::getQuantity).sum();
    }

}
