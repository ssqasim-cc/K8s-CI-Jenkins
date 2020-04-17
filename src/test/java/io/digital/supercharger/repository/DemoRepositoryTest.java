package io.digital.supercharger.repository;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

import io.digital.supercharger.IntegrationTest;
import io.digital.supercharger.model.Demo;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test") //Active profile test is needed to exclude swagger from loading
@Category(IntegrationTest.class)
public class DemoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DemoRepository demoRepository;

    @Test
    public void itShouldSaveDemoAndFind() {
        final long id = 1L;
        Demo demo = new Demo();
        demo.setName("test");
        entityManager.persistAndFlush(demo);

        assertTrue(demoRepository.findById(id).isPresent());
        assertThat(demoRepository.findById(id).get()).isEqualTo(demo);
    }

    @Test
    public void itShouldNotFoundDemo() {
        Optional<Demo> not_found = demoRepository.findById(2L);
        assertFalse(not_found.isPresent());
    }
}
