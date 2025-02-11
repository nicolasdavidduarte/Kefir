package org.kefir;

import org.hibernate.cfg.Configuration;
import java.io.File;

public class TestHibernateConfig {

    public static void main(String[] args) {
        try {
            File configFile = new File("/Users/nicolasduarte/IdeaProjects/Kefir/build/resources/main/hibernate.cfg.xml");
            Configuration configuration = new Configuration().configure(configFile);

            System.out.println("Hibernate configuration successfully loaded!");
        } catch (Exception ex) {
            System.err.println("Failed to load Hibernate configuration.");
            ex.printStackTrace();
        }
    }
}