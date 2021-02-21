package uz.itcenterbaza.service.impl;

import uz.itcenterbaza.service.PaymentMethodConfigService;
import uz.itcenterbaza.domain.PaymentMethodConfig;
import uz.itcenterbaza.repository.PaymentMethodConfigRepository;
import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;
import uz.itcenterbaza.service.mapper.PaymentMethodConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentMethodConfig}.
 */
@Service
@Transactional
public class PaymentMethodConfigServiceImpl implements PaymentMethodConfigService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodConfigServiceImpl.class);

    private final PaymentMethodConfigRepository paymentMethodConfigRepository;

    private final PaymentMethodConfigMapper paymentMethodConfigMapper;

    public PaymentMethodConfigServiceImpl(PaymentMethodConfigRepository paymentMethodConfigRepository, PaymentMethodConfigMapper paymentMethodConfigMapper) {
        this.paymentMethodConfigRepository = paymentMethodConfigRepository;
        this.paymentMethodConfigMapper = paymentMethodConfigMapper;
    }

    @Override
    public PaymentMethodConfigDTO save(PaymentMethodConfigDTO paymentMethodConfigDTO) {
        log.debug("Request to save PaymentMethodConfig : {}", paymentMethodConfigDTO);
        PaymentMethodConfig paymentMethodConfig = paymentMethodConfigMapper.toEntity(paymentMethodConfigDTO);
        paymentMethodConfig = paymentMethodConfigRepository.save(paymentMethodConfig);
        return paymentMethodConfigMapper.toDto(paymentMethodConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentMethodConfigs");
        return paymentMethodConfigRepository.findAll(pageable)
            .map(paymentMethodConfigMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentMethodConfigDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethodConfig : {}", id);
        return paymentMethodConfigRepository.findById(id)
            .map(paymentMethodConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethodConfig : {}", id);
        paymentMethodConfigRepository.deleteById(id);
    }
}
