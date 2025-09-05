package dev.marisol.helpdesk_software.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.entities.RequestEntity;
import dev.marisol.helpdesk_software.enums.RequestStatus;
import dev.marisol.helpdesk_software.exceptions.RequestConflictException;
import dev.marisol.helpdesk_software.exceptions.RequestNotFoundException;
import dev.marisol.helpdesk_software.mappers.RequestMapper;
import dev.marisol.helpdesk_software.repository.RequestRepository;

@Service
public class RequestServiceImpl implements IRequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
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

    @Override
    public void markAsAttended(Long id, String technicianName) {
        RequestEntity r = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));

        if (technicianName == null || technicianName.isBlank()) {
            throw new IllegalArgumentException("El nombre del técnico es obligatorio");
        }
        if (r.getStatus() == RequestStatus.ATTENDED) {
            throw new RequestConflictException("La solicitud ya está marcada como atendida");
        }

        r.setStatus(RequestStatus.ATTENDED);
        r.setAttendedBy(technicianName);
        r.setAttendedAt(LocalDateTime.now());
        requestRepository.save(r);
    }

    @Override
    public RequestDTOResponse create(RequestDTORequest dto) {
        RequestEntity entity = requestMapper.toEntity(dto);

        if (entity.getStatus() == null) {
            entity.setStatus(RequestStatus.PENDING);
        }

        RequestEntity saved = requestRepository.save(entity);
        return requestMapper.toDTO(saved);
    }

    @Override
    public List<RequestDTOResponse> getEntities() {
        List<RequestEntity> entities = requestRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
        List<RequestDTOResponse> dtos = new ArrayList<>();
        for (RequestEntity e : entities) {
            dtos.add(requestMapper.toDTO(e));
        }
        return dtos;
    }

    @Override
    public RequestDTOResponse showById(Long id) {
        RequestEntity r = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));
        return requestMapper.toDTO(r);
    }

    @Override
    public RequestDTOResponse update(Long id, RequestDTORequest dto) {
        RequestEntity r = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("Solicitud no encontrada"));

        RequestEntity data = requestMapper.toEntity(dto);

        r.setApplicantName(data.getApplicantName());
        r.setDescription(data.getDescription());
        r.setTopic(data.getTopic());
        r.setUpdatedAt(LocalDateTime.now());

        RequestEntity saved = requestRepository.save(r);
        return requestMapper.toDTO(saved);
    }
}
