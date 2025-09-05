package dev.marisol.helpdesk_software.service;

import java.util.List;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;

public interface IRequestService {
    
    void deleteIfAttended(Long id);
 
    void markAsAttended(Long id, String technicianName);

    RequestDTOResponse create(RequestDTORequest dto);

    List<RequestDTOResponse> getEntities();

    RequestDTOResponse showById(Long id);
    RequestDTOResponse update(Long id, RequestDTORequest dto);
}