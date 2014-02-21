package org.lightadmin.demo.config.listener;

import org.lightadmin.demo.model.Customer;
import org.lightadmin.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.repository.annotation.HandleAfterSave;
import org.springframework.data.rest.repository.annotation.HandleBeforeSave;
import org.springframework.data.rest.repository.annotation.RepositoryEventHandler;

@RepositoryEventHandler(Customer.class)
public class CustomerRepositoryEventListener {

    private final Logger logger = LoggerFactory.getLogger(CustomerRepositoryEventListener.class);

    @Autowired
    private CustomerService customerService;

    @HandleBeforeSave
    public void onBeforeSave(Customer customer) {
        final boolean vipCustomer = customerService.isVIP(customer);

        logger.info("#handleBeforeSave: Customer {} is VIP: {}", customer, vipCustomer);
    }

    @HandleAfterSave
    public void onAfterSave(Customer customer) {
        logger.info("#handleAfterSave: Hello");
    }
}