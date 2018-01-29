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
import com.itv.kata.exceptions.InvalidKataRequestException;
import com.itv.kata.model.Item;
import com.itv.kata.pricing.PricingRule;

public class KataSupermarketCheckoutTest {

	private static final Item A = new Item("A");
	private static final Item B = new Item("B");
	private static final Item C = new Item("C");
	private static final Item D = new Item("D");

	private KataSupermarketCheckout checkout = new KataSupermarketCheckout();

	@Test(expected = InvalidKataRequestException.class)
	public void calculateNullIndividuallyPricedItems() {
		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		checkout.getTransaction(thisWeeksPrices);
		
	}

	@Test
	public void calculateTotalPriceOfIndividuallyAndMultiPricedItems() {
		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.50).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(B).atPrice(new BigDecimal(0.30).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(C).atPrice(new BigDecimal(2.00).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(D).atPrice(new BigDecimal(0.75).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(A).buy(3).atPrice(new BigDecimal(1.30).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(B).buy(2).atPrice(new BigDecimal(0.45).setScale(2, RoundingMode.HALF_UP)));

		Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(A)
                .scan(A)
                .scan(B)
                .scan(A)
                .scan(A)
                .scan(C)
        		.scan(D);

		assertEquals(new BigDecimal(4.85).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));
	}

	@Test
	public void calculateAItemBogof() {

		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.25).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(A).buy(2).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));

		Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(A)
                .scan(A);

		assertEquals(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));
	}
	
	
	@Test
	public void calculateNullBasket() {

		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.25).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(A).buy(2).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));

		Transaction transaction = checkout.getTransaction(thisWeeksPrices);// No Scanning of items here.

		assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));
	}
	
	@Test(expected = InvalidKataRequestException.class)
	public void calculateExtraCItemWithoutPriceTag() {

		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.25).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(A).buy(2).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));

		Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(A)
                .scan(A)
                .scan(C);
		
		checkout.calculateTotalPrice(transaction);

	}

	@Test
	public void calculateBitem3For2() {
		Set<PricingRule> thisWeeksPrices = new HashSet<PricingRule>();
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(pricingRuleFor(A).atPrice(new BigDecimal(0.25).setScale(2, RoundingMode.HALF_UP)));
		thisWeeksPrices.add(multiItemPricingRuleFor(B).buy(3).atPrice(new BigDecimal(0.50).setScale(2, RoundingMode.HALF_UP)));

		Transaction transaction = checkout.getTransaction(thisWeeksPrices)
                .scan(B)
                .scan(B)
                .scan(B);

		assertEquals(new BigDecimal(0.50).setScale(2, RoundingMode.HALF_UP), checkout.calculateTotalPrice(transaction));
	}

}