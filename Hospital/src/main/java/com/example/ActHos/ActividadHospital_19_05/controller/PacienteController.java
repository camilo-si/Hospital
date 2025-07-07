package com.example.ActHos.ActividadHospital_19_05.controller;
import com.example.ActHos.ActividadHospital_19_05.model.Atencion;
import com.example.ActHos.ActividadHospital_19_05.repository.AtencionRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.PacienteRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.PrevisionRepository;
import com.example.ActHos.ActividadHospital_19_05.service.PacienteService;
import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/pacientes")
@Tag(name = "Pacientes", description = "Operaciones para la gestión de pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

     @Autowired
    private AtencionRepository atencionRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
     private PrevisionRepository previsionRepository;

    @Operation(summary = "Obtener todos los pacientes", description = "Retorna una lista con todos los pacientes registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class))))
    })
    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes(){
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @Operation(summary = "Obtener un paciente por ID", description = "Retorna un paciente específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/{idPaciente}")
    public ResponseEntity<Paciente> getPacienteById(
            @Parameter(description = "ID del paciente a buscar", required = true)
            @PathVariable int idPaciente){
        Optional<Paciente> paciente = pacienteService.getPacienteById(idPaciente);
        return paciente.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo paciente", description = "Agrega un nuevo paciente al sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o el paciente ya existe")
    })
    @PostMapping
    public ResponseEntity<Paciente> createPaciente(
            @Parameter(description = "Objeto del paciente a crear.", required = true)
            @RequestBody Paciente paciente) {
        if (paciente.getIdPaciente() != null && pacienteService.existsPaciente(paciente.getIdPaciente())) {
            return ResponseEntity.badRequest().build();
        }
        Paciente newPaciente = pacienteService.savePaciente(paciente);
        return new ResponseEntity<>(newPaciente, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un paciente existente", description = "Actualiza los datos de un paciente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado para actualizar")
    })
    @PutMapping
    public ResponseEntity<Paciente> updatePaciente(
            @Parameter(description = "Datos del paciente a actualizar. El ID debe ser válido.", required = true)
            @RequestBody Paciente paciente){
        if (paciente.getIdPaciente() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!pacienteService.existsPaciente(paciente.getIdPaciente())) {
            return ResponseEntity.notFound().build();
        }
        Paciente updatedPaciente = pacienteService.savePaciente(paciente);
        return ResponseEntity.ok(updatedPaciente);
    }

    @Operation(summary = "Eliminar un paciente", description = "Elimina un paciente dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado para eliminar")
    })
    @DeleteMapping("/{idPaciente}")
    public ResponseEntity<Void> deletePaciente(
            @Parameter(description = "ID del paciente a eliminar.", required = true)
            @PathVariable int idPaciente){
        if (!pacienteService.existsPaciente(idPaciente)) {
            return ResponseEntity.notFound().build();
        }
        pacienteService.deletePaciente(idPaciente);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar paciente por RUN", description = "Encuentra y retorna un único paciente basado en su RUN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Paciente.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el RUN proporcionado",
                    content = @Content)
    })
    @GetMapping("/buscar-por-run/{run}")
    public ResponseEntity<Paciente> BuscarByRut(
            @Parameter(description = "RUN del paciente a buscar (con puntos y guion)", required = true, example = "20.123.456-7")
            @PathVariable String run) {
        Paciente paciente = pacienteService.findByRun(run);
        if (paciente == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(paciente);
    }

    @Operation(summary = "Buscar pacientes por nombre y apellido", description = "Obtiene una lista de pacientes que coinciden con un nombre y apellido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes con ese nombre y apellido",
                    content = @Content)
    })
    @GetMapping("/buscar-por-nombre-apellido")
    public ResponseEntity<List<Paciente>> buscarPorNombreYApellido(
            @Parameter(description = "Nombre a buscar en pacientes", required = true, example = "Ana")
            @RequestParam String nombre,
            @Parameter(description = "Apellido a buscar en pacientes", required = true, example = "González")
            @RequestParam String apellido) {
        List<Paciente> pacientes = pacienteService.findByNombreAndApellido(nombre, apellido);
        return pacientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Buscar pacientes menores a una edad", description = "Obtiene una lista de pacientes cuya edad es menor a la especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes menores a la edad especificada",
                    content = @Content)
    })
    @GetMapping("/menores-a/{edad}")
    public ResponseEntity<List<Paciente>> getPacientesYoungerThan(
            @Parameter(description = "Edad máxima para la búsqueda", required = true, example = "18")
            @PathVariable int edad) {
        List<Paciente> pacientes = pacienteService.findPatientsYoungerThan(edad);
        return pacientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Buscar pacientes mayores a una edad", description = "Obtiene una lista de pacientes cuya edad es mayor a la especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes mayores a la edad especificada",
                    content = @Content)
    })
    @GetMapping("/mayores-a/{edad}")
    public ResponseEntity<List<Paciente>> getPacientesOlderThan(
            @Parameter(description = "Edad mínima para la búsqueda", required = true, example = "65")
            @PathVariable int edad) {
        List<Paciente> pacientes = pacienteService.findPatientsOlderThan(edad);
        return pacientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Buscar pacientes por ID de previsión", description = "Obtiene una lista de pacientes asociados a un ID de previsión de salud específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes con esa previsión",
                    content = @Content)
    })
    @GetMapping("/por-id-prevision/{id}")
    public ResponseEntity<List<Paciente>> getByPrevisionId(
            @Parameter(description = "ID de la previsión de salud (ej. 1 para Fonasa)", required = true, example = "1")
            @PathVariable int id) {
        List<Paciente> pacientes = pacienteService.obtenerPorIdPrevision(id);
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    // NOTA: Este endpoint parece redundante con el anterior y el parámetro 'nombre' no se usa. Se documenta como está.
    @Operation(summary = "Buscar pacientes por ID de previsión (con filtro opcional)",
            description = "Obtiene una lista de pacientes asociados a un ID de previsión. Nota: El filtro opcional por nombre no está implementado en la lógica actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes con esa previsión", content = @Content)
    })
    @GetMapping("/por-prevision/{previsionId}")
    public ResponseEntity<List<Paciente>> getPacientesPorPrevision(
            @Parameter(description = "ID de la previsión de salud", required = true, example = "2")
            @PathVariable("previsionId") int id,
            @Parameter(description = "Filtro opcional por nombre (no implementado actualmente)", required = false)
            @RequestParam(required = false) String nombre) {
        List<Paciente> pacientes = pacienteService.obtenerPorIdPrevision(id);
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Buscar pacientes atendidos en una especialidad", description = "Obtiene una lista de pacientes que han tenido al menos una atención en la especialidad médica especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paciente.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron pacientes atendidos en esa especialidad",
                    content = @Content)
    })
    @GetMapping("/por-especialidad")
    public ResponseEntity<List<Paciente>> getPacientesPorEspecialidad(
            @Parameter(description = "Nombre de la especialidad médica", required = true, example = "Cardiología")
            @RequestParam String nombre) {
        List<Paciente> pacientes = pacienteService.obtenerPacientesPorEspecialidad(nombre);
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Calcular costo total a pagar por un paciente", description = "Calcula el costo total de todas las atenciones de un paciente, aplicando el descuento de su previsión de salud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Costo total a pagar calculado exitosamente",
                    content = @Content(schema = @Schema(implementation = Double.class, example = "157500.0"))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado con el ID proporcionado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al calcular el costo",
                    content = @Content)
    })
    @GetMapping("/costo-total/{id_paciente}")
    public ResponseEntity<Double> getCostoTotal(
            @Parameter(description = "ID del paciente para el cálculo", required = true, example = "1")
            @PathVariable("id_paciente") Integer pacienteId) {
        try {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            List<Atencion> atenciones = atencionRepository.findtotalcostoByPacienteId(pacienteId);
            double costoTotal = atenciones.stream()
                    .mapToDouble(Atencion::getCosto)
                    .sum();
            double descuento = costoTotal * (paciente.getPrevision().getCobertura() / 100.0);
            double totalAPagar = costoTotal - descuento;
            return ResponseEntity.ok(Math.round(totalAPagar * 100.0) / 100.0);
        } catch (RuntimeException e) { // Captura el "Paciente no encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) { // Captura cualquier otro error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
