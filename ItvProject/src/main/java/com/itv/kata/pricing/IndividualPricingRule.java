package com.itv.kata.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.itv.kata.model.Item;

/**
 * This class contains the builder to build an individual item purchase price rule.
 * 
 * Example you can - Buy item x at price z. so you need to build a price rule using rule(x).atPrice(z); 
 *
 */
@PricingRuleOrder(0)
public final class IndividualPricingRule implements PricingRule {

    public static class PricingRuleBuilder {

        private final Item item;

        public PricingRuleBuilder(final Item item) {
            this.item = item;
        }

        /**
         * IndividualPricingRule
         * @param price price in GBP.
         * @return IndividualPricingRule
         */
        public IndividualPricingRule atPrice(final BigDecimal price) {
        	long tempPrice = price.multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING).longValue();
            return new IndividualPricingRule(item, tempPrice);
        }
    }

	/**
	 * Gives you PricingRuleBuilder object means the builder for Individual Item
	 * price of given item.
	 * 
	 * @param item
	 *            - item you want to build rule for.
	 * @return PricingRuleBuilder
	 */
    public static PricingRuleBuilder pricingRuleFor(Item item) {
        return new PricingRuleBuilder(item);
    }

    
    
    
    
    private final Item item;
    // Price is in pence.
    private final long price;

    /**
     * Constructor for building price rule for selling individual item.
     * @param item - item
     * @param price - price of item in pence.
     */
    private IndividualPricingRule(final Item item, final long price) {
        this.item = item;
        this.price = price;
    }

    @Override
    public Item getItem() {
        return item;
    }
    
    @Override
    public Unit getUnit(final long quantity) {
        return new Unit(quantity * price, quantity);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + (int) (price ^ (price >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IndividualPricingRule other = (IndividualPricingRule) obj;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.equals(other.item))
            return false;
        if (price != other.price)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "item " + item + " at " + price + "p each";
    }

}
