package org.jrandrews.liveperson.customers.customer.data;

import org.jrandrews.liveperson.customers.customer.CustomerFilterExpression;
import org.jrandrews.liveperson.customers.customer.CustomerFilterId;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.tenant.TenantId;

public interface CustomerObjectDao {

    CustomerId create(TenantId tenant, Customer customer);

    Customer retrieve(CustomerId id);

    CustomerId update(TenantId tenant, Customer customer);

    
    CustomerFilterId create(TenantId tenantId, CustomerFilterExpression filter);

    CustomerFilterExpression retrieve(CustomerFilterId filterId);
}
