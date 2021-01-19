package org.jrandrews.liveperson.customers.tenant;

/**
 * Tenant ID.
 */
public class TenantId {
    private long id;

    public TenantId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return Long.toString(id, 10);
    }
}
