package org.sj.ws.app.service;

import org.sj.ws.app.model.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Service
 */
public class TestService {

    public List<Test> getTests(){
        List<Test> tests = new ArrayList<>();
        tests.add(new Test("a"));
        tests.add(new Test("b"));
        return tests;
    }
}
