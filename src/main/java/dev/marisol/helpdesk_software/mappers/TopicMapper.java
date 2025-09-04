package dev.marisol.helpdesk_software.mappers;

import org.springframework.stereotype.Component;

import dev.marisol.helpdesk_software.dtos.TopicDTOResponse;
import dev.marisol.helpdesk_software.entities.TopicEntity;

@Component
public class TopicMapper {

    public static TopicDTOResponse toDTO(TopicEntity entity) {
        return new TopicDTOResponse(
            entity.getId(),
            entity.getName()
        );
    }
}
