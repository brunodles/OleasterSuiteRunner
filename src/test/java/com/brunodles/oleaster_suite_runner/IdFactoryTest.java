package com.brunodles.oleaster_suite_runner;

import org.junit.runner.RunWith;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by bruno on 13/03/16.
 */
@RunWith(OleasterSuiteRunner.class)
public class IdFactoryTest {

    private IdFactory idFactory;


    {
        describe("Given a IdFactory", () -> {

            beforeEach(() -> idFactory = new IdFactory());

            describe("When call nextId", () -> {

                describe("the first call", () -> {
                    it("should return 0", () -> {
                        assertEquals(0L, idFactory.nextId());
                    });
                });

                describe("the second call", () -> {
                    beforeEach(() -> idFactory.nextId());
                    it("should return 1", () -> {
                        assertEquals(1L, idFactory.nextId());
                    });
                });

                describe("after call 10 times", () -> {
                    beforeEach(() -> run(10, () -> idFactory.nextId()));
                    it("should return 10", () -> {
                        assertEquals(10L, idFactory.nextId());
                    });
                });
            });
        });
    }

    private void run(int times, Runnable runnable) {
        for (int i = 0; i < times; i++) runnable.run();
    }
}