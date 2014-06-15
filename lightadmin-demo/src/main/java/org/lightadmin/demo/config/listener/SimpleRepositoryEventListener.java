package org.lightadmin.demo.config.listener;

import org.lightadmin.demo.model.Customer;
import org.lightadmin.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public class SimpleRepositoryEventListener extends AbstractRepositoryEventListener<Customer> {

    private final Logger logger = LoggerFactory.getLogger(SimpleRepositoryEventListener.class);

    @Autowired
    private CustomerService customerService;

    @Override
    public void onBeforeSave(Customer customer) {
        final boolean vipCustomer = customerService.isVIP(customer);

        logger.info("#handleBeforeSave: Customer {} is VIP: {}", customer, vipCustomer);
    }

    @Override
    public void onAfterSave(Customer customer) {
        logger.info("#handleAfterSave: Hello");
    }
}