package io.digital.supercharger.service;

import io.digital.supercharger.dto.DemoData;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface DemoService {
    /**
     * Listing all demos
     *
     * @return list of demo data
     */
    List<DemoData> findAll();

    /**
     * Save Demo into demo database
     *
     * @param demoData the demo data inserted
     * @return inserted demo data
     */
    DemoData save(DemoData demoData);

    /**
     * Get by demo id
     *
     * @param demoId demo id
     * @return DemoData object
     * @throws EntityNotFoundException with no data
     */
    DemoData findById(Long demoId);
}
