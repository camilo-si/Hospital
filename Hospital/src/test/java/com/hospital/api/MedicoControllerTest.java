package com.hospital.api;

import com.hospital.api.controller.MedicoController;
import com.hospital.api.model.Medico;
import com.hospital.api.service.MedicoService;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
public class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicoService medicoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Medico medico1;

    @BeforeEach
    void setUp() {
        medico1 = new Medico();
        medico1.setIdMedico(1);
        medico1.setNombre("Carlos");
        medico1.setApellido("Soto");
        medico1.setRun("15.111.222-3");
        medico1.setFechaContrato(LocalDate.of(2008, 12, 22));
        medico1.setSueldo_base(900000.0f);
        medico1.setCorreo("test@test.com");
        medico1.setTelefono("+5612345678");
    }

    @Test
    void testGetAllMedicos() throws Exception {
        Mockito.when(medicoService.getAllMedicos()).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }

    @Test
    void testGetMedicoById_Found() throws Exception {
        Mockito.when(medicoService.getMedicoById(1)).thenReturn(Optional.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedico").value(1));
    }

    @Test
    void testGetMedicoById_NotFound() throws Exception {
        Mockito.when(medicoService.getMedicoById(99)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/medicos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMedico() throws Exception {
        Mockito.when(medicoService.saveMedico(any(Medico.class))).thenReturn(medico1);
        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.run").value("15.111.222-3"));
    }

    @Test
    void testCreateMedico_AlreadyExists() throws Exception {
        Mockito.when(medicoService.existsMedico(1)).thenReturn(true);
        mockMvc.perform(post("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateMedico_Success() throws Exception {
        Mockito.when(medicoService.existsMedico(1)).thenReturn(true);
        Mockito.when(medicoService.saveMedico(any(Medico.class))).thenReturn(medico1);
        mockMvc.perform(put("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testUpdateMedico_NotFound() throws Exception {
        Mockito.when(medicoService.existsMedico(1)).thenReturn(false);
        mockMvc.perform(put("/api/v1/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMedico_Success() throws Exception {
        Mockito.when(medicoService.existsMedico(1)).thenReturn(true);
        Mockito.doNothing().when(medicoService).deleteMedico(1);
        mockMvc.perform(delete("/api/v1/medicos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMedico_NotFound() throws Exception {
        Mockito.when(medicoService.existsMedico(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/medicos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMedicoByRun_Found() throws Exception {
        Mockito.when(medicoService.findByRun("15.111.222-3")).thenReturn(Optional.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/por-run/15.111.222-3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testGetMedicoByRun_NotFound() throws Exception {
        Mockito.when(medicoService.findByRun(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/medicos/por-run/00.000.000-0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarMedicos_WithParam() throws Exception {
        Mockito.when(medicoService.buscarPorNombreOApellido("Soto")).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/buscar").param("texto", "Soto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testBuscarMedicos_WithoutParam() throws Exception {
        Mockito.when(medicoService.getAllMedicos()).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/buscar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetMedicosConMenosDeAniosAntiguedad_Success() throws Exception {
        Mockito.when(medicoService.buscarMedicosConMenosDeAniosAntiguedad(20)).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/antiguedad/menor-que").param("anios", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetMedicosConMenosDeAniosAntiguedad_BadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/medicos/antiguedad/menor-que").param("anios", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El número de años debe ser positivo"));
    }

    @Test
    void testGetMedicosConMasDeAniosAntiguedad_Success() throws Exception {
        Mockito.when(medicoService.buscarMedicosConMasDeAniosAntiguedad(10)).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/antiguedad/mayor-que").param("anios", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }

    @Test
    void testBuscarPorNombreAndApellido() throws Exception {
        Mockito.when(medicoService.findByNombreAndApellido("Carlos", "Soto")).thenReturn(List.of(medico1));
        mockMvc.perform(get("/api/v1/medicos/buscar-por-nombre-apellido")
                        .param("nombre", "Carlos")
                        .param("apellido", "Soto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testObtenerSueldoTotal_Success() throws Exception {
        Mockito.when(medicoService.calcularSueldoTotal(1)).thenReturn(950000.0f);
        mockMvc.perform(get("/api/v1/medicos/sueldo-total").param("medicoId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("950000.0"));
    }

    @Test
    void testObtenerSueldoTotal_NotFound() throws Exception {
        Mockito.when(medicoService.calcularSueldoTotal(99)).thenThrow(new RuntimeException("Médico no encontrado"));
        mockMvc.perform(get("/api/v1/medicos/sueldo-total").param("medicoId", "99"))
                .andExpect(status().isNotFound());
    }
}