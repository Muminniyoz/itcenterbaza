package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethodConfig} and its DTO {@link PaymentMethodConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class})
public interface PaymentMethodConfigMapper extends EntityMapper<PaymentMethodConfigDTO, PaymentMethodConfig> {

    @Mapping(source = "method.id", target = "methodId")
    PaymentMethodConfigDTO toDto(PaymentMethodConfig paymentMethodConfig);

    @Mapping(source = "methodId", target = "method")
    PaymentMethodConfig toEntity(PaymentMethodConfigDTO paymentMethodConfigDTO);

    default PaymentMethodConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentMethodConfig paymentMethodConfig = new PaymentMethodConfig();
        paymentMethodConfig.setId(id);
        return paymentMethodConfig;
    }
}
