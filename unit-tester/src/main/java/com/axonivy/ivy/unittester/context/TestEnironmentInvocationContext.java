package com.axonivy.ivy.unittester.context;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

public class TestEnironmentInvocationContext implements TestTemplateInvocationContext {
  private String contextDisplayName;

  public TestEnironmentInvocationContext(String contextDisplayName) {
    super();
    this.contextDisplayName = contextDisplayName;
  }

  @Override
  public String getDisplayName(int invocationIndex) {
    return contextDisplayName;
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return Collections.singletonList(new ParameterResolver() {

      @Override
      public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
          throws ParameterResolutionException {
        return extensionContext;
      }

      @Override
      public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
          throws ParameterResolutionException {
        return ExtensionContext.class == parameterContext.getParameter().getType();
      }
    });
  }
}
