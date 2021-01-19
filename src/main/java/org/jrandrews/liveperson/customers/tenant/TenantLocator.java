package org.jrandrews.liveperson.customers.tenant;

import javax.ws.rs.core.SecurityContext;

public class TenantLocator {
    /**
     * Look up the tenant ID for the supplied security context.
     * 
     * For now, we can stub this, but tenancy can be determined from the request information,
     * and the fact that we are multi-tenant needs to be masked from users, so it's probably
     * part of the security validation process.
     * 
     * @param securityContext
     * @return the tenant ID of the user
     */
    public TenantId getTenant(SecurityContext securityContext) {
        return new TenantId(0);       // LivePerson is Tenant 0.
    }
}
