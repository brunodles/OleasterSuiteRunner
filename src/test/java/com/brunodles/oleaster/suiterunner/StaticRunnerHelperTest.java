package com.brunodles.oleaster.suiterunner;

import com.brunodles.oleaster.StaticRunnerHelper;
import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner;
import com.mscharhag.oleaster.runner.suite.Suite;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.List;

import static com.brunodles.oleaster.StaticRunnerHelper.*;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(OleasterSuiteRunner.class)
public class StaticRunnerHelperTest {

    private OleasterSuiteRunner runner;

    private List<Suite> children;

    {
        given(OleasterSuiteRunner.class, () -> {
            beforeEach(() -> {
                runner = new OleasterSuiteRunner(TestClass.class);
            });
            when("getChildren", () -> {
                beforeEach(() -> {
                    children = runner.getChildren();
                });
                it("should return a list with 6 items", () -> {
                    assertEquals(6, children.size());
                });
                itContains("Given String");
                itContains("Given a object");
                itContains("Context do something");
                itContains("When do something");
                itContains("On do something");
                itContains("With a parameter");
            });
        });
    }

    private void itContains(String s) {
        it("should contain a block with '" + s + "'", () -> contains(children, s));
    }

    private static void contains(List<Suite> children, String description) {
        for (Suite child : children)
            if (description.equals(child.getDescription()))
                return;

        fail("Didn't found a suite with description '" + description + "'");
    }

    public static class TestClass {
        {
            given(String.class, () -> {});
            given("a object", () -> {});
            context("do something", () -> {});
            when("do something", () -> {});
            on("do something", () -> {});
            with("a parameter", () -> {});
            then("should do something", () -> {});

            xgiven(Boolean.class, () -> {});
            xgiven("Another object", () -> {});
            xcontext("do something", () -> {});
            xwhen("do something", () -> {});
            xon("do something", () -> {});
            xwith("a parameter", () -> {});
            xthen("should do something else", () -> {});

            xdescribe("a text", () -> {});
            xit("should something", () -> {});
        }
    }
}