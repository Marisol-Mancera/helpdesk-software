package dev.marisol.helpdesk_software.mappers;

import org.springframework.stereotype.Component;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.entities.TopicEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.repository.TopicRepository;

@Component
public class RequestMapper {

    private final TopicRepository topicRepository;

    public RequestMapper(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public RequestEntity toEntity(RequestDTORequest dto) {
        RequestEntity entity = new RequestEntity();
        entity.setApplicantName(dto.applicantName());
        entity.setDescription(dto.description());

        TopicEntity topic = topicRepository.findById(dto.topicId())
            .orElseThrow(() -> new IllegalArgumentException("Tema no encontrado: " + dto.topicId()));
        entity.setTopic(topic);

        entity.setStatus(RequestStatus.PENDING);
        return entity;
    }

    public RequestDTOResponse toDTO(RequestEntity entity) {
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
