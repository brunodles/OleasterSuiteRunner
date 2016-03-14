package com.github.brunodles.oleaster_suite_runner;

import com.mscharhag.oleaster.runner.suite.Spec;
import com.mscharhag.oleaster.runner.suite.Suite;
import org.junit.runner.Description;
import org.junit.runners.model.TestClass;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno de Lima on 11/03/16.
 */
class Describer {

    TestClass testClass;
    private IdFactory idFactory = new IdFactory();
    private Map<Object, Description> map = new HashMap<>();
    private Map<Serializable, Description> idMap = new HashMap<>();

    Describer(TestClass testClass) {
        this.testClass = testClass;
    }

    Description describeSuite(String parent, Suite suite) {
        Description description = map.get(suite);
        if (description != null) return description;
        return buildDescription(parent, suite);
    }

    private Description buildDescription(String parent, Suite suite) {
        Serializable id = idFactory.nextId();
        String description = buildDescriptionText(parent, suite);

        Description suiteDescription = Description.createSuiteDescription(description, id);

        suite.getSpecs().forEach(cSpec -> suiteDescription.addChild(describeSpec(description, cSpec)));
        suite.getSuites().forEach(cSuite -> suiteDescription.addChild(describeSuite(description, cSuite)));

        map.put(suite, suiteDescription);
        idMap.put(id, suiteDescription);
        return suiteDescription;
    }

    private String buildDescriptionText(String parent, Suite suite) {
        String description = suite.getDescription();
        if (description == null || description.isEmpty())
            description = getTestClass().getName();
        if (parent != null)
            description = merge(parent, description);
        return description;
    }

    private TestClass getTestClass() {
        return testClass;
    }

    private String merge(String first, String last) {
        if (first == null || first.isEmpty()) return last;
        if (last == null || last.isEmpty()) return first;
        return String.format("%s.%s", first, last);
    }

    Description describeSpec(String description, Spec spec) {
        Description found = map.get(spec);
        if (found != null) return found;
        return buildDescription(description, spec);
    }

    private Description buildDescription(String description, Spec spec) {
        Serializable id = idFactory.nextId();
        Description testDescription = Description.createTestDescription(
                description, "It " + spec.getDescription(), id);

        map.put(spec, testDescription);
        idMap.put(id, testDescription);
        return testDescription;
    }

    Description get(Spec spec) {
        return map.get(spec);
    }

    Description get(Suite suite) {
        return map.get(suite);
    }
}
