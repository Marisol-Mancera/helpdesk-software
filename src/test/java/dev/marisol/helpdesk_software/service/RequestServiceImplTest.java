package dev.marisol.helpdesk_software.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.exceptions.RequestConflictException;
import dev.marisol.helpdesk_software.exceptions.RequestNotFoundException;
import dev.marisol.helpdesk_software.mappers.RequestMapper;
import dev.marisol.helpdesk_software.repository.RequestRepository;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;
    private RequestServiceImpl requestService;

    @Mock
    private RequestMapper RequestMapper;

    @BeforeEach
    public void setUp() {
        requestService = new RequestServiceImpl(requestRepository, RequestMapper);
    }

    @Test
    @DisplayName("Should delete when status is ATTENDED")
    public void shouldDeleteWhenAttended() {

        RequestEntity r = new RequestEntity();
        r.setId(10L);
        r.setStatus(RequestStatus.ATTENDED);

        when(requestRepository.findById(10L)).thenReturn(Optional.of(r));
        requestService.deleteIfAttended(10L);

        verify(requestRepository).deleteById(10L);
        verify(requestRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("Should throw 404 when trying to delete a non-existing request")
    public void shouldThrowWhenDeletingNonExistingRequest() {
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> requestService.deleteIfAttended(99L));
    }

    @Test
    @DisplayName("Should throw 409 when trying to delete a pending request")
    public void shouldThrowWhenDeletingPendingRequest() {
        RequestEntity r = new RequestEntity();
        r.setId(5L);
        r.setStatus(RequestStatus.PENDING);

        when(requestRepository.findById(5L)).thenReturn(Optional.of(r));

        assertThrows(RequestConflictException.class, () -> requestService.deleteIfAttended(5L));
    }

    @Test
    @DisplayName("Should mark as attended when valid technician name is provided")
    public void shouldMarkAsAttended() {
        RequestEntity r = new RequestEntity();
        r.setId(7L);
        r.setStatus(RequestStatus.PENDING);

        when(requestRepository.findById(7L)).thenReturn(Optional.of(r));
        requestService.markAsAttended(7L, "Alice");

        verify(requestRepository, times(1)).save(r);
    }

    @Test
    @DisplayName("Should throw 400 when technician name is blank")
    public void shouldThrowWhenTechnicianNameIsBlank() {
        RequestEntity r = new RequestEntity();
        r.setId(8L);
        r.setStatus(RequestStatus.PENDING);

        when(requestRepository.findById(8L)).thenReturn(Optional.of(r));

        assertThrows(IllegalArgumentException.class, () -> requestService.markAsAttended(8L, ""));
    }

    @Test
    @DisplayName("Should throw 404 when trying to mark as attended but request does not exist")
    public void shouldThrowWhenAttendingNonExistingRequest() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> requestService.markAsAttended(999L, "Bob"));
    }

    @Test
    @DisplayName("Should throw 409 when trying to mark as attended an already attended request")
    public void shouldThrowWhenAlreadyAttended() {
        RequestEntity r = new RequestEntity();
        r.setId(11L);
        r.setStatus(RequestStatus.ATTENDED);
        r.setAttendedAt(LocalDateTime.now());

        when(requestRepository.findById(11L)).thenReturn(Optional.of(r));

        assertThrows(RequestConflictException.class, () -> requestService.markAsAttended(11L, "Carlos"));
    }

    @Test
    @DisplayName("create should return DTO with PENDING status")
    public void createShouldReturnDTO() {
        RequestDTORequest dto = new RequestDTORequest("María", 1L, "No enciende");
        RequestEntity entityFromMapper = new RequestEntity();
        entityFromMapper.setApplicantName("María");
        entityFromMapper.setDescription("No enciende");
        entityFromMapper.setStatus(RequestStatus.PENDING);

        RequestEntity saved = new RequestEntity();
        saved.setId(1L);
        saved.setApplicantName("María");
        saved.setDescription("No enciende");
        saved.setStatus(RequestStatus.PENDING);

        RequestDTOResponse dtoResp = new RequestDTOResponse(1L, "María", "Hardware", "No enciende", "PENDING", null,
                null, null, null);

        when(RequestMapper.toEntity(dto)).thenReturn(entityFromMapper);
        when(requestRepository.save(entityFromMapper)).thenReturn(saved);
        when(RequestMapper.toDTO(saved)).thenReturn(dtoResp);

        RequestDTOResponse result = requestService.create(dto);

        assertEquals(1L, result.id());
        assertEquals("María", result.applicantName());
        assertEquals("PENDING", result.status());
    }

    @Test
    @DisplayName("create should throw when topic not found in mapper")
    public void createShouldThrowWhenTopicMissing() {
        RequestDTORequest dto = new RequestDTORequest("María", 999L, "No enciende");

        when(RequestMapper.toEntity(dto)).thenThrow(new IllegalArgumentException("Tema no encontrado: 999"));

        assertThrows(IllegalArgumentException.class, () -> requestService.create(dto));
    }

    
}