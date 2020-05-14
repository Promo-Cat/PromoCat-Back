package org.promocat.promocat.data_entities.company;

import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Danil Lyskin at 20:29 12.05.2020
 */
@Service
public class CompanyService {
    private final CompanyMapper mapper;
    private final CompanyRepository repository;

    @Autowired
    public CompanyService(final CompanyMapper mapper, final CompanyRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public CompanyDTO save(CompanyDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public CompanyDTO findById(Long id) {
        Optional<Company> company = repository.findById(id);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such id: %d in db.", id));
        }
    }
}
