package com.hospital.api;

import com.hospital.api.controller.AtencionController;
import com.hospital.api.model.Atencion;
import com.hospital.api.service.AtencionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AtencionController.class)
public class AtencionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtencionService atencionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Atencion atencion1;

    @BeforeEach
    void setUp() {
        atencion1 = new Atencion();
        atencion1.setIdAtencion(1);
        atencion1.setComentario("Consulta de control");
        atencion1.setFechaAtencion(LocalDate.now());
        atencion1.setHoraAtencion(LocalDateTime.now());
        atencion1.setCosto(10000);
    }

    @Test
    void testGetAllAtenciones() throws Exception {
        Mockito.when(atencionService.getAllAtencions()).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].comentario").value("Consulta de control"));
    }

    @Test
    void testGetAtencionById_Found() throws Exception {
        Mockito.when(atencionService.getAtencionById(1)).thenReturn(Optional.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAtencion").value(1));
    }

    @Test
    void testGetAtencionById_NotFound() throws Exception {
        Mockito.when(atencionService.getAtencionById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/atenciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAtencion() throws Exception {
        Atencion atencionSinId = new Atencion();
        atencionSinId.setComentario("Consulta de control");

        Mockito.when(atencionService.saveAtencion(any(Atencion.class))).thenReturn(atencion1);

        mockMvc.perform(post("/api/v1/atenciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atencionSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAtencion").value(1));
    }

    @Test
    void testUpdateAtencion_Success() throws Exception {
        Mockito.when(atencionService.existsAtencion(1)).thenReturn(true);
        Mockito.when(atencionService.saveAtencion(any(Atencion.class))).thenReturn(atencion1);

        mockMvc.perform(put("/api/v1/atenciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atencion1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Consulta de control"));
    }

    @Test
    void testUpdateAtencion_NotFound() throws Exception {
        Mockito.when(atencionService.existsAtencion(1)).thenReturn(false);

        mockMvc.perform(put("/api/v1/atenciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atencion1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAtencion_Success() throws Exception {
        Mockito.when(atencionService.existsAtencion(1)).thenReturn(true);
        Mockito.doNothing().when(atencionService).deleteAtencion(1);

        mockMvc.perform(delete("/api/v1/atenciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAtencion_NotFound() throws Exception {
        Mockito.when(atencionService.existsAtencion(99)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/atenciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAtencionesByFecha() throws Exception {
        LocalDate fecha = LocalDate.of(2025, 6, 26);
        Mockito.when(atencionService.getAtencionesByFecha(fecha)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/por-fecha").param("fecha", "2025-06-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetAtencionesBetweenFechas() throws Exception {
        LocalDate fechaInicio = LocalDate.of(2025, 6, 1);
        LocalDate fechaFin = LocalDate.of(2025, 6, 30);
        Mockito.when(atencionService.getAtencionesBetweenFechas(fechaInicio, fechaFin)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/entre-fechas")
                        .param("fechaInicio", "2025-06-01")
                        .param("fechaFin", "2025-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetAtencionesCostoMenorQue() throws Exception {
        Mockito.when(atencionService.getAtencionesCostoMenorQue(50000)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/costo-menor-que").param("cantidad", "50000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].costo", is(10000.0)));
    }

    @Test
    void testGetAtencionesCostoMayorQue() throws Exception {
        Mockito.when(atencionService.getAtencionesCostoMayorQue(5000)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/costo-mayor-que").param("cantidad", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].costo", is(10000.0)));
    }

    @Test
    void testGetAtencionesByMedico_Success() throws Exception {
        Mockito.when(atencionService.getAtencionesByMedicoId(1)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/por-medico/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetAtencionesByMedico_NoContent() throws Exception {
        Mockito.when(atencionService.getAtencionesByMedicoId(2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/atenciones/por-medico/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetGananciaTotalAltas() throws Exception {
        Mockito.when(atencionService.getGananciaTotalPorAltas()).thenReturn(250000.0);

        mockMvc.perform(get("/api/v1/atenciones/reportes/ganancias-hospital"))
                .andExpect(status().isOk())
                .andExpect(content().string("250000.0"));
    }

    @Test
    void testGetAtencionesPorPaciente() throws Exception {
        Mockito.when(atencionService.obtenerAtencionesPorPaciente(1)).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/por-paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetAtencionesPorEstado() throws Exception {
        Mockito.when(atencionService.obtenerAtencionesPorEstado("Alta")).thenReturn(List.of(atencion1));

        mockMvc.perform(get("/api/v1/atenciones/por-estado").param("estado", "Alta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetTotalPago() throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("paciente", "Ana González");
        resultado.put("totalAPagar", 75000.0);

        Mockito.when(atencionService.calcularTotalPagoPorPaciente(1)).thenReturn(resultado);

        mockMvc.perform(get("/api/v1/atenciones/total-a-pagar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paciente").value("Ana González"))
                .andExpect(jsonPath("$.totalAPagar").value(75000.0));
    }
}