package me.josephcosentino;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
// Override properties for Heroku Deployment
@SpringBootTest(properties = {
        "SPRING_JPA_DATABASE-PLATFORM=",
        "SPRING_DATASOURCE_URL=",
        "SPRING_DATASOURCE_USERNAME=",
        "SPRING_DATASOURCE_DRIVER-CLASS-NAME=",
        "SPRING_DATASOURCE_PASSWORD="
})
@DirtiesContext
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
                .apply(documentationConfiguration(this.restDocumentation).uris()
                        .withScheme("https")
                        .withHost("meals-api.josephcosentino.me")
                        .withPort(443)).build();
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void userFindAll() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/users?page=0&size=50")
                        .header("Authorization", "Basic dXNlcjpwd2Q")
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

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void userFindById() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/users/{user_id}", 1L)
                        .header("Authorization", "Basic dXNlcjpwd2Q")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-findById",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"),
                                headerWithName("Accept").description("Acceptable response media type")),
                        pathParameters(
                                parameterWithName("user_id").description("Id of user to retrieve")),
                        responseFields(user)
                                .andWithPrefix("groups[].", group)
                                .andWithPrefix("roles[].", role)));
    }

    @Test
    public void userClearCache() throws Exception {
        this.mockMvc.perform(
                delete("/api/v1/users/cache")
                        .header("Authorization", "Basic dXNlcjpwd2Q"))
                .andExpect(status().isOk())
                .andDo(document("user-clearCache",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"))));
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void userClearCacheById() throws Exception {
        this.mockMvc.perform(
                delete("/api/v1/users/{user_id}/cache", 1L)
                        .header("Authorization", "Basic dXNlcjpwd2Q"))
                .andExpect(status().isOk())
                .andDo(document("user-clearCacheById",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("user_id").description("Id of user to remove from cache")),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"))));
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void groupFindAll() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/groups?page=0&size=50")
                        .header("Authorization", "Basic dXNlcjpwd2Q")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("group-findAll",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"),
                                headerWithName("Accept").description("Acceptable response media type")),
                        requestParameters(
                                parameterWithName("page").description("The page to retrieve"),
                                parameterWithName("size").description("Entries per page")),
                        responseFields(
                                fieldWithPath("[]").description("Array of groups"))
                                .andWithPrefix("[].", group)));
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void groupFindById() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/groups/{group_id}", 1L)
                        .header("Authorization", "Basic dXNlcjpwd2Q")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("group-findById",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"),
                                headerWithName("Accept").description("Acceptable response media type")),
                        pathParameters(
                                parameterWithName("group_id").description("Id of group to retrieve")),
                        responseFields(group)));
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void groupFindUsers() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/groups/{group_id}/users", 1L)
                        .header("Authorization", "Basic dXNlcjpwd2Q")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("group-findUsers",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"),
                                headerWithName("Accept").description("Acceptable response media type")),
                        pathParameters(
                                parameterWithName("group_id").description("Group id")),
                        responseFields(
                                fieldWithPath("[]").description("Array of users"))
                                .andWithPrefix("[].", user)
                                .andWithPrefix("[].groups[].", group)
                                .andWithPrefix("[].roles[].", role)));
    }

    @Test
    public void groupClearCache() throws Exception {
        this.mockMvc.perform(
                delete("/api/v1/groups/cache")
                        .header("Authorization", "Basic dXNlcjpwd2Q"))
                .andExpect(status().isOk())
                .andDo(document("group-clearCache",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"))));
    }

    @Test
    @Transactional
    @Sql("/load-documentation.sql")
    public void groupClearCacheById() throws Exception {
        this.mockMvc.perform(
                delete("/api/v1/groups/{group_id}/cache", 1L)
                        .header("Authorization", "Basic dXNlcjpwd2Q"))
                .andExpect(status().isOk())
                .andDo(document("group-clearCacheById",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("group_id").description("Id of group to remove from cache")),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic auth credentials"))));
    }

}
