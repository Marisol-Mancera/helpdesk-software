package dev.marisol.helpdesk_software.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.marisol.helpdesk_software.service.IRequestService;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {

    private final IRequestService requestService;

    public RequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        requestService.deleteIfAttended(id);
        return ResponseEntity.noContent().build();

    }
}
