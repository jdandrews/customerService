package org.jrandrews.liveperson.customers.customer.data;

import java.util.List;

import org.jrandrews.liveperson.customers.customer.CustomerFilterExpression;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;

public interface CustomerIndexDao {

    List<CustomerId> retrieve(CustomerFilterExpression filter);
}
