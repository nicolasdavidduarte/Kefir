package org.kefir;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class TestJAXB {

    public static void main(String[] args) {
        try {
            // Try to create a JAXB context
            JAXBContext jaxbContext = JAXBContext.newInstance(String.class);
            System.out.println("JAXB context was created successfully!");

        } catch (JAXBException e) {
            System.err.println("Failed to create JAXB context!");
            e.printStackTrace();
        }
    }
}