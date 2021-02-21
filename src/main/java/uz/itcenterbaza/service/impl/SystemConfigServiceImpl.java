package uz.itcenterbaza.service.impl;

import uz.itcenterbaza.service.SystemConfigService;
import uz.itcenterbaza.domain.SystemConfig;
import uz.itcenterbaza.repository.SystemConfigRepository;
import uz.itcenterbaza.service.dto.SystemConfigDTO;
import uz.itcenterbaza.service.mapper.SystemConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SystemConfig}.
 */
@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    private final Logger log = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    @Override
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to save SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        return systemConfigMapper.toDto(systemConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemConfigs");
        return systemConfigRepository.findAll(pageable)
            .map(systemConfigMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SystemConfigDTO> findOne(Long id) {
        log.debug("Request to get SystemConfig : {}", id);
        return systemConfigRepository.findById(id)
            .map(systemConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemConfig : {}", id);
        systemConfigRepository.deleteById(id);
    }
}
