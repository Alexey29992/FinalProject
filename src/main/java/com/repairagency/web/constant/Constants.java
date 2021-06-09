package com.repairagency.web.constant;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    private final List<String> reqStatusFilters = new ArrayList<>();

    public Constants() {
        reqStatusFilters.add("none");
        reqStatusFilters.add("new");
        reqStatusFilters.add("wait-for-payment");
        reqStatusFilters.add("paid");
        reqStatusFilters.add("cancelled");
        reqStatusFilters.add("in-process");
        reqStatusFilters.add("done");
    }

    public List<String> getReqStatusFilters() {
        return reqStatusFilters;
    }

}
