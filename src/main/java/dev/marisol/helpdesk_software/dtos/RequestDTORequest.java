package dev.marisol.helpdesk_software.dtos;

import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;  


public record RequestDTORequest(
        @NotBlank String applicantName, 
        @NotNull Long topicId,          
        @NotBlank String description    
) {}
