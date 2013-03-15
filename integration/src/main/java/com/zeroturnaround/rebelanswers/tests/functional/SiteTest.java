package com.zeroturnaround.rebelanswers.tests.functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SiteTest {

  private WebDriver driver;

  @Before
  public void setUp() throws MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setBrowserName("chrome");
    driver = new RemoteWebDriver(new URL("http://10.127.128.2:4444/wd/hub"), capabilities);
  }

  @After
  public void tearDown() {
    driver.quit();
    driver = null;
  }

  @Test
  public void testIndex() {
    driver.get("http://10.127.128.2/lr-demo-answers-java");
    assertEquals(driver.getTitle(), "A wrong title");
  }
}