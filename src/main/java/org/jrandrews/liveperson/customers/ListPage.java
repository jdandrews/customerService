package org.jrandrews.liveperson.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ListPage<T> {
    private UUID requestId;
    private int firstResultOnThisPage = 0;
    private int pageNumber = 0;
    private boolean finalPage = true;
    protected List<T> resultList = new ArrayList<>();

    public UUID getRequestId() {
        return requestId;
    }
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
    public int getFirstResultOnThisPage() {
        return firstResultOnThisPage;
    }
    public void setFirstResultOnThisPage(int firstResultOnThisPage) {
        this.firstResultOnThisPage = firstResultOnThisPage;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public boolean isFinalPage() {
        return finalPage;
    }
    public void setFinalPage(boolean finalPage) {
        this.finalPage = finalPage;
    }
    public List<T> getResultList() {
        return resultList;
    }
    public void setResultList(List<T> resultList) {
        this.resultList = Collections.unmodifiableList(resultList);
    }
}
