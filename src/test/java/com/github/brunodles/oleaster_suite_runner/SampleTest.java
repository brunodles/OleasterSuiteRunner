package com.github.brunodles.oleaster_suite_runner;

import com.mscharhag.oleaster.runner.Invokable;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.beforeEach;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

/**
 * Created by bruno on 11/03/16.
 */
@RunWith(OleasterSuiteRunner.class)
public class SampleTest {

    private static List<String> calls = new ArrayList<>();
    private static Function<String, Invokable> block = (String name) -> () -> calls.add(name);

    {
        describe("outer describe", () -> {
//            beforeEach(() -> beforeCallsCounter++);
            describe("inner describe", () -> {
                it("inner it", block.apply("inner it"));

                describe("inner describe", () -> {
                    it("inner it", block.apply("inner inner it"));
                });
            });
            it("outer it", block.apply("outer it"));
        });
    }
}
