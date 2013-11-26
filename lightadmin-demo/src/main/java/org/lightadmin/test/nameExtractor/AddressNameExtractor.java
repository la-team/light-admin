package org.lightadmin.test.nameExtractor;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.test.model.TestAddress;

public class AddressNameExtractor implements EntityNameExtractor<TestAddress> {

    @Override
    public String apply(TestAddress address) {
        return String.format("%s, %s, %s", address.getStreet(), address.getCity(), address.getCountry());
    }
}
