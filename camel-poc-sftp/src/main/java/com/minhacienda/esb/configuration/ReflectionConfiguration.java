package com.minhacienda.esb.configuration;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.ValidationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.UnmarshalException;


@RegisterForReflection(targets = {
    ConnectException.class,
    SQLRecoverableException.class,
    SQLSyntaxErrorException.class,
    SQLException.class,
    NullPointerException.class,
    InvalidFormatException.class,
    UnmarshalException.class,
    JsonParseException.class,
    MarshalException.class,
    XPathExpressionException.class,
    String.class,
    SocketTimeoutException.class,
    java.lang.ClassCastException.class,
    List.class,
    ArrayList.class,
    Map.class,
    SSLPeerUnverifiedException.class,
    UnknownHostException.class,
    NoRouteToHostException.class,
    SocketTimeoutException.class,
    java.lang.Thread.class,
    java.util.concurrent.locks.AbstractQueuedSynchronizer.class,
    ValidationException.class
})
public class ReflectionConfiguration {
}
