package dev.marisol.helpdesk_software.dtos;

import java.time.LocalDateTime; 


public record RequestDTOResponse(
        Long id,            
        String applicantName,  
        String topicName,      
        String description,   
        String status,      
        String attendedBy,    
        LocalDateTime attendedAt,
        LocalDateTime createdAt,  
        LocalDateTime updatedAt   
) {}
