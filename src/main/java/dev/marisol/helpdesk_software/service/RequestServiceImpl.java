package dev.marisol.helpdesk_software.service;

import org.springframework.stereotype.Service;

import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.exceptions.RequestConflictException;
import dev.marisol.helpdesk_software.exceptions.RequestNotFoundException;
import dev.marisol.helpdesk_software.repository.RequestRepository;

@Service
public class RequestServiceImpl implements IRequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) { 
        this.requestRepository = requestRepository; 
    }

    @Override 
    public void deleteIfAttended(Long id) {

        RequestEntity r = requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));
        if (r.getStatus() != RequestStatus.ATTENDED) { 
            throw new RequestConflictException("Solo se pueden eliminar solicitudes atendidas"); }
            requestRepository.deleteById(id);
    }

    
}
