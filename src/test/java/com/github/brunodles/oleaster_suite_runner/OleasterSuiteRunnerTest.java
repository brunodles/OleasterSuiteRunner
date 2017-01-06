package com.github.brunodles.oleaster_suite_runner;

import com.mscharhag.oleaster.runner.Invokable;
import com.mscharhag.oleaster.runner.OleasterTest;
import com.mscharhag.oleaster.runner.suite.Suite;
import com.mscharhag.oleaster.runner.suite.SuiteBuilder;
import junit.framework.Assert;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by bruno on 11/03/16.
 */
@RunWith(OleasterSuiteRunner.class)
public class OleasterSuiteRunnerTest {

    private static List<String> calls;
    private static Function<String, Invokable> block = (String name) -> () -> calls.add(name);

    private OleasterSuiteRunner runner;
    private List<Suite> suites;
    private Description description;
    private ArrayList<Description> children;
    private Description child;
    private static int beforeEachCallsCounter;
    private static int beforeCallsCounter;
    private static int innerBeforeCallsCounter;

    public static class TestClass {

        {
            describe("outer describe", () -> {
                before(() -> beforeCallsCounter++);
                describe("inner describe", () -> {

                    beforeEach(() -> beforeEachCallsCounter++);

                    it("inner it", block.apply("inner it"));

                    describe("inner describe", () -> {

                        before(() -> innerBeforeCallsCounter++);

                        it("deep inner it", block.apply("deep inner it"));
                        it("last inner it", block.apply("deep inner it"));
                    });
                });
                it("outer it", block.apply("outer it"));
            });
        }
    }


    public static class OleasterTestImplementingTestClass implements OleasterTest {

        public static SuiteBuilder suiteBuilder;

        @Override
        public void buildTestSuite(SuiteBuilder sb) {
            suiteBuilder = sb;
        }
    }

    {
        describe("OleasterSuiteRunner", () -> {

            beforeEach(() -> {
                calls = new ArrayList<>();
                runner = new OleasterSuiteRunner(TestClass.class);
            });

            describe("when suites are obtained from the test class using getChildren", () -> {

                beforeEach(() -> {
                    suites = runner.getChildren();
                });

                it("returns a list with a single Suite", () -> {
                    List specNames = suites.stream().map(Suite::getDescription).collect(Collectors.toList());
                    assertEquals(Arrays.asList("outer describe"), specNames);
                });

                describe("when the test class implements OleasterTest", () -> {

                    beforeEach(() -> {
                        OleasterTestImplementingTestClass.suiteBuilder = null;
                        runner = new OleasterSuiteRunner(OleasterTestImplementingTestClass.class);
                        runner.getChildren();
                    });

                    it("calls buildTestSuite() and passes a SuiteBuilder instance", () -> {
                        assertNotNull(OleasterTestImplementingTestClass.suiteBuilder);
                    });

                });

            });

            describe("When build a description", () -> {

                beforeEach(() -> {
                    runner.getChildren();
                    description = runner.getDescription();
                });

                describe("Given the main description", () -> {

                    beforeEach(() -> {
                        children = description.getChildren();
                    });

                    it("should use class name as displayName", () -> {
                        assertEquals(TestClass.class.getName(), description.getDisplayName());
                    });

                    it("should have 1 child", () -> {
                        assertEquals(1, children.size());
                    });

                    describe("given the first child", () -> {

                        beforeEach(() -> {
                            child = children.get(0);
                            children = child.getChildren();
                        });

                        it("should have 'outer describe' as description", () -> {
                            assertEquals("outer describe", child.getDisplayName());
                        });

                        it("should have 2 childs", () -> {
                            assertEquals(2, child.getChildren().size());
                        });

                        describeItBlock(0, "outer it");

                        describe("given the second child", () -> {

                            beforeEach(() -> {
                                child = children.get(1);
                                children = child.getChildren();
                            });

                            it("should have 'inner describe' as description", () -> {
                                assertEquals("outer describe.inner describe", child.getDisplayName());
                            });

                            describeItBlock(0, "inner it");

                            describe("given second child", () -> {

                                beforeEach(() -> {
                                    child = children.get(1);
                                    children = child.getChildren();
                                });

                                it("should have 'inner describe' as description", () -> {
                                    assertEquals("outer describe.inner describe.inner describe", child.getDisplayName());
                                });

                                it("should have 2 childs", () -> {
                                    assertEquals(2, child.getChildren().size());
                                });

                                describeItBlock(0, "deep inner it");
                                describeItBlock(1, "last inner it");

                            });

                        });

                    });

                });

            });

            describe("When have beforeEach blocks", () -> {

                beforeEach(() -> {
                    beforeCallsCounter = 0;
                    beforeEachCallsCounter = 0;
                    runner.run(mock(RunNotifier.class));
                });

                it("should call beforeEach once for each it child", () -> {
                    assertEquals(3, beforeEachCallsCounter);
                });

                it("should call before once for each it child", () -> {
                    assertEquals(3, beforeCallsCounter);
                });

                it("should call innerBefore once for each it child", () -> {
                    assertEquals(3, innerBeforeCallsCounter);
                });


            });

        });

    }

    private void describeItBlock(int index, String itDescription) {
        describe("given the child at " + index, () -> {

            beforeEach(() -> {
                child = children.get(index);
            });

            it("should have no child", () -> {
                assertEquals(0, child.getChildren().size());
            });

            it("should have '" + itDescription + "' as description", () -> {
                assertEquals("It " + itDescription, child.getMethodName());
            });

        });
    }
}