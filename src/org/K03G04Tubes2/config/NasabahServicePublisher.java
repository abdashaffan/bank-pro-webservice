package org.K03G04Tubes2.config;

import org.K03G04Tubes2.NasabahServiceImpl;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;


@WebService
public class NasabahServicePublisher {

    public NasabahServicePublisher() {}

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1234/bankpro", new NasabahServiceImpl());
    }
}

