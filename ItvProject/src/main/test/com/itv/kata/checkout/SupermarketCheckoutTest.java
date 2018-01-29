package com.itv.kata.checkout;

import static com.itv.kata.pricing.IndividualPricingRule.pricingRuleFor;
import static com.itv.kata.pricing.MultiItemPricingRule.multiItemPricingRuleFor;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.itv.kata.checkout.KataSupermarketCheckout.Transaction;
import com.itv.kata.model.Item;
import com.itv.kata.pricing.PricingRule;

public class SupermarketCheckoutTest {

    private static final Item A = new Item("A");
    private static final Item B = new Item("B");
    private static final Item C = new Item("C");
    private static final Item D = new Item("D");

    private KataSupermarketCheckout checkout = new KataSupermarketCheckout();

    private Set<PricingRule> thisWeeksPrices;

	public void initThisWeekPrices() {
		thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.50).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(B).atPrice(new BigDecimal(0.30).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(C).atPrice(new BigDecimal(0.20).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(D).atPrice(new BigDecimal(0.15).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(A).buy(3).atPrice(new BigDecimal(1.30).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(B).buy(2).atPrice(new BigDecimal(0.45).setScale(2, RoundingMode.HALF_UP)));
	}
    
    @Test
    public void calculateTotalPriceOfIndividuallyPricedItems() {
    	initThisWeekPrices();
        Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(A)
                .scan(D);
        assertEquals(new BigDecimal(0.65).setScale(2,RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));
    }

    @Test
    public void calculateTotalPriceOfMultiPricedItems() {
    	initThisWeekPrices();
        Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(A)
                .scan(A)
                .scan(A)
                .scan(C);

        assertEquals(new BigDecimal(1.50).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));        
    }

    @Test
    public void calculateTotalPriceOfMultiPricedItemsInAnyOrder() {
    	initThisWeekPrices();
        Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(B)
                .scan(A)
                .scan(B);

        assertEquals(new BigDecimal(0.95).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));  
    }

    @Test
    public void onlyChargeForMultiplesOfOffers() {
    	initThisWeekPrices();
        Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(B)
                .scan(A)
                .scan(B)
                .scan(B);

        assertEquals(new BigDecimal(1.25).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));  
    }

}