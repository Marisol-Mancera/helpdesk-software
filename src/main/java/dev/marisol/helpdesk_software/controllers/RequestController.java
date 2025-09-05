package dev.marisol.helpdesk_software.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.service.IRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

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

    @PatchMapping("/{id}/attend")
    public ResponseEntity<Void> attend(@PathVariable Long id, @RequestParam String technicianName) {
        requestService.markAsAttended(id, technicianName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<RequestDTOResponse> create(@Valid @RequestBody RequestDTORequest dto) {
        RequestDTOResponse created = requestService.create(dto);
        if (created == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RequestDTOResponse>> index() {
        List<RequestDTOResponse> list = requestService.getEntities();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTOResponse> showById(@PathVariable Long id) {
        RequestDTOResponse dto = requestService.showById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestDTOResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RequestDTORequest dto) {
        RequestDTOResponse updated = requestService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

}
