package com.torryharris.employee.crud.util;

import com.torryharris.employee.crud.model.Employee;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Utils {
  private static final Logger LOGGER = LogManager.getLogger(Utils.class);

  /**
   * Get an error response object with the given error message
   *
   * @param errorMessage error message to set
   * @return {@link JsonObject}
   */
  public static JsonObject getErrorResponse(String errorMessage) {
    return new JsonObject()
      .put("error", new JsonObject().put("message", errorMessage));
  }

  public static JsonObject getMessage(String Message) {
    return new JsonObject()
      .put("Status", new JsonObject().put("message", Message));
  }


  public static List<Employee> getconnection(Long id) {
    List<Employee> emp = new ArrayList<>();
    Employee employee = new Employee();
    Connection con = null;
    String url = "jdbc:mariadb://localhost/employeedb";
    String username = "root";
    String password = "Maria@123";
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);

      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("select * from employees where id=" + id);

      if (rs.next()) {
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setDesignation(rs.getString("designation"));
        employee.setSalary(rs.getDouble("salary"));
        employee.setUsername(rs.getString("username"));
        employee.setPassword(rs.getString("password"));
        System.out.println(rs.getLong("id"));
        emp.add(employee);
        LOGGER.info(employee);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return emp;
  }
}
