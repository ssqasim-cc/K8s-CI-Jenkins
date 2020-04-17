package io.digital.supercharger.integration;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digital.supercharger.IntegrationTest;
import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.exception.ApiError;
import io.digital.supercharger.TestHelper;
import io.digital.supercharger.util.JsonUtil;
import junit.framework.TestCase;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Category(IntegrationTest.class)
@TestPropertySource("classpath:application.properties")
public class DemoIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void requestNoTokenForbidden() {
        ResponseEntity<ApiError> response = restTemplate.exchange(
            createURLWithPort(TestHelper.DEMO_URL + "/999"),
            HttpMethod.GET,
            null, ApiError.class);

        ApiError apiError = response.getBody();

        assertNotNull(apiError);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("missing access token", apiError.getMessage());
    }

    @Test
    public void createAndRetrieveTests() throws JsonProcessingException {
        List<DemoData> listOfDemoData = new ArrayList<>(2);
        listOfDemoData.add(new DemoData(1L, "Test 1"));
        listOfDemoData.add(new DemoData(2L, "Test 2"));

        createDemoTest(listOfDemoData);
        retrieveTests(listOfDemoData);
    }

    private void retrieveTests(List<DemoData> demoData) throws JsonProcessingException {
        ResponseEntity<List<DemoData>> response = restTemplate.exchange(
            createURLWithPort(TestHelper.DEMO_URL),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<DemoData>>() {
            });
        List<DemoData> list = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(list);
        assertEquals(demoData.size(), list.size());

        TestCase.assertEquals(JsonUtil.toJson(demoData.stream().sorted()), JsonUtil.toJson(list.stream().sorted()));
    }

    private void createDemoTest(List<DemoData> demoData) {
        headers.add(HttpHeader.CONTENT_TYPE.asString(), "application/json");
        demoData.forEach(demo -> {
            HttpEntity<DemoData> entity = new HttpEntity<>(demo, headers);
            ResponseEntity<DemoData> response = restTemplate.exchange(
                createURLWithPort(TestHelper.DEMO_URL), HttpMethod.POST, entity, DemoData.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(demo.getId(), response.getBody().getId());
            assertEquals(demo.getName(), response.getBody().getName());
        });
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
