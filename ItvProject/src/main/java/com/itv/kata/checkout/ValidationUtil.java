package com.itv.kata.checkout;

import java.util.HashSet;
import java.util.Set;

import com.itv.kata.exceptions.InvalidKataRequestException;
import com.itv.kata.model.Item;
import com.itv.kata.pricing.PricingRule;

public final class ValidationUtil {

	private ValidationUtil() {
		super();
	}
	/**
     * Validates the pricing rules.
     * @param pricingRules the given pricing rules.
     * @return true if valid else throws exception.
     * @throws InvalidKataRequestException
     */
    public static boolean validatePricingRules(final Set<PricingRule> pricingRules) throws InvalidKataRequestException {
    	if(null != pricingRules && !pricingRules.isEmpty()) return true;
    	throw new InvalidKataRequestException("Please provide set of pricing rules.");
	}
    
    
    /**
     * Return true if item in basket has valid pricing rule. Else should throw the exception.
     * @param item - item to put in basket.
     * @param pricingRules set of pricingRules .
     * @return  true if item has pricing rules.
     */
    public static boolean validateItem(Item item, final Set<PricingRule> pricingRules) {
		Set<Item> itemsInPricingRuleSet = new HashSet<Item>();
		for (PricingRule rule : pricingRules) {
			itemsInPricingRuleSet.add(rule.getItem());
		}
		
		// If item is in pricing rule set, then fine. Else tell the customer to remove the item.
		if(!itemsInPricingRuleSet.contains(item)) {
			throw new InvalidKataRequestException("Sorry!" + item + " has not been priced yet. Please remove this item from basket.");
		}
		
		return true;
    }
    
}
