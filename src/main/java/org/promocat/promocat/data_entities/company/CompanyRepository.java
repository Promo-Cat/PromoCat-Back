package org.promocat.promocat.data_entities.company;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Danil Lyskin at 20:28 12.05.2020
 */
public interface CompanyRepository extends JpaRepository<CompanyRecord, Long> {
}
