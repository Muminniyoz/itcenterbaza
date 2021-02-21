package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ParticipantMapper.class, PaymentMethodMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "method.id", target = "methodId")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "modifiedById", target = "modifiedBy")
    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "methodId", target = "method")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
