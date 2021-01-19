package org.jrandrews.liveperson.customers.servicecase;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.servicecase.data.ServiceCaseDao;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCase;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCaseId;
import org.jrandrews.liveperson.customers.tenant.TenantLocator;

import io.quarkus.security.Authenticated;

/**
 * Emits individual service cases.
 */
@Path("/servicecase")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceCaseResource {

    private ServiceCaseDao dao = new ServiceCaseDao();
    private TenantLocator tenantLocator = new TenantLocator();

    @GET
    @Path("{id}")
    @Authenticated  // probably not everyone, but certainly everyone in this particular implementation.
    public ServiceCase getServiceCase(@PathParam("id") String id, @Context SecurityContext securityContext) {
        return dao.retrieve(new ServiceCaseId(tenantLocator.getTenant(securityContext), Long.parseLong(id)));
    }
}
