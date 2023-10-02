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

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import com.codeborne.selenide.Configuration;

/**
 * <p>Provide the base url of the ivy test engine.</p>
 * <p>If selenium driver is remote then the host name of the configured ivy test engine is replaced
 * if it is localhost, 127.0.0.1 or [::01] to the real network name of the local host.
 * This should ensure that the selenium driver still can access the ivy test engine that is running on local host.</p>
 * @author rwei
 * @since Jan 5, 2022
 */
class BaseEngineUrl {

  static final String TEST_ENGINE_URL = "test.engine.url";

  static String url() {
    return new BaseEngineUrl().evaluate();
  }

  private String evaluate() {
    var engineUrl = System.getProperty(TEST_ENGINE_URL, "http://localhost:8081/");
    try {
      return evaluate(engineUrl);
    } catch(Exception ex) {
      return engineUrl;
    }
  }

  private String evaluate(String engine) throws URISyntaxException, UnknownHostException {
    var engineUri = new URI(engine);
    if (seleniumRunsOnDifferentHost(engineUri) &&
        isLocalHost(engineUri)) {
      // replace localhost with real network name so that the remote selenium driver
      // still can access the test engine
      String hostName = InetAddress.getLocalHost().getHostName();
      engineUri = replaceHost(engineUri, hostName);
    }
    return engineUri.toString();
  }

  private boolean isLocalHost(URI engineUri) {
    String host = engineUri.getHost();
    return "localhost".equalsIgnoreCase(host) ||
           "127.0.0.1".equals(host) ||
           "[::1]".equals(host);
  }

  private boolean seleniumRunsOnDifferentHost(URI engineUri) throws URISyntaxException {
    if (Configuration.remote == null) {
      // selenium driver does not run on remote host
      return false;
    }
    URI remoteUri = new URI(Configuration.remote);
    var remoteHost = remoteUri.getHost();
    return remoteHost != null &&
           !remoteHost.equalsIgnoreCase(engineUri.getHost());
  }

  private URI replaceHost(URI engineUri, String hostName) throws URISyntaxException {
    return new URI(
            engineUri.getScheme(),
            engineUri.getUserInfo(),
            hostName,
            engineUri.getPort(),
            engineUri.getPath(),
            engineUri.getQuery(),
            engineUri.getFragment());
  }
}
