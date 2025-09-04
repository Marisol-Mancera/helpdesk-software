package dev.marisol.helpdesk_software.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
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

        RequestEntity r = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));
        if (r.getStatus() != RequestStatus.ATTENDED) {
            throw new RequestConflictException("Solo se pueden eliminar solicitudes atendidas");
        }
        requestRepository.deleteById(id);
    }

    public void markAsAttended(Long id, String technicianName) {
        RequestEntity r = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));
        if (technicianName == null || technicianName.isBlank())
            throw new IllegalArgumentException("El nombre del técnico es obligatorio");
        if (r.getStatus() == RequestStatus.PENDING) {
            r.setStatus(RequestStatus.ATTENDED);
            r.setAttendedBy(technicianName);
            r.setAttendedAt(LocalDateTime.now());
            requestRepository.save(r);

        }
    }

    @Override
    public RequestDTOResponse create(RequestDTORequest dto) {
        return null;
    }

}
