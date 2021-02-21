package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.PaymentMethodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {


    @Mapping(target = "confs", ignore = true)
    @Mapping(target = "removeConf", ignore = true)
    PaymentMethod toEntity(PaymentMethodDTO paymentMethodDTO);

    default PaymentMethod fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        return paymentMethod;
    }
}
