package dev.marisol.helpdesk_software.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import dev.marisol.helpdesk_software.dtos.RequestDTORequest;
import dev.marisol.helpdesk_software.dtos.RequestDTOResponse;
import dev.marisol.helpdesk_software.exceptions.RequestConflictException;
import dev.marisol.helpdesk_software.exceptions.RequestNotFoundException;
import dev.marisol.helpdesk_software.service.IRequestService;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper mapper;

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

        @Test
        @DisplayName("POST should return 201 with created payload")
        public void postShouldReturn201() throws Exception {
                RequestDTORequest dto = new RequestDTORequest("María", 1L, "No enciende el PC");

                RequestDTOResponse created = new RequestDTOResponse(
                                1L, "María", "Hardware", "No enciende el PC", "PENDING", null, null, null, null);

                String json = mapper.writeValueAsString(dto);
                when(requestService.create(any(
                                RequestDTORequest.class))).thenReturn(created);
                MockHttpServletResponse response = mockMvc.perform(post("/api/v1/requests")
                                .contentType("application/json")
                                .content(json))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse();

                assertThat(response.getContentAsString(), containsString("María"));
                assertThat(response.getContentAsString(), containsString("PENDING"));
        }

        @Test
        @DisplayName("POST should return 400 when applicantName is empty")
        public void postShouldReturn400WhenApplicantNameIsEmpty() throws Exception {

                RequestDTORequest dto = new RequestDTORequest("", 1L, "No enciende el PC");
                String json = mapper.writeValueAsString(dto);
                mockMvc.perform(post("/api/v1/requests")
                                .contentType("application/json")
                                .content(json))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST should return 400 when topicId is null")
        public void postShouldReturn400WhenTopicIdIsNull() throws Exception {

                RequestDTORequest dto = new RequestDTORequest("María", null, "No enciende el PC");
                String json = mapper.writeValueAsString(dto);

                mockMvc.perform(post("/api/v1/requests")
                                .contentType("application/json")
                                .content(json))
                                .andExpect(status().isBadRequest());

                verify(requestService, never()).create(any(RequestDTORequest.class));
        }

        @Test
        @DisplayName("POST should return 404 when topicId does not exist")
        public void postShouldReturn404WhenTopicIdDoesNotExist() throws Exception {

                RequestDTORequest dto = new RequestDTORequest("María", 999L, "No enciende el PC");
                String json = mapper.writeValueAsString(dto);

                when(requestService.create(any(RequestDTORequest.class)))
                                .thenThrow(new RequestNotFoundException("Topic not found"));

                mockMvc.perform(post("/api/v1/requests")
                                .contentType("application/json")
                                .content(json))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST should return 409 when request creation conflicts")
        public void postShouldReturn409WhenConflictOccurs() throws Exception {

                RequestDTORequest dto = new RequestDTORequest("María", 1L, "Duplicado de prueba");
                String json = mapper.writeValueAsString(dto);

                when(requestService.create(any(RequestDTORequest.class)))
                                .thenThrow(new RequestConflictException("Request already exists"));

                mockMvc.perform(post("/api/v1/requests")
                                .contentType("application/json")
                                .content(json))
                                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("GET should return 200 with list")
        public void getIndexShouldReturn200WithList() throws Exception {
                RequestDTOResponse a = new RequestDTOResponse(1L, "María", "Hardware", "Desc A", "PENDING", null, null,
                                null, null);
                RequestDTOResponse b = new RequestDTOResponse(2L, "Luis", "Software", "Desc B", "PENDING", null, null,
                                null, null);
                when(requestService.getEntities()).thenReturn(List.of(a, b));

                MockHttpServletResponse response = mockMvc.perform(get("/api/v1/requests"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse();

                assertThat(response.getContentAsString(), containsString("María"));
                assertThat(response.getContentAsString(), containsString("Luis"));
        }

        @Test
        @DisplayName("GET should return 204 when empty")
        public void getIndexShouldReturn204WhenEmpty() throws Exception {
                when(requestService.getEntities()).thenReturn(List.of());

                mockMvc.perform(get("/api/v1/requests"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("GET by id should return 200 with item")
        public void showShouldReturn200() throws Exception {
                Long id = 7L;
                RequestDTOResponse dto = new RequestDTOResponse(id, "María", "Hardware", "No enciende", "PENDING", null,
                                null, null, null);
                when(requestService.showById(id)).thenReturn(dto);

                MockHttpServletResponse response = mockMvc.perform(get("/api/v1/requests/{id}", id))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse();

                assertThat(response.getContentAsString(), containsString("\"id\":" + id));
                assertThat(response.getContentAsString(), containsString("María"));
        }

        // GET by id 404
        @Test
        @DisplayName("GET by id should return 404 when not found")
        public void showShouldReturn404WhenNotFound() throws Exception {
                when(requestService.showById(anyLong()))
                                .thenThrow(new RequestNotFoundException("Solicitud no encontrada"));

                mockMvc.perform(get("/api/v1/requests/{id}", 999L))
                                .andExpect(status().isNotFound());
        }

        // PATCH attend 400 (falta parámetro)
        @Test
        @DisplayName("PATCH should return 400 when technicianName is missing")
        public void attendShouldReturn400WhenTechnicianMissing() throws Exception {
                mockMvc.perform(patch("/api/v1/requests/{id}/attend", 1L))
                                .andExpect(status().isBadRequest());
        }

        // PATCH attend 404
        @Test
        @DisplayName("PATCH should return 404 when request not found")
        public void attendShouldReturn404WhenNotFound() throws Exception {
                doThrow(new RequestNotFoundException("Solicitud no encontrada"))
                                .when(requestService).markAsAttended(eq(1L), any());

                mockMvc.perform(patch("/api/v1/requests/{id}/attend", 1L)
                                .param("technicianName", "Alice"))
                                .andExpect(status().isNotFound());
        }

        // PATCH attend 409
        @Test
        @DisplayName("PATCH should return 409 when already attended")
        public void attendShouldReturn409WhenAlreadyAttended() throws Exception {
                doThrow(new RequestConflictException("La solicitud ya está marcada como atendida"))
                                .when(requestService).markAsAttended(eq(1L), any());

                mockMvc.perform(patch("/api/v1/requests/{id}/attend", 1L)
                                .param("technicianName", "Alice"))
                                .andExpect(status().isConflict());
        }

        // PUT update 200
        @Test
        @DisplayName("PUT should return 200 with updated payload")
        public void updateShouldReturn200WithPayload() throws Exception {
                Long id = 3L;
                RequestDTORequest body = new RequestDTORequest("María", 1L, "Cambia pantalla");
                String json = mapper.writeValueAsString(body);

                RequestDTOResponse updated = new RequestDTOResponse(
                                id, "María", "Hardware", "Cambia pantalla", "PENDING", null, null, null, null);

                when(requestService.update(eq(id), any(RequestDTORequest.class))).thenReturn(updated);

                MockHttpServletResponse resp = mockMvc.perform(
                                put("/api/v1/requests/{id}", id)
                                                .contentType("application/json")
                                                .content(json))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse();

                assertThat(resp.getContentAsString(), containsString("Cambia pantalla"));
        }

        // PUT update 404
        @Test
        @DisplayName("PUT should return 404 when request not found")
        public void updateShouldReturn404WhenNotFound() throws Exception {
                Long id = 999L;
                RequestDTORequest body = new RequestDTORequest("María", 1L, "Texto");
                String json = mapper.writeValueAsString(body);

                when(requestService.update(eq(id), any(RequestDTORequest.class)))
                                .thenThrow(new RequestNotFoundException("Solicitud no encontrada"));

                mockMvc.perform(
                                put("/api/v1/requests/{id}", id)
                                                .contentType("application/json")
                                                .content(json))
                                .andExpect(status().isNotFound());
        }

        // PUT update 400 (validación)
        @Test
        @DisplayName("PUT should return 400 when validation fails")
        public void updateShouldReturn400WhenValidationFails() throws Exception {
                RequestDTORequest invalid = new RequestDTORequest("", 1L, "desc");
                String json = mapper.writeValueAsString(invalid);

                mockMvc.perform(
                                put("/api/v1/requests/{id}", 5L)
                                                .contentType("application/json")
                                                .content(json))
                                .andExpect(status().isBadRequest());
        }
}
