package org.lightadmin.test.nameExtractor;

import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.test.model.TestLineItem;

import javax.annotation.Nullable;

public class LineItemNameExtractor implements EntityNameExtractor<TestLineItem>{

	@Override
	public String apply( @Nullable TestLineItem lineItem ) {
		return lineItem.getId() +
				". Product: " + lineItem.getProduct().getName() +
				"; Amount: " + lineItem.getAmount() +
				"; Total: " + lineItem.getTotal();
	}
}
