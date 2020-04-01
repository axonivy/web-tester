/*
 * Copyright (C) 2020 AXON IVY AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.axonivy.ivy.webtest.engine;

/**
 * This is a Util to build URLs against the designer (localhost:8081) or a test engine.
 * 
 * To run a test engine have a look at the project-build-plugin: https://github.com/axonivy/project-build-plugin
 */
public class EngineUrl
{
  public static final String TEST_ENGINE_APP = "test.engine.app";
  public static final String TEST_ENGINE_URL = "test.engine.url";
  public static final String DESIGNER = "designer";

  /**
   * Gets base URL of a running engine.
   * Returns URL of started project-build-plugin test engine ({@value #TEST_ENGINE_URL}) or 'http://localhost:8080/ivy/'
   * @return URL of engine
   */
  public static String base()
  {
    return System.getProperty(TEST_ENGINE_URL, "http://localhost:8081/ivy/");
  }

  /**
   * Gets base URL of a rest request.
   * @return rest base URL
   */
  public static String rest()
  {
    return getServletUrl("api");
  }
  
  /**
   * Gets base URL of a webservice request.
   * @return soap base URL
   */
  public static String soap()
  {
    return getServletUrl("ws");
  }

  /**
   * Gets base URL of a process.
   * @return process base URL
   */
  public static String process()
  {
    return getServletUrl("pro");
  }
  
  /**
   * Gets base URL of a static page.
   * @return static page base URL
   */
  public static String staticView()
  {
    return getServletUrl("faces/view");
  }

  /**
   * Gets base URL of a given servlet context (like 'pro').
   * @param servletContext identifier of the servlet
   * @return servlet base URL
   */
  public static String getServletUrl(String servletContext)
  {
    return base() + servletContext + "/" + applicationName();
  }

  /**
   * Gets name of set application ({@value #TEST_ENGINE_APP}).
   * @return application name
   */
  public static String applicationName()
  {
    return System.getProperty(TEST_ENGINE_APP, DESIGNER);
  }

  /**
   * Check if the set application is the designer.
   * @return true if application is designer
   */
  public static Boolean isDesigner()
  {
    return Boolean.valueOf(applicationName() == DESIGNER);
  }
}
