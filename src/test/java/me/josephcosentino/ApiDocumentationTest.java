package me.josephcosentino;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
// Override properties for Heroku Deployment
@DirtiesContext
@SpringBootTest(properties = {
        "SPRING_JPA_DATABASE-PLATFORM=",
        "SPRING_DATASOURCE_URL=",
        "SPRING_DATASOURCE_USERNAME=",
        "SPRING_DATASOURCE_DRIVER-CLASS-NAME=",
        "SPRING_DATASOURCE_PASSWORD="
})
@WithMockUser(roles = "ADMIN")
public class ApiDocumentationTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final FieldDescriptor[] user = new FieldDescriptor[] {
            fieldWithPath("username").description("The user's unique name"),
            fieldWithPath("id").description("The user's id"),
            fieldWithPath("roles").description("The user's security roles"),
            fieldWithPath("groups").description("The user's groups.")
    };

    private final FieldDescriptor[] group = new FieldDescriptor[] {
            fieldWithPath("id").description("The groups's id"),
            fieldWithPath("name").description("The groups's name")
    };

    private final FieldDescriptor[] role = new FieldDescriptor[] {
            fieldWithPath("id").description("The role's id"),
            fieldWithPath("value").description("The role's name")
    };

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void users() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/users?page=0&size=50")
                        .header("Authorization", "Basic am9lOnB3ZA==")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-findAll",
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic auth credentials"),
                                        headerWithName("Accept").description("Acceptable response media type")),
                                requestParameters(
                                        parameterWithName("page").description("The page to retrieve"),
                                        parameterWithName("size").description("Entries per page")),
                                responseFields(
                                        fieldWithPath("[]").description("Array of users"))
                                        .andWithPrefix("[].", user)
                                        .andWithPrefix("[].groups[].", group)
                                        .andWithPrefix("[].roles[].", role)));
    }

}
