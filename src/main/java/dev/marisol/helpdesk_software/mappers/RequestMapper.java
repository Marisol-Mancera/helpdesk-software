package dev.marisol.helpdesk_software.mappers;

import org.springframework.stereotype.Component;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.entities.TopicEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;

@Component
public class RequestMapper {

    public static RequestEntity toEntity(RequestDTORequest dtoRequest, TopicEntity topic) {
        RequestEntity e = new RequestEntity();
        e.setApplicantName(dtoRequest.applicantName());
        e.setDescription(dtoRequest.description());
        e.setTopic(topic);
        e.setStatus(RequestStatus.PENDING);
        return e;
    }

    public static RequestDTOResponse toDTO(RequestEntity entity) {
        String topicName = entity.getTopic() != null ? entity.getTopic().getName() : null;
        String status = entity.getStatus() != null ? entity.getStatus().name() : RequestStatus.PENDING.name();
        return new RequestDTOResponse(
            entity.getId(),
            entity.getApplicantName(),
            topicName,
            entity.getDescription(),
            status,
            entity.getAttendedBy(),
            entity.getAttendedAt(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
