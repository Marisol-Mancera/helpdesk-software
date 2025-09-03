package dev.marisol.helpdesk_software.repository;

import dev.marisol.helpdesk_software.entities.*;
import dev.marisol.helpdesk_software.enums.RequestStatus;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties={"spring.datasource.url=jdbc:h2:mem:testdb","spring.jpa.hibernate.ddl-auto=create-drop"},showSql=true)

public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    private TopicEntity topic;

    @BeforeEach
    public void setUp() {
        topic = new TopicEntity();
        topic.setName("Hardware");
        topic.setActive(true);
        entityManager.persist(topic);
    }

    @AfterEach
    public void tearDown() {
        entityManager.clear();
    }

    @Test
    @DisplayName("Should assign createAt and updateAt when saving")
    public void shouldSetTimestampOnSave() {

        RequestEntity r = new RequestEntity();

        r.setApplicantName("María");
        r.setDescription("No enciende el pc");
        r.setTopic(topic);

        requestRepository.save(r);
        entityManager.flush();

        assertThat(r.getId(), notNullValue());
        assertThat(r.getCreatedAt(), notNullValue());
        assertThat(r.getUpdatedAt(), notNullValue());
        assertThat(r.getStatus(), is(RequestStatus.PENDING));
    }
}
