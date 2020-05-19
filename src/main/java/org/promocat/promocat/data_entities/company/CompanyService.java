package org.promocat.promocat.data_entities.company;

import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
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

    public CompanyDTO save(final CompanyDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public CompanyDTO findById(final Long id) {
        Optional<Company> company = repository.findById(id);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such id: %d in db.", id));
        }
    }

    public CompanyDTO findByTelephone(final String telephone) {
        Optional<Company> company = repository.findByTelephone(telephone);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such telephone: %s in db.", telephone));
        }
    }

    public CompanyDTO findByToken(final String token) {
        Optional<Company> company = repository.findByToken(token);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such token: %s in db.", token));
        }
    }

    public CompanyDTO findByOrganizationName(final String organizationName) {
        Optional<Company> company = repository.findByOrganizationName(organizationName);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such name: %s in db.", organizationName));
        }
    }

    public CompanyDTO findByMail(final String mail) {
        Optional<Company> company = repository.findByMail(mail);
        if (company.isPresent()) {
            return mapper.toDto(company.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such mail: %s in db.", mail));
        }
    }
}
