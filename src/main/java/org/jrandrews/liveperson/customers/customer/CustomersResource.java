package org.jrandrews.liveperson.customers.customer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.ListPage;
import org.jrandrews.liveperson.customers.customer.data.CustomerDao;
import org.jrandrews.liveperson.customers.customer.data.CustomerObjectDao;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.jrandrews.liveperson.customers.tenant.TenantLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Groups of customers.
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static class CustomerPage extends ListPage<Customer> {
        public void addCustomer(Customer c) {
            resultList.add(c);
        }
    };

    private CustomerObjectDao dao = new CustomerDao();
    private TenantLocator tenantLocator = new TenantLocator();

    /**
     * Validates and saves a filter expression. Current supported format:
     * 
     * ex: {"idList" : [ 5, 7 ]}
     *
     * @param filter the deserialized filter expression
     * @return the ID of that expression as stored.
     */
    @POST
    @RolesAllowed({"admin", "piiEnabled"})
    @Consumes(MediaType.APPLICATION_JSON)
    public CustomerFilterId createSearch(CustomerFilterExpression filter, @Context SecurityContext securityContext) {

        return dao.create(tenantLocator.getTenant(securityContext), filter);
    }

    /**
     * Retrieves the paged list of customers matching the supplied filter ID.
     * 
     * TODO: implement paging.
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"admin", "piiEnabled"})
    public CustomerPage getCustomers(@PathParam("id") String filterId, @QueryParam("page") Integer page, @Context SecurityContext securityContext) {
        CustomerPage result = new CustomerPage();
        
        // TODO: remove this and implement paging. This just tells us that the paging info arrived here.
        if (page!=null) {
            result.setPageNumber(page.intValue());
        }

        TenantId tenant = tenantLocator.getTenant(securityContext);

        CustomerFilterExpression filter = dao.retrieve(new CustomerFilterId(tenant, Long.valueOf(filterId)));

        for (long id : filter.idList) {
            Customer customer = dao.retrieve(new CustomerId(tenant, id));
            if ( ! securityContext.isUserInRole("admin")) {
                customer = Customer.mask(customer);
                logger.info("masked customer {} for user {}", customer.getId(), getPrincipal(securityContext));
            } else {
                logger.info("using unmasked customer {} for user {}", customer.getId(), getPrincipal(securityContext));
            }
            result.addCustomer(customer);
        }

        return result;
    }

    private String getPrincipal(SecurityContext securityContext) {
        return securityContext.getUserPrincipal() == null ? "(null)" : securityContext.getUserPrincipal().getName();
    }
    
}
