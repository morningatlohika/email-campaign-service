package com.lohika.morning.ecs;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.options;

public class BaseControllerTest extends BaseTest {
  public static final String HAL_JSON_CHARSET_UTF_8 = "application/hal+json;charset=UTF-8";

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  protected MockMvc mockMvc;

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .addFilters(springSecurityFilterChain)
        .apply(documentationConfiguration(this.restDocumentation))
        .defaultRequest(options("/")
            .accept(HAL_JSON_CHARSET_UTF_8)
            .header("Authorization", "Basic dXNlcjpxd2Vhc2Q=")
            .contentType(HAL_JSON_CHARSET_UTF_8))
        .build();
  }
}