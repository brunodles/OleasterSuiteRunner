# Oleaster SuiteRunner
A jUnit test runner to format Oleaster tests as a suite.

## What is Oleaster?
Oleaster is a java testing library inspired by Jasmine.
This lib enables you to write tests in BDD style.

## Why do I need this lib?
You don't need, this suite runner is just a formatter for the tests and you can continue using the Oleaster.

## What this lib does?

The default Oleaster runner prints whole strings for each `it` block, something like:
```
* Given a Object, when I do something, with a valid parameter, it should return true
* Given a Object, when I do something, with a valid parameter, it should not hit the api
* Given a Object, when I do something, with an invalid parameter, it should return false
* Given a Object, when I do something, with an invalid parameter, it should hit the api
```

This lib just format the test output as a suite, so the result will be as follows:
```
Given a Object
  when I do something
    with a valid parameter
      it should return true
      it should not hit the api
    with an invalid parameter
      it should return false
      it should hit the api
```

## How to import?
```gradle
  dependency {
    testCompile 'com.mscharhag.oleaster:oleaster-runner:0.1.2'
    testCompile 'com.brunodles:oleaster-suiterunner:0.1.1'
  }
```

## Sources

* [Oleaster](https://github.com/mscharhag/oleaster)
* [GivenWhenThen](https://martinfowler.com/bliki/GivenWhenThen.html)
* [Definition Given-When-Then](https://www.agilealliance.org/glossary/gwt/)
* [Understanding junits runner architecture](http://www.mscharhag.com/java/understanding-junits-runner-architecture)
