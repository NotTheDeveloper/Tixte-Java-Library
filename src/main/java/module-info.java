/**
 * A wrapper for the Tixte API in Java.
 */
module tixte4j {
    uses org.slf4j.spi.SLF4JServiceProvider;

    requires java.desktop;

    requires jdk.incubator.concurrent;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires okhttp3;
    requires org.slf4j;
    requires org.apache.commons.collections4;

    requires org.jetbrains.annotations;
    requires com.google.errorprone.annotations;

    exports dev.blocky.library.tixte.annotations;

    exports dev.blocky.library.tixte.api;
    exports dev.blocky.library.tixte.api.enums;
    exports dev.blocky.library.tixte.api.exceptions;

    exports dev.blocky.library.tixte.internal.interceptor;
    exports dev.blocky.library.tixte.internal.requests;
    exports dev.blocky.library.tixte.internal.requests.json;
    exports dev.blocky.library.tixte.internal.utils;
    exports dev.blocky.library.tixte.internal.utils.io;
}
