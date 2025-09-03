package dev.marisol.helpdesk_software.service;

import org.springframework.stereotype.Service;

import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.repository.RequestRepository;

@Service
public class RequestService implements IRequestService {

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) { 
        this.requestRepository = requestRepository; 
    }

    @Override 
    public void deleteIfAttended(Long id) {

        RequestEntity r = requestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        if (r.getStatus() != RequestStatus.ATTENDED) { 
            throw new IllegalStateException("Solo se pueden eliminar solicitudes atendidas"); }
            requestRepository.deleteById(id);
    }

    
}
