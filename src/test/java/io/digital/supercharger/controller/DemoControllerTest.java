package io.digital.supercharger.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.interceptor.security.AuthenticationInterceptor;
import io.digital.supercharger.TestHelper;
import io.digital.supercharger.exception.MicroserviceRestExceptionHandler;
import io.digital.supercharger.exception.PermissionException;
import io.digital.supercharger.service.DemoService;
import io.digital.supercharger.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DemoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DemoService demoService;

    @Mock
    AuthenticationInterceptor interceptor;

    @Before
    public void setUp() throws Exception {
        when(interceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any())).thenReturn(true);

        this.mockMvc = standaloneSetup(new DemoController(demoService))
            .setControllerAdvice(new MicroserviceRestExceptionHandler())
            .addInterceptors(interceptor)
            .build();
    }

    @Test
    public void getAll() throws Exception {
        List<DemoData> itemDtoList = new ArrayList<>(2);
        itemDtoList.add(new DemoData());
        itemDtoList.add(new DemoData());
        when(demoService.findAll()).thenReturn(itemDtoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(TestHelper.DEMO_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void saveDemo() throws Exception {
        DemoData demo = new DemoData(1L, "test");
        when(demoService.save(any(DemoData.class))).thenReturn(demo);
        this.mockMvc.perform(post(TestHelper.DEMO_URL)
            .content(JsonUtil.toJson(demo))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        verify(demoService, times(1)).save(any(DemoData.class));
    }

    @Test
    public void getDemoById() throws Exception {
        when(demoService.findById(1L)).thenReturn(new DemoData(1L, "test"));
        this.mockMvc.perform(get(TestHelper.DEMO_URL + "/" + 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void handleEntityNotFoundErrorResponse() throws Exception {
        when(demoService.findById(1L)).thenThrow(new EntityNotFoundException());
        this.mockMvc.perform(get(TestHelper.DEMO_URL + "/" + 1))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void handleAuthenticationErrorResponse() throws Exception {

        when(interceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
            .thenThrow(new PermissionException("Unauthorized"));
        this.mockMvc.perform(get(TestHelper.DEMO_URL + "/" + 1))
            .andExpect(status().isForbidden());
    }

    @Test
    public void handleRuntimeExceptionErrorResponse() throws Exception {
        when(demoService.findById(1L)).thenThrow(new IllegalArgumentException());
        when(interceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any())).thenReturn(true);
        this.mockMvc.perform(get(TestHelper.DEMO_URL + "/" + 1))
            .andExpect(status().is4xxClientError());
    }
}
