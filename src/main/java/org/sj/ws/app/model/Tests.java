package org.sj.ws.app.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Wrapper for Test List
 */
@XmlRootElement(name = "Tests")
@XmlSeeAlso({Test.class})
public class Tests {
    private List<Test> tests;

    public Tests(){

    }

    public Tests(List<Test> tests){
        this.tests = tests;
    }

    @XmlElement(name="Test")
    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
