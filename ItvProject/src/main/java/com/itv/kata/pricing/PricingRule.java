package com.itv.kata.pricing;

import com.itv.kata.model.Item;

/**
 * This interface helps system to build the pricing rule for different business
 * scenarios example individual items and multiple items.
 */
public interface PricingRule {

	public class Unit {
		// Price is in pence.
		private final long price;
		private final long quantity;

		public Unit(final long price, final long quantity) {
			this.price = price;
			this.quantity = quantity;
		}

		public long getPrice() {
			return price;
		}

		public long getQuantity() {
			return quantity;
		}

		@Override
		public String toString() {
			return "Unit [price=" + price + "p, quantity=" + quantity + "]";
		}
	}

	Item getItem();

	Unit getUnit(long quantity);

}
