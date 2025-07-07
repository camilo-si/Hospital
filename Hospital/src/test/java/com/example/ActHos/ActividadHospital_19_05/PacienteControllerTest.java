package com.example.ActHos.ActividadHospital_19_05;

import com.example.ActHos.ActividadHospital_19_05.controller.PacienteController;
import com.example.ActHos.ActividadHospital_19_05.model.Atencion;
import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import com.example.ActHos.ActividadHospital_19_05.model.Prevision;
import com.example.ActHos.ActividadHospital_19_05.repository.AtencionRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.PacienteRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.PrevisionRepository;
import com.example.ActHos.ActividadHospital_19_05.service.PacienteService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private AtencionRepository atencionRepository;
    @MockBean
    private PacienteRepository pacienteRepository;
    @MockBean
    private PrevisionRepository previsionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Paciente paciente1;

    @BeforeEach
    void setUp() {
        paciente1 = new Paciente();
        paciente1.setIdPaciente(1);
        paciente1.setNombre("Ana");
        paciente1.setApellido("González");
        paciente1.setRun("20.111.222-K");
        paciente1.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        paciente1.setCorreo("test@test.com");
        paciente1.setTelefono("+56912345678");

        Prevision prevision = new Prevision();
        prevision.setCobertura(80);
        paciente1.setPrevision(prevision);
    }

    @Test
    void testGetAllPacientes() throws Exception {
        Mockito.when(pacienteService.getAllPacientes()).thenReturn(List.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void testGetPacienteById_Found() throws Exception {
        Mockito.when(pacienteService.getPacienteById(1)).thenReturn(Optional.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPaciente").value(1));
    }

    @Test
    void testGetPacienteById_NotFound() throws Exception {
        Mockito.when(pacienteService.getPacienteById(99)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePaciente() throws Exception {
        Mockito.when(pacienteService.savePaciente(any(Paciente.class))).thenReturn(paciente1);
        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.run").value("20.111.222-K"));
    }

    @Test
    void testCreatePaciente_AlreadyExists() throws Exception {
        Mockito.when(pacienteService.existsPaciente(1)).thenReturn(true);
        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePaciente_Success() throws Exception {
        Mockito.when(pacienteService.existsPaciente(1)).thenReturn(true);
        Mockito.when(pacienteService.savePaciente(any(Paciente.class))).thenReturn(paciente1);
        mockMvc.perform(put("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void testUpdatePaciente_NotFound() throws Exception {
        Mockito.when(pacienteService.existsPaciente(1)).thenReturn(false);
        mockMvc.perform(put("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePaciente_Success() throws Exception {
        Mockito.when(pacienteService.existsPaciente(1)).thenReturn(true);
        Mockito.doNothing().when(pacienteService).deletePaciente(1);
        mockMvc.perform(delete("/api/v1/pacientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePaciente_NotFound() throws Exception {
        Mockito.when(pacienteService.existsPaciente(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarByRut_Found() throws Exception {
        Mockito.when(pacienteService.findByRun("20.111.222-K")).thenReturn(paciente1);
        mockMvc.perform(get("/api/v1/pacientes/buscar-por-run/20.111.222-K"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void testBuscarByRut_NotFound() throws Exception {
        Mockito.when(pacienteService.findByRun(anyString())).thenReturn(null);
        mockMvc.perform(get("/api/v1/pacientes/buscar-por-run/00.000.000-0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarPorNombreYApellido_Found() throws Exception {
        Mockito.when(pacienteService.findByNombreAndApellido("Ana", "González")).thenReturn(List.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes/buscar-por-nombre-apellido")
                        .param("nombre", "Ana")
                        .param("apellido", "González"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testBuscarPorNombreYApellido_NoContent() throws Exception {
        Mockito.when(pacienteService.findByNombreAndApellido(anyString(), anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/pacientes/buscar-por-nombre-apellido")
                        .param("nombre", "Test")
                        .param("apellido", "Test"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPacientesYoungerThan_Found() throws Exception {
        Mockito.when(pacienteService.findPatientsYoungerThan(40)).thenReturn(List.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes/menores-a/40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetByPrevisionId_Found() throws Exception {
        Mockito.when(pacienteService.obtenerPorIdPrevision(1)).thenReturn(List.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes/por-id-prevision/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetPacientesPorEspecialidad_Found() throws Exception {
        Mockito.when(pacienteService.obtenerPacientesPorEspecialidad("Cardiología")).thenReturn(List.of(paciente1));
        mockMvc.perform(get("/api/v1/pacientes/por-especialidad").param("nombre", "Cardiología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetCostoTotal_Success() throws Exception {
        Atencion atencion1 = new Atencion();
        atencion1.setCosto(50000);
        Atencion atencion2 = new Atencion();
        atencion2.setCosto(100000);

        Mockito.when(pacienteRepository.findById(1)).thenReturn(Optional.of(this.paciente1));
        Mockito.when(atencionRepository.findtotalcostoByPacienteId(1)).thenReturn(List.of(atencion1, atencion2));

        mockMvc.perform(get("/api/v1/pacientes/costo-total/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("30000.0"));
    }

    @Test
    void testGetCostoTotal_PacienteNotFound() throws Exception {
        Mockito.when(pacienteRepository.findById(99)).thenThrow(new RuntimeException("Paciente no encontrado"));
        mockMvc.perform(get("/api/v1/pacientes/costo-total/99"))
                .andExpect(status().isNotFound());
    }
}