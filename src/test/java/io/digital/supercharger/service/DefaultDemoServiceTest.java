package io.digital.supercharger.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.mapper.DemoMappable;
import io.digital.supercharger.model.Demo;
import io.digital.supercharger.repository.DemoRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = DefaultDemoServiceTest.SpringTestConfig.class)
@RunWith(SpringRunner.class)
public class DefaultDemoServiceTest {

    @Configuration
    @ComponentScan(basePackageClasses = DemoMappable.class)
    public static class SpringTestConfig {
    }

    @Autowired
    private DemoMappable demoMappable;

    @Mock
    private DemoRepository demoRepository;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private DefaultDemoService defaultDemoService;

    @Before
    public void setup() {
        defaultDemoService = new DefaultDemoService(demoRepository, demoMappable);
    }


    @Test
    public void findAll() {
        List<Demo> itemsInDb = new ArrayList<>(2);
        itemsInDb.add(new Demo(1L, "test 1"));
        itemsInDb.add(new Demo(2L, "test 2"));

        when(demoRepository.findAll()).thenReturn(itemsInDb);
        List<DemoData> demoDataList = defaultDemoService.findAll();
        verify(demoRepository, times(1)).findAll();

        assertEquals(2, demoDataList.size());
    }

    @Test
    public void save() {
        when(demoRepository.save(any())).thenReturn(new Demo(1L, "test 2"));

        defaultDemoService.save(new DemoData(1L, "test 2"));
        ArgumentCaptor<Demo> argument = ArgumentCaptor.forClass(Demo.class);
        verify(demoRepository).save(argument.capture());
        assertEquals(new Long(1L), argument.getValue().getId());
        assertEquals("test 2", argument.getValue().getName());
    }

    @Test
    public void findById() {
        long id = 1L;
        when(demoRepository.findById(id)).thenReturn(Optional.of(new Demo(id, "test 1")));
        DemoData demoData = defaultDemoService.findById(id);
        verify(demoRepository, times(1)).findById(id);

        assertEquals(new Long(id), demoData.getId());
        assertEquals("test 1", demoData.getName());
    }

    @Test
    public void findByIdError() {
        long id = 1L;
        exceptionRule.expect(EntityNotFoundException.class);
        exceptionRule.expectMessage("Demo not found.");


        when(demoRepository.findById(id)).thenReturn(Optional.empty());
        defaultDemoService.findById(id);
        verify(demoRepository, times(1)).findById(id);
    }
}
