package org.promocat.promocat.data_entities.parameters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Parameter;

@Repository
public interface ParametersRepository extends JpaRepository<Parameters, Long> {

}
