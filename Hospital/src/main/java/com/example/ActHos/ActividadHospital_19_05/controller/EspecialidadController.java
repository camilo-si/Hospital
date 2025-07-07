package com.example.ActHos.ActividadHospital_19_05.controller;

import com.example.ActHos.ActividadHospital_19_05.model.Especialidad;
import com.example.ActHos.ActividadHospital_19_05.service.EspecialidadService;
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
@RequestMapping("api/v1/especialidades")
@Tag(name = "Especialidades", description = "Operaciones relacionadas con las especialidades médicas")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @Operation(summary = "Obtener todas las especialidades", description = "Retorna una lista con todas las especialidades registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Especialidad.class))))
    })
    @GetMapping
    public ResponseEntity<List<Especialidad>> getAllEspecialidads(){
        return ResponseEntity.ok(especialidadService.getAllEspecialidads());
    }

    @Operation(summary = "Obtener una especialidad por ID", description = "Retorna una especialidad específica dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada",
                    content = @Content(schema = @Schema(implementation = Especialidad.class))),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @GetMapping("/{idEspecialidad}")
    public ResponseEntity<Especialidad> getEspecialidadById(
            @Parameter(description = "ID de la especialidad a buscar", required = true)
            @PathVariable int idEspecialidad){
        Optional<Especialidad> especialidad = especialidadService.getEspecialidadById(idEspecialidad);
        return especialidad.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva especialidad", description = "Agrega una nueva especialidad al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Especialidad.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o la especialidad ya existe")
    })
    @PostMapping
    public ResponseEntity<Especialidad> createEspecialidad(
            @Parameter(description = "Objeto de la especialidad a crear", required = true)
            @RequestBody Especialidad especialidad){
        if(especialidad.getIdEspecialidad() != null && especialidadService.existsEspecialidad(especialidad.getIdEspecialidad())){
            return ResponseEntity.badRequest().build();
        }
        Especialidad newEspecialidad = especialidadService.saveEspecialidad(especialidad);
        return new ResponseEntity<>(newEspecialidad, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una especialidad existente", description = "Actualiza los datos de una especialidad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Especialidad.class))),
            @ApiResponse(responseCode = "400", description = "Datos de especialidad inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada para actualizar")
    })
    @PutMapping
    public ResponseEntity<Especialidad> updateEspecialidad(
            @Parameter(description = "Datos de la especialidad a actualizar. El ID debe ser válido.", required = true)
            @RequestBody Especialidad especialidad){
        if (especialidad.getIdEspecialidad() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!especialidadService.existsEspecialidad(especialidad.getIdEspecialidad())) {
            return ResponseEntity.notFound().build();
        }
        Especialidad updatedEspecialidad = especialidadService.saveEspecialidad(especialidad);
        return ResponseEntity.ok(updatedEspecialidad);
    }

    @Operation(summary = "Eliminar una especialidad", description = "Elimina una especialidad dado su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especialidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada para eliminar")
    })
    @DeleteMapping("/{idEspecialidad}")
    public ResponseEntity<Void> deleteEspecialidad(
            @Parameter(description = "ID de la especialidad a eliminar", required = true)
            @PathVariable int idEspecialidad){
        if (!especialidadService.existsEspecialidad(idEspecialidad)) {
            return ResponseEntity.notFound().build();
        }
        especialidadService.deleteEspecialidad(idEspecialidad);
        return ResponseEntity.noContent().build();
    }
}
