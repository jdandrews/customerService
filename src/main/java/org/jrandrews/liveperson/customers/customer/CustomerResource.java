package org.jrandrews.liveperson.customers.customer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.customer.data.CustomerDao;
import org.jrandrews.liveperson.customers.customer.data.CustomerObjectDao;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.jrandrews.liveperson.customers.tenant.TenantLocator;

/**
 * Emits individual customers.
 */
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
 
    private CustomerObjectDao dao = new CustomerDao();
    private TenantLocator tenantLocator = new TenantLocator();

    @GET
    @Path("{id}")
    @RolesAllowed({"admin","piiEnabled"}) 
    public Customer getCustomer(@PathParam("id") String id, @Context SecurityContext securityContext) {
        long customerId = Long.parseLong(id);
        Customer customer = dao.retrieve(new CustomerId(tenantLocator.getTenant(securityContext), customerId));

        if ( ! securityContext.isUserInRole("admin")) {
            customer = Customer.mask(customer);
        }

        return customer;
    }

    @POST
    @Path("")
    @RolesAllowed({"admin","piiEnabled"}) 
    @Consumes(MediaType.APPLICATION_JSON)
    public CustomerId createCustomer(Customer customer, @Context SecurityContext securityContext) {
        TenantId tenantId = tenantLocator.getTenant(securityContext);
        return dao.create(tenantId, customer);
    }

    @PUT
    @Path("")
    @RolesAllowed({"admin","piiEnabled"}) 
    @Consumes(MediaType.APPLICATION_JSON)
    public CustomerId updateCustomer(Customer customer, @Context SecurityContext securityContext) {
        TenantId tenant = tenantLocator.getTenant(securityContext);
        Customer existing = dao.retrieve(new CustomerId(tenant, customer.getId()));
        if (existing==null) {
            throw new UnsupportedOperationException("customer "+customer.getId()+" does not exist.");
        }

        return dao.update(tenant, customer);
    }
}
