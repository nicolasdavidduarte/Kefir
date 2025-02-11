package org.kefir;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Get the session factory from HibernateUtil

        SpringApplication.run(Main.class, args);

/*
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Open a session
        // Session session = sessionFactory.getCurrentSession();

        // Open a session explicitly instead of using getCurrentSession
        Session session = sessionFactory.openSession();

        // Begin a transaction
        Transaction transaction = session.beginTransaction();

        try {
            // Query the database for all "Testo" objects
            Query<Testo> query = session.createQuery("from Testo", Testo.class);

            // Get the list of "Testo" objects
            List<Testo> results = query.list();

            // Print out the results
            if (results.isEmpty()) {
                System.out.println("No records found.");
            } else {
                for (Testo t : results) {
                    System.out.println("ID: " + t.getId() + ", Name: " + t.getName());
                }
            }

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            // Rollback in case of an error
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }*/
    }
}