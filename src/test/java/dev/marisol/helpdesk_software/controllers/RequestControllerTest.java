package dev.marisol.helpdesk_software.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.marisol.helpdesk_software.exceptions.RequestConflictException;
import dev.marisol.helpdesk_software.exceptions.RequestNotFoundException;
import dev.marisol.helpdesk_software.service.IRequestService;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private IRequestService requestService;

        @Test
        @DisplayName("DELETE should response 204 when request is ATTENDED")
        public void deleteShouldReturn204WhenAttended() throws Exception {

                mockMvc.perform(delete("/api/v1/requests/{id}", 1L))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE should respnse 404 when id doesn't exist")
        public void deleteShouldReturn404WhenNotFound() throws Exception {

                doThrow(new RequestNotFoundException("Solicitud no encontrada"))
                                .when(requestService).deleteIfAttended(99L);

                mockMvc.perform(delete("/api/v1/requests/{id}", 99L))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DELETE  should respnse 409 when Request is PENDING")
        public void deleteShouldReturn409WhenPending() throws Exception {

                doThrow(new RequestConflictException("Solo se pueden eliminar solicitudes atendidas"))
                                .when(requestService).deleteIfAttended(1L);

                mockMvc.perform(delete("/api/v1/requests/{id}", 1L))
                                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("PATCH should response 204 when ATTENDED")
        public void attendShouldReturn204() throws Exception {
                mockMvc.perform(patch("/api/v1/requests/{id}/attend", 10L).param("technicianName", "Alice"))
                                .andExpect(status().isNoContent());
        }

}