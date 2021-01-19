package org.jrandrews.liveperson.customers.customer.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jrandrews.liveperson.customers.customer.CustomerFilterExpression;
import org.jrandrews.liveperson.customers.customer.CustomerFilterId;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.tenant.TenantId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerDao implements CustomerIndexDao, CustomerObjectDao {

    //  ****    The test database     *****

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String CUSTOMER_JSON = "["
            + "{\"id\":\"1\",  \"ssn\":\"123-45-6789\", \"name\":{\"name\":\"Alice\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"2\",  \"ssn\":\"234-56-7890\", \"name\":{\"name\":\"Bob\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"3\",  \"ssn\":\"345-67-8901\", \"name\":{\"name\":\"Camille\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"4\",  \"ssn\":\"456-78-9012\", \"name\":{\"name\":\"Doug\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"5\",  \"ssn\":\"567-89-0123\", \"name\":{\"name\":\"Eve\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"6\",  \"ssn\":\"678-90-1234\", \"name\":{\"name\":\"Frank\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"7\",  \"ssn\":\"789-01-2345\", \"name\":{\"name\":\"Grace\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"8\",  \"ssn\":\"890-12-3456\", \"name\":{\"name\":\"Henry\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"9\",  \"ssn\":\"901-23-4567\", \"name\":{\"name\":\"Indira\"},"
            + "\"contactInfoList\":[]},"
            + "{\"id\":\"10\", \"ssn\":\"012-34-5678\", \"name\":{\"name\":\"Jaipal\"},"
            + "\"contactInfoList\":[]}"
            + "]";

    private static Map<Long, Customer> idToCustomer = new HashMap<>();
    private static List<CustomerFilterExpression> filters = new ArrayList<>();
    private static long nextCustomerId = 11;

    //
    // Create the customer database
    //
    static {
        List<Customer> customers;
        TypeReference<List<Customer>> listOfCustomerType = new TypeReference<List<Customer>>() {};
        try {
            customers = mapper.readValue(CUSTOMER_JSON, listOfCustomerType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            customers = new ArrayList<>();
        }

        idToCustomer = customers.stream().collect(Collectors.toMap(Customer::getId, Function.identity()));
    }


    //
    //      Customer
    //

	@Override
    public CustomerId create(TenantId tenant, Customer customer) {
    	Customer newCustomer = new Customer(customer);
    	
    	long newID = nextCustomerId++;

    	newCustomer.setId(newID);
        idToCustomer.put(newID, newCustomer);

        return new CustomerId(tenant, newID);
    }

	@Override
    public CustomerId update(TenantId tenant, Customer customer) {
        long id = customer.getId();

        if ( ! idToCustomer.containsKey(id)) {
            throw new UnsupportedOperationException("Customer "+id+" does not exist.");
        }

        idToCustomer.put(id, customer);

        return new CustomerId(tenant, id);
    }

    @Override
    public Customer retrieve(CustomerId id) {
        Customer customer = idToCustomer.get(id.getId());
        return idToCustomer.get(id.getId());
    }

    @Override
    public List<CustomerId> retrieve(CustomerFilterExpression filter) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    //
    // Customer Filters
    //

    @Override
    public CustomerFilterId create(TenantId tenantId, CustomerFilterExpression filter) {
        int n = filters.indexOf(filter);
        if (n < 0) {
            filters.add(filter);
            n = filters.size() - 1;

        }

        return new CustomerFilterId(tenantId, n);
    }

    @Override
    public CustomerFilterExpression retrieve(CustomerFilterId filterId) {
        return filters.get((int) filterId.getId());
    }
}
