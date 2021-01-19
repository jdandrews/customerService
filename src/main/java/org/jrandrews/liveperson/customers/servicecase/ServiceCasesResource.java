package org.jrandrews.liveperson.customers.servicecase;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.ListPage;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.servicecase.data.ServiceCaseDao;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCase;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCaseId;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.jrandrews.liveperson.customers.tenant.TenantLocator;

import io.quarkus.security.Authenticated;

/**
 * Emits individual service cases.
 */
@Path("/servicecases")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceCasesResource {
    public static class ServiceCasePage extends ListPage<ServiceCase> {

        public void addCase(ServiceCase serviceCase) {
            this.resultList.add(serviceCase);
        }
    }

    private ServiceCaseDao dao = new ServiceCaseDao();
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
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceCaseFilterId createSearch(ServiceCaseFilterExpression filter, @Context SecurityContext securityContext) {

        return dao.create(tenantLocator.getTenant(securityContext), filter);
    }

    @GET
    @Path("{id}")
    @Authenticated  // probably not everyone, but certainly everyone in this particular implementation.
    public ServiceCasePage getServiceCasesForCustomer(@PathParam("id") String id, @Context SecurityContext securityContext) {

        TenantId tenantId = tenantLocator.getTenant(securityContext);

        ServiceCaseFilterExpression filter = dao.retrieve( new ServiceCaseFilterId(tenantId, Long.parseLong(id)) );
        List<ServiceCaseId> caseIds = dao.retrieveCaseIdsFor(new CustomerId(tenantId, filter.customerId));

        ServiceCasePage result = new ServiceCasePage();
        for (ServiceCaseId caseId : caseIds) {
            result.addCase(dao.retrieve(caseId));
        }
        return result;
    }
}
