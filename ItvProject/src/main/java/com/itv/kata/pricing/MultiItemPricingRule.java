package com.itv.kata.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.itv.kata.model.Item;

/**
 * This class contains the builder to build a multiple item purchase price
 * rule.
 * 
 * Example you can - Buy item x of quantity y at special price z. so you need to
 * build a price rule using rule(x).buy(y).atPrice(z);
 *
 */
@PricingRuleOrder(1)
public final class MultiItemPricingRule implements PricingRule {

	
    /**
     * Build the price rule configuration for given item for special quantity and price.
     */
    public static class MultiItemPricingRuleBuilder {

        private final Item item;
        private int specialQty;

        public MultiItemPricingRuleBuilder(final Item item) {
            this.item = item;
        }

        public MultiItemPricingRuleBuilder buy(final int specialQty) {
            this.specialQty = specialQty;
            return this;
        }

        public MultiItemPricingRule atPrice(final BigDecimal specialPrice) {
        	long tempSpecialPrice = specialPrice.multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING).longValue();
            return new MultiItemPricingRule(item, tempSpecialPrice, specialQty);
        }
        
    }

    /**
     * Gives you MultiItem price builder for given item.
     * @param item - item you want to build rule for.
     * @return MultiItemPricingRuleBuilder
     */
    public static MultiItemPricingRuleBuilder multiItemPricingRuleFor(final Item item) {
        return new MultiItemPricingRuleBuilder(item);
    }

    private final Item item;
    private final int quantity;
    // Price is in pence.
    private final long price;
    

    /**
     * Constructor for given item, price and quantity.
     * @param item - item
     * @param price - provide the special price for multi item in pence.
     * @param quantity - provide the special quantity for multi item.
     */
    private MultiItemPricingRule(final Item item, final long price, final int quantity) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public Item getItem() {
        return item;
    }

    /**
     * Returns Unit if this rule can be applied to given quantity, else unit will contain 0 values. 
     * @param inputQuantity the actual quantity to which you want to apply this rule.
     */
    @Override
    public Unit getUnit(final long inputQuantity) {
    	final long applicableCount = inputQuantity / quantity;
        
        final long tempPrice = price * applicableCount;
        final long tempQty = quantity* applicableCount;
        return new Unit(tempPrice, tempQty);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + (int) (price ^ (price >>> 32));
        result = prime * result + quantity;
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
        MultiItemPricingRule other = (MultiItemPricingRule) obj;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.equals(other.item))
            return false;
        if (price != other.price)
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "item " + item + " at " + price + "p for " + quantity;
    }

}