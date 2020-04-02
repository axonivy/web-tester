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

import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a Util to build URLs against the designer (localhost:8081) or a test engine.
 * 
 * To run a test engine have a look at the project-build-plugin: https://github.com/axonivy/project-build-plugin
 */
public class EngineUrl
{
  public static final String SLASH = "/";
  public static final String SERVLET_STATIC_VIEW = "faces/view";
  public static final String SERVLET_PROCESS = "pro";
  public static final String SERVLET_SOAP = "ws";
  public static final String SERVLET_REST = "api";
  public static final String TEST_ENGINE_APP = "test.engine.app";
  public static final String TEST_ENGINE_URL = "test.engine.url";
  public static final String DESIGNER = "designer";
  
  private String base;
  private String app;
  private String servlet;
  private String path;
  
  private EngineUrl()
  {
    this.base = base();
    this.app = applicationName();
  }
  
  public static EngineUrl create()
  {
    return new EngineUrl();
  }
  
  public static String createProcess(String path)
  {
    return create().servlet(SERVLET_PROCESS).path(path).toUrl();
  }
  
  public static String createRest(String path)
  {
    return create().servlet(SERVLET_REST).path(path).toUrl();
  }
  
  public static String createSoap(String path)
  {
    return create().servlet(SERVLET_SOAP).path(path).toUrl();
  }
  
  public static String createStaticView(String path)
  {
    return create().servlet(SERVLET_STATIC_VIEW).path(path).toUrl();
  }
  
  public EngineUrl base(@SuppressWarnings("hiding") String base)
  {
    this.base = base;
    return this;
  }
  
  public EngineUrl app(@SuppressWarnings("hiding") String app)
  {
    this.app = app;
    return this;
  }
  
  public EngineUrl servlet(@SuppressWarnings("hiding") String servlet)
  {
    this.servlet = servlet;
    return this;
  }
  
  public EngineUrl path(@SuppressWarnings("hiding") String path)
  {
    this.path = path;
    return this;
  }
  
  public String toUrl()
  {
    return new StringJoiner(SLASH)
            .add(trim(base))
            .add(trim(app))
            .add(trim(servlet))
            .add(trim(path))
            .toString();
  }

  private static String trim(String base)
  {
    return StringUtils.removeStart(StringUtils.removeEnd(base, SLASH), SLASH);
  }
  
  /**
   * Gets base URL of a running engine.
   * Returns URL of started project-build-plugin test engine ({@value #TEST_ENGINE_URL}) or 'http://localhost:8080/ivy/'
   * @return URL of engine
   */
  public static String base()
  {
    return System.getProperty(TEST_ENGINE_URL, "http://localhost:8081/");
  }

  /**
   * Gets base URL of a rest request.
   * @deprecated use {@link #createRest(String)} instead.
   * @return rest base URL
   */
  @Deprecated
  public static String rest()
  {
    return createRest("");
  }
  
  /**
   * Gets base URL of a webservice request.
   * @deprecated use {@link #createSoap(String)} instead.
   * @return soap base URL
   */
  @Deprecated
  public static String soap()
  {
    return createSoap("");
  }

  /**
   * Gets base URL of a process.
   * @deprecated use {@link #createProcess(String)} instead.
   * @return process base URL
   */
  @Deprecated
  public static String process()
  {
    return createProcess("");
  }
  
  /**
   * Gets base URL of a static page.
   * @deprecated use {@link #createStaticView(String)} instead.
   * @return static page base URL
   */
  @Deprecated
  public static String staticView()
  {
    return createStaticView("");
  }

  /**
   * Gets base URL of a given servlet context (like 'pro').
   * @deprecated use {@link #create()} builder instead.
   * @param servletContext identifier of the servlet
   * @return servlet base URL
   */
  @Deprecated
  public static String getServletUrl(String servletContext)
  {
    return create().servlet(servletContext).toUrl();
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
