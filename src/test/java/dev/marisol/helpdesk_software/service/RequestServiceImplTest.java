package dev.marisol.helpdesk_software.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.repository.RequestRepository;


@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @Mock private RequestRepository requestRepository;
    private RequestServiceImpl requestService;

    @BeforeEach 
    public void setUp() { 
        requestService = new RequestServiceImpl(requestRepository); 
    }

    @Test
    @DisplayName("Should delete when status is ATTENDED")
    public void shouldDeleteWhenAttended(){

        RequestEntity r = new RequestEntity();
        r.setId(10L);
        r.setStatus(RequestStatus.ATTENDED);

        when(requestRepository.findById(10L)).thenReturn(Optional.of(r));
        requestService.deleteIfAttended(10L);

        verify(requestRepository).deleteById(10L);
        verify(requestRepository, times(1)).findById(10L);
        }
}