package com.axonivy.ivy.webtest;

import java.util.Properties;

class IvyTestRuntimeProps {

  /** keep sync with ivy.core resource
   *  ch.ivyteam.ivy.bpm.exec.client.restricted.IvyTestRuntime.IvyTestRuntimeIO#RESOURCE_NAME */
  private static final String RT_PROPS = "ivyTestRuntime.properties";

  public static void loadToSystem() {
    var rtProps = loadProps();
    rtProps.propertyNames().asIterator().forEachRemaining(it -> {
      var key = (String)it;
      System.setProperty(key, rtProps.getProperty(key));
    });
  }

  public static Properties loadProps() {
    var props = new Properties();
    ClassLoader loader = IvyWebTestExtension.class.getClassLoader();
    try(var in = loader.getResourceAsStream(RT_PROPS)) {
      props.load(in);
    } catch (Exception ex) {
      System.err.println("Failed to read properties " + RT_PROPS);
      ex.printStackTrace();
    }
    return props;
  }

}
