package com.axonivy.ivy.unittester.utils;

import com.axonivy.ivy.unittester.constants.UnitTesterConstants;

public class UnitTesterUtils {
  public static void setUpConfigForContext(String contextName, Runnable configureEnvForReal,
      Runnable configureEnvForMock) {
    switch (contextName) {
      case UnitTesterConstants.REAL_CALL_CONTEXT_DISPLAY_NAME:
        configureEnvForReal.run();
        break;
      case UnitTesterConstants.MOCK_SERVER_CONTEXT_DISPLAY_NAME:
        configureEnvForMock.run();
        break;
      default:
        break;
    }
  }
}
