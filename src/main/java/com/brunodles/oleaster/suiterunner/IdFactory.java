package com.brunodles.oleaster.suiterunner;

import java.io.Serializable;

/**
 * Created by Bruno de Lima on 11/03/16.
 */
class IdFactory {

    long id;

    Serializable nextId() {
        return id++;
    }
}
