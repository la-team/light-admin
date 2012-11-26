package org.lightadmin.util;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class WebElementTransformer implements Function<WebElement, String> {

	public static List<String> transform( Collection<WebElement> collection ) {
		return newLinkedList( Collections2.transform( collection, new WebElementTransformer() ) );
	}

	public static String[] transformToArray( Collection<WebElement> collection ) {
		final Collection<String> elementsText = Collections2.transform( collection, new WebElementTransformer() );
		return elementsText.toArray( new String[elementsText.size()] );
	}

	@Override
	public String apply( final WebElement webElement ) {
		return webElement.getText();
	}
}