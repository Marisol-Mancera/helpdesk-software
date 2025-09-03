package dev.marisol.helpdesk_software.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.marisol.helpdesk_software.repository.RequestRepository;

@Service
public class RequestServiceTest {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
    }

}
