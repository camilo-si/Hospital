package com.example.ActHos.ActividadHospital_19_05.controller;

import com.example.ActHos.ActividadHospital_19_05.model.Prevision;
import com.example.ActHos.ActividadHospital_19_05.service.PrevisionService;
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
@RequestMapping("api/v1/previsiones")
@Tag(name = "Previsiones de Salud", description = "Operaciones para gestionar los tipos de previsiones de salud (ej. Fonasa, Isapre)")
public class PrevisionController {

    @Autowired
    private PrevisionService previsionService;

    @Operation(summary = "Obtener todas las previsiones de salud", description = "Retorna una lista con todas las previsiones de salud registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de previsiones obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Prevision.class))))
    })
    @GetMapping
    public ResponseEntity<List<Prevision>> getAllPrevisions(){
        return ResponseEntity.ok(previsionService.getAllPrevisions());
    }

    @Operation(summary = "Obtener una previsión por ID", description = "Retorna una previsión de salud específica según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Previsión encontrada",
                    content = @Content(schema = @Schema(implementation = Prevision.class))),
            @ApiResponse(responseCode = "404", description = "Previsión no encontrada")
    })
    @GetMapping("/{idPrevision}")
    public ResponseEntity<Prevision> getPrevisionById(
            @Parameter(description = "ID de la previsión a buscar", required = true)
            @PathVariable int idPrevision){
        Optional<Prevision> prevision = previsionService.getPrevisionById(idPrevision);
        return prevision.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva previsión de salud", description = "Agrega una nueva previsión de salud al sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Previsión creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Prevision.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o la previsión ya existe")
    })
    @PostMapping
    public ResponseEntity<Prevision> createPrevision(
            @Parameter(description = "Objeto de la previsión a crear.", required = true)
            @RequestBody Prevision prevision){
        if (prevision.getIdPrevision() != null && previsionService.existsPrevision(prevision.getIdPrevision())) {
            return ResponseEntity.badRequest().build();
        }
        Prevision newPrevision = previsionService.savePrevision(prevision);
        return new ResponseEntity<>(newPrevision, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una previsión de salud existente", description = "Actualiza los datos de una previsión de salud existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Previsión actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Prevision.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Previsión no encontrada para actualizar")
    })
    @PutMapping
    public ResponseEntity<Prevision> updatePrevision(
            @Parameter(description = "Datos de la previsión a actualizar. El ID debe ser válido.", required = true)
            @RequestBody Prevision prevision){
        if (prevision.getIdPrevision() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!previsionService.existsPrevision(prevision.getIdPrevision())) {
            return ResponseEntity.notFound().build();
        }
        Prevision updatedPrevision = previsionService.savePrevision(prevision);
        return ResponseEntity.ok(updatedPrevision);
    }

    @Operation(summary = "Eliminar una previsión de salud", description = "Elimina una previsión de salud dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Previsión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Previsión no encontrada para eliminar")
    })
    @DeleteMapping("/{idPrevision}")
    public ResponseEntity<Void> deletePrevision(
            @Parameter(description = "ID de la previsión a eliminar.", required = true)
            @PathVariable int idPrevision){
        if (!previsionService.existsPrevision(idPrevision)) {
            return ResponseEntity.notFound().build();
        }
        previsionService.deletePrevision(idPrevision);
        return ResponseEntity.noContent().build();
    }
}