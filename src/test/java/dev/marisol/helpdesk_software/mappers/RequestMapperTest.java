package dev.marisol.helpdesk_software.mappers;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.entities.TopicEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.repository.TopicRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

public class RequestMapperTest {

    private TopicRepository topicRepository;
    private RequestMapper mapper;

    @BeforeEach
    void setUp() {
        topicRepository = Mockito.mock(TopicRepository.class);
        mapper = new RequestMapper(topicRepository);
    }

    @Test
    @DisplayName("toEntity should map fields correctly")
    void toEntityShouldMapFields() {
        // arrange
        TopicEntity topic = new TopicEntity();
        topic.setId(1L);
        topic.setName("Hardware");

        Mockito.when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        RequestDTORequest dto = new RequestDTORequest("María", 1L, "No enciende el PC");

        // act
        RequestEntity entity = mapper.toEntity(dto);

        // assert
        assertThat(entity.getApplicantName(), is("María"));
        assertThat(entity.getDescription(), is("No enciende el PC"));
        assertThat(entity.getTopic().getName(), is("Hardware"));
        assertThat(entity.getStatus(), is(RequestStatus.PENDING));
    }

    @Test
    @DisplayName("toDTO should map fields correctly")
    void toDTOShouldMapFields() {
        // arrange
        TopicEntity topic = new TopicEntity();
        topic.setId(1L);
        topic.setName("Software");

        RequestEntity entity = new RequestEntity();
        entity.setId(5L);
        entity.setApplicantName("Carlos");
        entity.setDescription("Pantalla azul");
        entity.setTopic(topic);
        entity.setStatus(RequestStatus.ATTENDED);
        entity.setAttendedBy("Alice");
        entity.setAttendedAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now().minusDays(1));
        entity.setUpdatedAt(LocalDateTime.now());

        // act
        RequestDTOResponse dto = mapper.toDTO(entity);

        // assert
        assertThat(dto.id(), is(5L));
        assertThat(dto.applicantName(), is("Carlos"));
        assertThat(dto.topicName(), is("Software"));
        assertThat(dto.description(), is("Pantalla azul"));
        assertThat(dto.status(), is("ATTENDED"));
        assertThat(dto.attendedBy(), is("Alice"));
        assertThat(dto.attendedAt(), notNullValue());
        assertThat(dto.createdAt(), notNullValue());
        assertThat(dto.updatedAt(), notNullValue());
    }
}
