package com.brunodles.oleaster;

import com.mscharhag.oleaster.runner.Invokable;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

@SuppressWarnings("WeakerAccess")
public final class StaticRunnerHelper {

    private StaticRunnerHelper() {
    }

    public static void given(Class aClass, Invokable block) {
        given(aClass.getSimpleName(), block);
    }

    public static void given(String text, Invokable block) {
        describe("Given " + text, block);
    }

    public static void context(String text, Invokable block) {
        describe("Context " + text, block);
    }

    public static void when(String text, Invokable block) {
        describe("When " + text, block);
    }

    public static void on(String text, Invokable block) {
        describe("On " + text, block);
    }

    public static void with(String text, Invokable block) {
        describe("With " + text, block);
    }

    public static void then(String text, Invokable block) {
        it(text, block);
    }

    public static void xgiven(Class aClass, Invokable block) {}

    public static void xgiven(String text, Invokable block) {}

    public static void xcontext(String text, Invokable block) {}

    public static void xwhen(String text, Invokable block) {}

    public static void xon(String text, Invokable block) {}

    public static void xwith(String text, Invokable block) {}

    public static void xthen(String text, Invokable block) {}

    public static void xdescribe(String text, Invokable block) {}

    public static void xit(String text, Invokable block) {}
}
