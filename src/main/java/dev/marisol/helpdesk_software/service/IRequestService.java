package dev.marisol.helpdesk_software.service;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;

public interface IRequestService {
    
    void deleteIfAttended(Long id);
 
    void markAsAttended(Long id, String technicianName);

    RequestDTOResponse create(RequestDTORequest dto);
}