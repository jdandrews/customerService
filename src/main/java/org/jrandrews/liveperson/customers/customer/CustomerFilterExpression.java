package org.jrandrews.liveperson.customers.customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a customer filter.
 */
public class CustomerFilterExpression {
    public List<Long> idList = new ArrayList<>();
    public String findInName;

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;

        if ( ! (obj instanceof CustomerFilterExpression)) return false;

        CustomerFilterExpression f = (CustomerFilterExpression)obj;

        boolean findInNameMatches = (findInName==null && f.findInName==null) || (findInName!=null && findInName.equals(f.findInName));

        return idList.equals(f.idList) && findInNameMatches;
    }
    
    @Override
    public int hashCode() {

        return idList.hashCode() + (findInName == null ? 0 : 31 * findInName.hashCode());
    }
}
