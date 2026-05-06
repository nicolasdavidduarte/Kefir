package com.kefir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class Kefir {
  public static void main(String[] args) {

    // Get the session factory from HibernateUtil
    SpringApplication.run(Kefir.class, args);
  }
}
