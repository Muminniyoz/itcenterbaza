package uz.itcenterbaza.service.impl;

import uz.itcenterbaza.service.RegisteredService;
import uz.itcenterbaza.domain.Registered;
import uz.itcenterbaza.repository.RegisteredRepository;
import uz.itcenterbaza.service.dto.RegisteredDTO;
import uz.itcenterbaza.service.mapper.RegisteredMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Registered}.
 */
@Service
@Transactional
public class RegisteredServiceImpl implements RegisteredService {

    private final Logger log = LoggerFactory.getLogger(RegisteredServiceImpl.class);

    private final RegisteredRepository registeredRepository;

    private final RegisteredMapper registeredMapper;

    public RegisteredServiceImpl(RegisteredRepository registeredRepository, RegisteredMapper registeredMapper) {
        this.registeredRepository = registeredRepository;
        this.registeredMapper = registeredMapper;
    }

    @Override
    public RegisteredDTO save(RegisteredDTO registeredDTO) {
        log.debug("Request to save Registered : {}", registeredDTO);
        Registered registered = registeredMapper.toEntity(registeredDTO);
        registered = registeredRepository.save(registered);
        return registeredMapper.toDto(registered);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegisteredDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Registereds");
        return registeredRepository.findAll(pageable)
            .map(registeredMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RegisteredDTO> findOne(Long id) {
        log.debug("Request to get Registered : {}", id);
        return registeredRepository.findById(id)
            .map(registeredMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Registered : {}", id);
        registeredRepository.deleteById(id);
    }
}
