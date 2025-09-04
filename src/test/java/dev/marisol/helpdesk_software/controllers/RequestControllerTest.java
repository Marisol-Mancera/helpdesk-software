package dev.marisol.helpdesk_software.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.marisol.helpdesk_software.service.IRequestService;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {


@Autowired private MockMvc mockMvc;

@MockitoBean private IRequestService requestService;

@Test
@DisplayName("DELETE should response 204 when request is ATTENDED")
public void deleteShouldReturn204WhenAttended() throws Exception{
    
    mockMvc.perform(delete("/api/v1/requests/{id}", 1L))
    .andExpect(status().isNoContent());
}
}