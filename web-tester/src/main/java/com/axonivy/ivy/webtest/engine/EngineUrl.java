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
  static final String TEST_ENGINE_APP = "test.engine.app";
  static final String TEST_ENGINE_URL = "test.engine.url";
  public static final String DESIGNER = "designer";

  public static String base()
  {
    return System.getProperty(TEST_ENGINE_URL, "http://localhost:8081/ivy/");
  }

  public static String rest()
  {
    return getServletUrl("api");
  }
  
  public static String soap()
  {
    return getServletUrl("ws");
  }

  public static String process()
  {
    return getServletUrl("pro");
  }
  
  public static String staticView()
  {
    return getServletUrl("faces/view");
  }

  public static String getServletUrl(String servletContext)
  {
    return base() + servletContext + "/" + applicationName();
  }

  public static String applicationName()
  {
    return System.getProperty(TEST_ENGINE_APP, DESIGNER);
  }

  public static Boolean isDesigner()
  {
    return Boolean.valueOf(applicationName() == DESIGNER);
  }
}
