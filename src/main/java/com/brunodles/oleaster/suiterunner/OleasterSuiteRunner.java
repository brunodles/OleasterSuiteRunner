/*
* Copyright 2016 Bruno de Lima e Silva
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.brunodles.oleaster.suiterunner;

import com.mscharhag.oleaster.runner.Invokable;
import com.mscharhag.oleaster.runner.OleasterTest;
import com.mscharhag.oleaster.runner.StaticSupportingSuiteBuilder;
import com.mscharhag.oleaster.runner.suite.Suite;
import com.mscharhag.oleaster.runner.suite.SuiteBuilder;
import com.mscharhag.oleaster.runner.suite.SuiteDefinition;
import com.mscharhag.oleaster.runner.suite.SuiteDefinitionEvaluator;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


/**
 * OleasterRunner is JUnit runner that lets you write JUnit tests
 * like you write Jasmine tests (a popular Javascript testing framework).
 * {@link com.mscharhag.oleaster.runner.OleasterRunner}
 * I just made a change to improve the output.
 */
public class OleasterSuiteRunner extends ParentRunner<Suite> {

    private Describer describer;

    public OleasterSuiteRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        describer = new Describer(getTestClass());
    }

    @Override
    protected List<Suite> getChildren() {
        SuiteBuilder suiteBuilder = this.createSuiteBuilder();
        SuiteDefinition baseSuiteDefinition = this.createBaseSuiteDefinition(suiteBuilder);
        SuiteDefinitionEvaluator evaluator = this.createSuiteDefinitionEvaluator();

        Suite suite = evaluator.evaluate(baseSuiteDefinition, suiteBuilder);
//        if (suite.getSpecs().size()>0) return Arrays.asList(suite);
        return suite.getSuites();
    }

    protected SuiteBuilder createSuiteBuilder() {
        return new StaticSupportingSuiteBuilder();
    }

    protected SuiteDefinition createBaseSuiteDefinition(SuiteBuilder suiteBuilder) {
        return new SuiteDefinition(null, null, () -> {
            Object obj = getTestClass().getJavaClass().newInstance();
            if (obj instanceof OleasterTest) {
                ((OleasterTest) obj).buildTestSuite(suiteBuilder);
            }
        });
    }

    protected SuiteDefinitionEvaluator createSuiteDefinitionEvaluator() {
        return new SuiteDefinitionEvaluator();
    }

    @Override
    protected Description describeChild(Suite child) {
        return describer.describeSuite(null, child);
    }

    @Override
    protected void runChild(Suite suite, RunNotifier notifier) {
        Description description = describer.get(suite);
        runBeforeCallbacks(suite);

        suite.getSpecs().forEach(spec -> {
            notifier.fireTestStarted(description);
            runBeforeEachCallbacks(suite);
            runLeaf(spec, describer.get(spec), notifier);
            runAfterEachCallbacks(suite);
            notifier.fireTestFinished(description);
        });

        suite.getSuites().forEach(childSuite -> {
            runChild(childSuite, notifier);
        });

        runAfterCallbacks(suite);
    }

    private void runBeforeEachCallbacks(Suite suite) {
        this.runInvokables(this.collectInvokables(suite, Suite::getBeforeEachHandlers, true));
    }

    private void runBeforeCallbacks(Suite suite) {
        this.runInvokables(this.collectInvokables(suite, Suite::getBeforeHandlers, true));
    }


    private void runAfterEachCallbacks(Suite suite) {
        this.runInvokables(this.collectInvokables(suite, Suite::getAfterEachHandlers, false));
    }

    private void runAfterCallbacks(Suite suite) {
        this.runInvokables(this.collectInvokables(suite, Suite::getAfterHandlers, false));
    }

    private List<Invokable> collectInvokables(Suite suite, Function<Suite, List<Invokable>> method, boolean reverseOrder) {
        List<List<Invokable>> lists = new ArrayList<>();
        Suite parent = suite;
        while (parent != null) {
            lists.add(new ArrayList<>(method.apply(parent)));
            parent = parent.getParent();
        }

        if (reverseOrder) {
            Collections.reverse(lists);
        }

        List<Invokable> flatList = new ArrayList<>();
        for (List<Invokable> list : lists) {
            flatList.addAll(list);
        }
        return flatList;
    }

    private void runInvokables(List<Invokable> invokables) {
        invokables.forEach(callback -> {
            try {
                callback.invoke();
            } catch (Exception e) {
                throw new RuntimeException("An exception occurred while running invokable: " + e.getMessage(), e);
            }
        });
    }
}
