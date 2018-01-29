package com.itv.kata.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.itv.kata.model.Item;
import com.itv.kata.pricing.PricingRule;
import com.itv.kata.pricing.PricingRule.Unit;
import com.itv.kata.pricing.PricingRuleOrder;

/**
 * 
 * KataSupermarketCheckout is class to calculate the total of basket for a given transaction.
 * Where transaction contains list of pricing rules and the shopping basket.
 *
 */
public class KataSupermarketCheckout {

	/**
	 * The checkout transaction class containing list of pricing rules and the shopping basket.
	 */
    public class Transaction {

        private final Set<PricingRule> pricingRules;
        private final Map<Item, Long> shoppingBasket = new HashMap<Item, Long>();

        Transaction(Set<PricingRule> pricingRules) {
            this.pricingRules = pricingRules;
        }

        /**
         * Add the item to the shopping basket, if already exist increase the quantity.
         * @param item the item to purchase.
         * @return Transaction.
         */
        public Transaction scan(Item item) {
        	if(ValidationUtil.validateItem(item, this.pricingRules)) {
	            Long quantity = shoppingBasket.get(item);
	            if(quantity != null) {
	                quantity++;
	            } else {
	                quantity = 1L;
	            }
	
	            shoppingBasket.put(item, quantity);
        	}
            return this;
        }
    }

    /**
     * Gives you transaction for certain price rules.
     * @param pricingRules Set of price rules.
     * @return the new Transaction object.
     */
    public Transaction getTransaction(final Set<PricingRule> pricingRules) {
		if (ValidationUtil.validatePricingRules(pricingRules)) {
			return new Transaction(pricingRules);
		} else {
			return null;
		}
    }

	/**
	 * Calculates the total price of the basket for a given transaction and with given pricing rules.
	 * @param transaction the transaction.
	 * @return the total amount in GBP.
	 */
	public BigDecimal calculateTotalPrice(final Transaction transaction) {
		long totalPrice = 0;
		if(null != transaction.shoppingBasket && !transaction.shoppingBasket.isEmpty()) {
		
			
			final List<PricingRule> orderedPricingRules = orderPricingRules(transaction.pricingRules);
	        final Map<Item, Long> shoppingBasket = new HashMap<Item, Long>(transaction.shoppingBasket);
	        
	
			for (PricingRule rule : orderedPricingRules) {
				if (shoppingBasket.containsKey(rule.getItem())) {
	
					long quantityInBasket = shoppingBasket.get(rule.getItem());
					Unit unit = rule.getUnit(quantityInBasket);
					totalPrice += unit.getPrice();
					long remainingQty = 0;
					if(quantityInBasket == 1) {
						remainingQty = quantityInBasket;
					} else {
						remainingQty = quantityInBasket - unit.getQuantity();
					}
					// Update the basket with remaining quantity.
					shoppingBasket.put(rule.getItem(), remainingQty);
				}
			}
		}
		
		// totalPrice in GBP.
		BigDecimal totPrice = new BigDecimal(totalPrice).setScale(2, RoundingMode.CEILING);
		totPrice = totPrice.divide(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
		return totPrice;
    }


	
    /**
     * Gives you ordered pricing rules based on the defined PricingRuleOrder.
     * 
     * @param pricingRules the collection of pricing rules.
     * @return the ordered pricing rules based on the defined PricingRuleOrder.
     */
    private List<PricingRule> orderPricingRules(Set<PricingRule> pricingRules) {
    	List<PricingRule> pricingRulesList = pricingRules.stream().collect(Collectors.toList());
    	Collections.sort(pricingRulesList, new Comparator<PricingRule>() {
			public int compare(PricingRule o1, PricingRule o2) {
				return ((o2.getClass().getAnnotation(PricingRuleOrder.class).value()) - (o1.getClass().getAnnotation(PricingRuleOrder.class).value()));
			}
		});
		return pricingRulesList;
    }
    
}

