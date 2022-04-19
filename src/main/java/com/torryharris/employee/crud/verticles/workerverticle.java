package com.torryharris.employee.crud.verticles;

import com.torryharris.employee.crud.dao.Dao;
import com.torryharris.employee.crud.dao.impl.EmployeeJdbcDao;
import com.torryharris.employee.crud.model.Employee;
import com.torryharris.employee.crud.model.Response;
import com.torryharris.employee.crud.util.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class workerverticle extends AbstractVerticle {
  private static final Logger LOGGER = LogManager.getLogger(workerverticle.class);
  private final Dao<Employee> employeeDao;
  private static Utils utils;

  public workerverticle(Vertx vertx) {
    employeeDao = new EmployeeJdbcDao(vertx);
  }

  @Override
  public void start() {
    //thread blocking example
//    vertx.setPeriodic(100, id -> {
//      try {
//        LOGGER.info("hello");
//        Thread.sleep(3000);
//        LOGGER.info("oops");
//        vertx.setPeriodic(2000, ne -> {
//          vertx.close();
//        });
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    });
//  }
//
//  public static void main(String[] args) {
//    Vertx vertx = Vertx.vertx();
//    DeploymentOptions deploymentOptions = new DeploymentOptions()
//      .setInstances(4)
//      .setWorker(true);
//    LOGGER.info(Utils.getconnection());
//    vertx.deployVerticle("Jdbcplain", deploymentOptions);

    //get by id using jdbc and worker verticle
    vertx.eventBus().consumer("getconnection", message -> {
      LOGGER.info("This is worker-thread");
      String id = (String) message.body();
      LOGGER.info(id);
      Response response = new Response();
      Utils.getconnection(Long.parseLong(id));
      response.setStatusCode(200);
    });

    // execute blocking

    vertx.executeBlocking(future-> {
      try {
        Thread.sleep(5);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String res = "hello";
      future.complete(res);
    },result->{
      if(result.succeeded()){
        System.out.println(result.result());
      }else {
        result.cause().printStackTrace();
      }
    });
  }

}

