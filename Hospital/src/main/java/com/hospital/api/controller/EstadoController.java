package com.hospital.api.controller;

import com.hospital.api.model.Estado;
import com.hospital.api.service.EstadoService;
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
@RequestMapping("api/v1/estados")
@Tag(name = "Estados de Salud", description = "Operaciones para gestionar los estados de salud de los pacientes (ej. Hospitalizado, Alta, etc.)")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @Operation(summary = "Obtener todos los estados de salud", description = "Retorna una lista con todos los estados de salud disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados de salud obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Estado.class))))
    })
    @GetMapping
    public ResponseEntity<List<Estado>> getAllEstados(){
        return ResponseEntity.ok(estadoService.getAllEstados());
    }

    @Operation(summary = "Obtener un estado de salud por ID", description = "Retorna un estado de salud específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de salud encontrado",
                    content = @Content(schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "404", description = "Estado de salud no encontrado")
    })
    @GetMapping("/{idEstado}")
    public ResponseEntity<Estado> getEstadoById(
            @Parameter(description = "ID del estado de salud a buscar", required = true)
            @PathVariable int idEstado){
        Optional<Estado> estado = estadoService.getEstadoById(idEstado);
        return estado.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo estado de salud", description = "Agrega un nuevo estado de salud al sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado de salud creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o el estado de salud ya existe")
    })
    @PostMapping
    public ResponseEntity<Estado> createEstado(
            @Parameter(description = "Objeto del estado de salud a crear.", required = true)
            @RequestBody Estado estado){
        if(estado.getIdEstado() != null && estadoService.existsEstado(estado.getIdEstado())){
            return ResponseEntity.badRequest().build();
        }
        Estado newEstado = estadoService.saveEstado(estado);
        return new ResponseEntity<>(newEstado, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un estado de salud existente", description = "Actualiza los datos de un estado de salud existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de salud actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Estado de salud no encontrado para actualizar")
    })
    @PutMapping
    public ResponseEntity<Estado> updateEstado(
            @Parameter(description = "Datos del estado de salud a actualizar. El ID debe ser válido.", required = true)
            @RequestBody Estado estado){
        if (estado.getIdEstado() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!estadoService.existsEstado(estado.getIdEstado())) {
            return ResponseEntity.notFound().build();
        }
        Estado updatedEstado = estadoService.saveEstado(estado);
        return ResponseEntity.ok(updatedEstado);
    }

    @Operation(summary = "Eliminar un estado de salud", description = "Elimina un estado de salud dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estado de salud eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estado de salud no encontrado para eliminar")
    })
    @DeleteMapping("/{idEstado}")
    public ResponseEntity<Void> deleteEstado(
            @Parameter(description = "ID del estado de salud a eliminar.", required = true)
            @PathVariable int idEstado){
        if (!estadoService.existsEstado(idEstado)) {
            return ResponseEntity.notFound().build();
        }
        estadoService.deleteEstado(idEstado);
        return ResponseEntity.noContent().build();
    }
}
