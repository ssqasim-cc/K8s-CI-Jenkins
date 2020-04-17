package io.digital.supercharger.service;

import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.mapper.DemoMappable;
import io.digital.supercharger.model.Demo;
import io.digital.supercharger.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultDemoService implements DemoService {

    private final DemoRepository demoRepository;
    private final DemoMappable demoMappable;

    @Autowired
    public DefaultDemoService(DemoRepository demoRepository, DemoMappable demoMappable) {
        this.demoRepository = demoRepository;
        this.demoMappable = demoMappable;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public List<DemoData> findAll() {
        return demoRepository.findAll().stream()
            .map(demoMappable::entityToDto)
            .collect(Collectors.toList());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public DemoData save(DemoData demoData) {
        Demo demo = demoRepository.save(new Demo(demoData.getId(), demoData.getName()));
        return new DemoData(demo.getId(), demo.getName());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public DemoData findById(Long demoId) {
        return demoRepository.findById(demoId)
            .map(demoMappable::entityToDto)
            .orElseThrow(() -> new EntityNotFoundException("Demo not found."));
    }
}
