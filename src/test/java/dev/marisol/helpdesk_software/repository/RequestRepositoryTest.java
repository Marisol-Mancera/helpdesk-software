package dev.marisol.helpdesk_software.repository;

import dev.marisol.helpdesk_software.entities.*;
import dev.marisol.helpdesk_software.enums.RequestStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop" }, showSql = true)

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

    @Test
    @DisplayName("Should update updateAt when modify")
    public void shouldUpdateWhenModify() {

        RequestEntity r = new RequestEntity();

        r.setApplicantName("Carlos");
        r.setDescription("Pantalla rota");
        r.setTopic(topic);
        requestRepository.save(r);
        entityManager.flush();
        LocalDateTime originalCreated = r.getCreatedAt();
        LocalDateTime originalUpdated = r.getUpdatedAt();

        r.setDescription("Pantalla rota - cambiado");
        requestRepository.save(r);
        entityManager.flush();

        assertThat(r.getCreatedAt(), is(equalTo(originalCreated)));
        assertThat(r.getUpdatedAt().isAfter(originalUpdated), is(true));
    }

    @Test
    @DisplayName("should  save a Request and retrieve it by id")
    public void shouldSaveAndFindById() {

        RequestEntity r = new RequestEntity();

        r.setApplicantName("Ana");
        r.setDescription("Problema con WiFi");
        r.setTopic(topic);
        RequestEntity stored = requestRepository.save(r);
        entityManager.flush();

        Long id = stored.getId();
        RequestEntity found = requestRepository.findById(id).orElseThrow();

        assertThat(stored.getId(), notNullValue());
        assertThat(found.getId(), is(equalTo(stored.getId())));
        assertThat(found.getApplicantName(), is(equalTo("Ana")));
        assertThat(found.getDescription(), is(equalTo("Problema con WiFi")));
        assertThat(found.getTopic().getId(), is(equalTo(topic.getId())));
    }

    @Test
    @DisplayName("Should list all the Requests and count the total.")
    public void shouldFindAllAndCount() {

        RequestEntity r1 = new RequestEntity();

        r1.setApplicantName("Pedro");
        r1.setDescription("Teclado roto");
        r1.setTopic(topic);

        RequestEntity r2 = new RequestEntity();
        r2.setApplicantName("Lucía");
        r2.setDescription("Problema en la red");
        r2.setTopic(topic);

        requestRepository.save(r1);
        requestRepository.save(r2);
        entityManager.flush();

        List<RequestEntity> allRequests = requestRepository.findAll();
        long total = requestRepository.count();

        assertThat(allRequests.size(), is(greaterThanOrEqualTo(2)));
        assertThat(total, is(greaterThanOrEqualTo(2L)));
    }

    @Test
    @DisplayName("Should delete a request by Id")
    public void shouldDeleteById() {

        RequestEntity r = new RequestEntity();

        r.setApplicantName("Sofía");
        r.setDescription("Error al iniciar sesión");
        r.setTopic(topic);
        RequestEntity stored = requestRepository.save(r);
        entityManager.flush();

        Long id = stored.getId();
        requestRepository.deleteById(id);
        entityManager.flush();

        boolean exists = requestRepository.findById(id).isPresent();

        assertThat(exists, is(false));
    }


}
