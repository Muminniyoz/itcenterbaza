package uz.itcenterbaza.service.impl;

import uz.itcenterbaza.service.RegionsService;
import uz.itcenterbaza.domain.Regions;
import uz.itcenterbaza.repository.RegionsRepository;
import uz.itcenterbaza.service.dto.RegionsDTO;
import uz.itcenterbaza.service.mapper.RegionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Regions}.
 */
@Service
@Transactional
public class RegionsServiceImpl implements RegionsService {

    private final Logger log = LoggerFactory.getLogger(RegionsServiceImpl.class);

    private final RegionsRepository regionsRepository;

    private final RegionsMapper regionsMapper;

    public RegionsServiceImpl(RegionsRepository regionsRepository, RegionsMapper regionsMapper) {
        this.regionsRepository = regionsRepository;
        this.regionsMapper = regionsMapper;
    }

    @Override
    public RegionsDTO save(RegionsDTO regionsDTO) {
        log.debug("Request to save Regions : {}", regionsDTO);
        Regions regions = regionsMapper.toEntity(regionsDTO);
        regions = regionsRepository.save(regions);
        return regionsMapper.toDto(regions);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regions");
        return regionsRepository.findAll(pageable)
            .map(regionsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RegionsDTO> findOne(Long id) {
        log.debug("Request to get Regions : {}", id);
        return regionsRepository.findById(id)
            .map(regionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Regions : {}", id);
        regionsRepository.deleteById(id);
    }
}
