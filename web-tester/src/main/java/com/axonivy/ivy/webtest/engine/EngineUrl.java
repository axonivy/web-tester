/*
 * Copyright (C) 2021 Axon Ivy AG
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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * This is a Util to build URLs against the designer (localhost:8081) or a test
 * engine.
 *
 * To run a test engine have a look at the project-build-plugin:
 * https://github.com/axonivy/project-build-plugin
 */
@SuppressWarnings("hiding")
public class EngineUrl {
  public enum SERVLET {
    PROCESS("pro"), REST("api"), WEBSERVICE("ws"), STATIC_VIEW("faces/view"), CASEMAP("casemap");

    final String path;

    SERVLET(String path) {
      this.path = path;
    }
  }

  public static final String TEST_ENGINE_APP = "test.engine.app";
  public static final String TEST_ENGINE_URL = BaseEngineUrl.TEST_ENGINE_URL;
  public static final String DESIGNER = "designer";

  private String base;
  private String app;
  private SERVLET servlet;
  private String path = "";
  private final List<QueryParam> queryParams = new ArrayList<>();

  private EngineUrl() {
    this.base = base();
    this.app = applicationName();
  }

  /**
   * Gets a builder for an engine URL. Returns the URL builder for a test engine
   * started by the project-build-plugin. Set the different values
   * base/app/servlet/path over their methods and call {@link #toUrl()} to get
   * your created URL.
   * <p>
   * Default values:
   * </p>
   * <ul>
   * <li>base: value of the system property {@value #TEST_ENGINE_URL} or
   * 'http://localhost:8080/'</li>
   * <li>app: value of the system property {@value #TEST_ENGINE_APP} or
   * {@value #DESIGNER}</li>
   * </ul>
   * @return engine url build
   */
  public static EngineUrl create() {
    return new EngineUrl();
  }

  public static String createProcessUrl(String path) {
    var parts = splitQueryFromPath(path);
    return create().process(parts.path).toUrl() + parts.query;
  }

  public static String createRestUrl(String path) {
    var parts = splitQueryFromPath(path);
    return create().rest(parts.path).toUrl() + parts.query;
  }

  public static String createWebServiceUrl(String path) {
    var parts = splitQueryFromPath(path);
    return create().webService(parts.path).toUrl() + parts.query;
  }

  public static String createStaticViewUrl(String path) {
    var parts = splitQueryFromPath(path);
    return create().staticView(parts.path).toUrl() + parts.query;
  }

  public static String createCaseMapUrl(String path) {
    var parts = splitQueryFromPath(path);
    return create().caseMap(parts.path).toUrl() + parts.query;
  }

  public EngineUrl base(String base) {
    this.base = base;
    return this;
  }

  public EngineUrl app(String app) {
    this.app = app;
    return this;
  }

  public EngineUrl process(String path) {
    return this.servlet(SERVLET.PROCESS).path(path);
  }

  public EngineUrl rest(String path) {
    return this.servlet(SERVLET.REST).path(path);
  }

  public EngineUrl webService(String path) {
    return this.servlet(SERVLET.WEBSERVICE).path(path);
  }

  public EngineUrl staticView(String path) {
    return this.servlet(SERVLET.STATIC_VIEW).path(path);
  }

  public EngineUrl caseMap(String path) {
    return this.servlet(SERVLET.CASEMAP).path(path);
  }

  public EngineUrl servlet(SERVLET servlet) {
    this.servlet = servlet;
    return this;
  }

  public EngineUrl path(String path) {
    if (Strings.CS.contains(path, "?")) {
      throw new IllegalArgumentException("Adding query parameters via the path method will not work, please use the queryParam method or encode the '?' with '%3F'.");
    }
    this.path = path;
    return this;
  }

  public EngineUrl queryParam(String key, String value) {
    this.queryParams.add(new QueryParam(key, value));
    return this;
  }

  public String toUrl() {
    return builder().toString();
  }

  UriBuilder builder() {
    var builder = UriBuilder.fromUri(base)
        .path(app)
        .path(getServletPath())
        .path(path);
    for (var queryParam : queryParams) {
      builder = builder.queryParam(queryParam.key, queryParam.value);
    }
    return builder;
  }

  private String getServletPath() {
    if (servlet != null) {
      return servlet.path;
    }
    return "";
  }

  private static PathAndQuery splitQueryFromPath(String pathWithQuery) {
    if (pathWithQuery == null) {
      return new PathAndQuery("", "");
    }
    var path = StringUtils.substringBefore(pathWithQuery, "?");
    var query = StringUtils.substringAfter(pathWithQuery, "?");
    if (query.length() > 0) {
      query = "?" + query;
    }
    return new PathAndQuery(path, query);
  }

  /**
   * Gets base URL of a running engine. Returns URL of started
   * project-build-plugin test engine ({@value #TEST_ENGINE_URL}) or
   * 'http://localhost:8080/ivy/'
   * @return URL of engine
   */
  public static String base() {
    return BaseEngineUrl.url();
  }

  /**
   * Gets base URL of a rest request.
   * @deprecated use {@link #createRestUrl(String)} instead.
   * @return rest base URL
   */
  @Deprecated
  public static String rest() {
    return createRestUrl("");
  }

  /**
   * Gets base URL of a webservice request.
   * @deprecated use {@link #createWebServiceUrl(String)} instead.
   * @return soap base URL
   */
  @Deprecated
  public static String soap() {
    return createWebServiceUrl("");
  }

  /**
   * Gets base URL of a process.
   * @deprecated use {@link #createProcessUrl(String)} instead.
   * @return process base URL
   */
  @Deprecated
  public static String process() {
    return createProcessUrl("");
  }

  /**
   * Gets base URL of a static page.
   * @deprecated use {@link #createStaticViewUrl(String)} instead.
   * @return static page base URL
   */
  @Deprecated
  public static String staticView() {
    return createStaticViewUrl("");
  }

  /**
   * Gets base URL of a given servlet context (like 'pro').
   * @deprecated use {@link #create()} builder instead.
   * @param servletContext identifier of the servlet
   * @return servlet base URL
   */
  @Deprecated
  public static String getServletUrl(String servletContext) {
    return create().path(servletContext).toUrl();
  }

  /**
   * Gets name of set application ({@value #TEST_ENGINE_APP}).
   * @return application name
   */
  public static String applicationName() {
    return System.getProperty(TEST_ENGINE_APP, DESIGNER);
  }

  /**
   * Check if the set application is the designer.
   * @return true if application is designer
   */
  public static Boolean isDesigner() {
    return Boolean.valueOf(applicationName() == DESIGNER);
  }

  private static record QueryParam(String key, String value) {}

  private static record PathAndQuery(String path, String query) {}
}
