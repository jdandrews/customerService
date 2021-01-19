package org.jrandrews.liveperson.customers.servicecase.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.servicecase.ServiceCaseFilterExpression;
import org.jrandrews.liveperson.customers.servicecase.ServiceCaseFilterId;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCase;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCaseId;
import org.jrandrews.liveperson.customers.tenant.TenantId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Interface to the service case management service.
 * 
 * We're relying on that service to have appropriate search APIs. This is a stub
 * to an embedded data structure.
 */
public class ServiceCaseDao {
    //  ****    The test database     *****

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DATA = "["
            + "{\"id\":1, \"customerId\":7, \"info\":\"a case for customer ID 7\"},"
            + "{\"id\":2, \"customerId\":7, \"info\":\"another case for customer ID 7\"},"
            + "{\"id\":17, \"customerId\":3, \"info\":\"customer ID 3 had some kind of problem\"},"
            + "{\"id\":144, \"customerId\":27, \"info\":\"invalid customer ID; probably a data cleanup issue.\"},"
            + "{\"id\":97, \"info\":\"no customer ID--probably an internal ticket.\"}"
            + "]";

    private static Map<Long, ServiceCase> idToCase = new HashMap<>();
    private static List<ServiceCaseFilterExpression> filters = new ArrayList<>();

    //
    // Create the database
    //
    static {
        List<ServiceCase> cases;
        TypeReference<List<ServiceCase>> listOfServiceCaseType = new TypeReference<List<ServiceCase>>() {};
        try {
            cases = mapper.readValue(DATA, listOfServiceCaseType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cases = new ArrayList<>();
        }

        idToCase = cases.stream().collect(Collectors.toMap(ServiceCase::getId, Function.identity()));
    }

    /**
     * Retrieve a specific service case.
     * 
     * @param id the ID of the service case to retrieve
     * @return the identified service case, or null if the ID is invalid.
     */
    public ServiceCase retrieve(ServiceCaseId id) {
        return idToCase.get(id.getId());
    }

    /**
     * Retrieve a list of service case IDs associated with a given customer.
     * 
     * @param customerId
     * @return
     */
    public List<ServiceCaseId> retrieveCaseIdsFor(CustomerId customerId) {

        List<Long> idList = idToCase.values().stream().filter(
                (serviceCase) -> 
                    customerId.getId()==serviceCase.getCustomerId() )
                .map(ServiceCase::getId).collect(Collectors.toList());

        TenantId tenantId = new TenantId(customerId.getTenantId());

        return idList.stream().map((id) -> new ServiceCaseId(tenantId, id)).collect(Collectors.toList());
    }


    //
    //  Filters
    //

    public ServiceCaseFilterId create(TenantId tenantId, ServiceCaseFilterExpression filter) {
        int n = filters.indexOf(filter);
        if (n < 0) {
            filters.add(filter);
            n = filters.size() - 1;

        }

        return new ServiceCaseFilterId(tenantId, n);
    }

    public ServiceCaseFilterExpression retrieve(ServiceCaseFilterId filterId) {
        return filters.get((int) filterId.getId());
    }
}
