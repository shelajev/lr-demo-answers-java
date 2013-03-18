package com.zeroturnaround.rebelanswers.mvc.tools;

import javax.servlet.ServletContextEvent;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DriverCleanup implements javax.servlet.ServletContextListener {

  // On application shutdown
  public void contextDestroyed(ServletContextEvent event) {
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    for (; drivers.hasMoreElements(); ) {
      Driver driver = drivers.nextElement();
      // We search for driver that was loaded by this web application
      if (driver.getClass().getClassLoader() == this.getClass().getClassLoader()) {
        try {
          DriverManager.deregisterDriver(driver);
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void contextInitialized(ServletContextEvent event) {
    // Nothing to do here
  }
}