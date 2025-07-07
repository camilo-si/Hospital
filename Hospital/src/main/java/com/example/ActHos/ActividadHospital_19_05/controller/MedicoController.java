package com.example.ActHos.ActividadHospital_19_05.controller;

import com.example.ActHos.ActividadHospital_19_05.model.Medico;
import com.example.ActHos.ActividadHospital_19_05.service.MedicoService;
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
@RequestMapping("api/v1/medicos")
@Tag(name = "Médicos", description = "Operaciones para la gestión de médicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Operation(summary = "Obtener todos los médicos", description = "Retorna una lista con todos los médicos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medico.class))))
    })
    @GetMapping
    public ResponseEntity<List<Medico>> getAllMedicos(){
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }

    @Operation(summary = "Obtener un médico por ID", description = "Retorna un médico específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico encontrado",
                    content = @Content(schema = @Schema(implementation = Medico.class))),
            @ApiResponse(responseCode = "404", description = "Médico no encontrado")
    })
    @GetMapping("/{idMedico}")
    public ResponseEntity<Medico> getMedicoById(
            @Parameter(description = "ID del médico a buscar", required = true)
            @PathVariable int idMedico){
        Optional<Medico> medico = medicoService.getMedicoById(idMedico);
        return medico.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo médico", description = "Agrega un nuevo médico al sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médico creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Medico.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o el médico ya existe")
    })
    @PostMapping
    public ResponseEntity<Medico> createMedico(
            @Parameter(description = "Objeto del médico a crear.", required = true)
            @RequestBody Medico medico){
        if (medico.getIdMedico() != null && medicoService.existsMedico(medico.getIdMedico())) {
            return ResponseEntity.badRequest().build();
        }
        Medico newMedico = medicoService.saveMedico(medico);
        return new ResponseEntity<>(newMedico, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un médico existente", description = "Actualiza los datos de un médico existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Medico.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Médico no encontrado para actualizar")
    })
    @PutMapping
    public ResponseEntity<Medico> updateMedico(
            @Parameter(description = "Datos del médico a actualizar. El ID debe ser válido.", required = true)
            @RequestBody Medico medico){
        if (medico.getIdMedico() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!medicoService.existsMedico(medico.getIdMedico())) {
            return ResponseEntity.notFound().build();
        }
        Medico updatedMedico = medicoService.saveMedico(medico);
        return ResponseEntity.ok(updatedMedico);
    }

    @Operation(summary = "Eliminar un médico", description = "Elimina un médico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médico eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Médico no encontrado para eliminar")
    })
    @DeleteMapping("/{idMedico}")
    public ResponseEntity<Void> deleteMedico(
            @Parameter(description = "ID del médico a eliminar.", required = true)
            @PathVariable int idMedico){
        if (!medicoService.existsMedico(idMedico)) {
            return ResponseEntity.notFound().build();
        }
        medicoService.deleteMedico(idMedico);
        return ResponseEntity.noContent().build();
    }

    //Métodos Personalizados

    @Operation(summary = "Buscar médico por RUN",
            description = "Encuentra y retorna un único médico basado en su RUN único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Medico.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró un médico con el RUN proporcionado",
                    content = @Content)
    })
    @GetMapping("/por-run/{run}")
    public ResponseEntity<Medico> getMedicoByRun(
            @Parameter(description = "RUN del médico a buscar (con puntos y guion)", required = true, example = "15.111.222-3")
            @PathVariable String run) {
        return medicoService.findByRun(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar médicos por nombre o apellido",
            description = "Busca médicos cuyo nombre o apellido contenga el texto proporcionado. Si no se provee texto, retorna todos los médicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medico.class))))
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Medico>> buscarMedicos(
            @Parameter(description = "Texto a buscar en el nombre o apellido del médico. Es opcional.", required = false, example = "Soto")
            @RequestParam(required = false) String texto) {
        if(texto != null && !texto.isEmpty()) {
            return ResponseEntity.ok(medicoService.buscarPorNombreOApellido(texto));
        }
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }

    @Operation(summary = "Buscar médicos con menos de X años de antigüedad",
            description = "Obtiene una lista de médicos cuya fecha de contratación sea menor a la cantidad de años especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos con menos años de antigüedad",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medico.class)))),
            @ApiResponse(responseCode = "400", description = "El número de años proporcionado no es válido",
                    content = @Content(schema = @Schema(implementation = String.class, example = "El número de años debe ser positivo")))
    })
    @GetMapping("/antiguedad/menor-que")
    public ResponseEntity<?> getMedicosConMenosDeAniosAntiguedad(
            @Parameter(description = "Número máximo de años de antigüedad", required = true, example = "5")
            @RequestParam int anios) {
        if (anios < 0) {
            return ResponseEntity.badRequest().body("El número de años debe ser positivo");
        }
        return ResponseEntity.ok(medicoService.buscarMedicosConMenosDeAniosAntiguedad(anios));
    }

    @Operation(summary = "Buscar médicos con más de X años de antigüedad",
            description = "Obtiene una lista de médicos cuya fecha de contratación sea mayor a la cantidad de años especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos con más años de antigüedad",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medico.class)))),
            @ApiResponse(responseCode = "400", description = "El número de años proporcionado no es válido",
                    content = @Content(schema = @Schema(implementation = String.class, example = "El número de años debe ser positivo")))
    })
    @GetMapping("/antiguedad/mayor-que")
    public ResponseEntity<?> getMedicosConMasDeAniosAntiguedad(
            @Parameter(description = "Número mínimo de años de antigüedad", required = true, example = "10")
            @RequestParam int anios) {
        if (anios < 0) {
            return ResponseEntity.badRequest().body("El número de años debe ser positivo");
        }
        return ResponseEntity.ok(medicoService.buscarMedicosConMasDeAniosAntiguedad(anios));
    }

    @Operation(summary = "Buscar médicos por nombre y apellido exactos",
            description = "Retorna una lista de médicos que coinciden exactamente con el nombre y apellido proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de médicos que coinciden",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Medico.class))))
    })
    @GetMapping("/buscar-por-nombre-apellido")
    public ResponseEntity<List<Medico>> BuscarPorNombreAndApellido(
            @Parameter(description = "Nombre exacto del médico", required = true, example = "Carlos")
            @RequestParam String nombre,
            @Parameter(description = "Apellido exacto del médico", required = true, example = "Soto")
            @RequestParam String apellido) {
        return ResponseEntity.ok(medicoService.findByNombreAndApellido(nombre,apellido));
    }

    @Operation(summary = "Calcular sueldo total de un médico",
            description = "Calcula y retorna el sueldo total de un médico específico, basado en su sueldo base y otros factores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sueldo total calculado exitosamente",
                    content = @Content(schema = @Schema(implementation = Float.class, example = "2550000.50"))),
            @ApiResponse(responseCode = "404", description = "Médico no encontrado con el ID proporcionado",
                    content = @Content)
    })
    @GetMapping("/sueldo-total")
    public ResponseEntity<Float> obtenerSueldoTotal(
            @Parameter(description = "ID del médico para calcular su sueldo", required = true, example = "1")
            @RequestParam Integer medicoId) {
        try {
            return ResponseEntity.ok(medicoService.calcularSueldoTotal(medicoId));
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}