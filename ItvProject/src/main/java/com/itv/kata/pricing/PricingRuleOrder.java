package com.itv.kata.pricing;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * PricingRuleOrder class is used for ordering the pricing rules such a way that
 * MultiItem Pricing Rule given high priority than other. For more details about
 * priorities, please see the values provided.
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface PricingRuleOrder {

    int value();
    
}