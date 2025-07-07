package com.example.ActHos.ActividadHospital_19_05.controller;

import com.example.ActHos.ActividadHospital_19_05.model.Atencion;
import com.example.ActHos.ActividadHospital_19_05.model.Estado;
import com.example.ActHos.ActividadHospital_19_05.service.AtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("api/v1/atenciones")
@Tag(name = "Atenciones", description = "Operaciones relacionadas con las atenciones hospitalarias")
public class AtencionController {
    @Autowired
    private AtencionService atencionService;

    @Operation(summary = "Obtener todas las atenciones", description = "Retorna una lista con todas las atenciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))})
    @GetMapping
    public ResponseEntity<List<Atencion>> getAllAtencions(){
        return ResponseEntity.ok(atencionService.getAllAtencions());
    }

    @Operation(summary = "Obtener una atención por código", description = "Retorna una atención específica dado su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atención encontrada",
                    content = @Content(schema = @Schema(implementation = Atencion.class))),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada")})
    @GetMapping("/{idAtencion}")
    public ResponseEntity<Atencion> getAtencionById(
            @Parameter(description = "Código de la atención a buscar", required = true)
            @PathVariable int idAtencion){
        Optional<Atencion> atencion = atencionService.getAtencionById(idAtencion);
        return atencion.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva atención", description = "Agrega una nueva atención al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atención creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Atencion.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o la atención ya existe")
    })
    @PostMapping
    public ResponseEntity<Atencion> createAtencion(
            @Parameter(description = "Objeto de la atención a crear. El ID puede ser omitido o ser único.", required = true)
            @RequestBody Atencion atencion){
        // La lógica puede variar, pero asumimos que no se puede crear una con un ID que ya existe.
        if(atencion.getIdAtencion() != null && atencionService.existsAtencion(atencion.getIdAtencion())){
            return ResponseEntity.badRequest().build();
        }
        Atencion newAtencion = atencionService.saveAtencion(atencion);
        return new ResponseEntity<>(newAtencion, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una atención existente", description = "Actualiza los datos de una atención existente en el sistema.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Atención actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Atencion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de atención inválidos (ej. ID nulo)"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada para actualizar")})
    @PutMapping
    public ResponseEntity<Atencion> updateAtencion(
            @Parameter(description = "Datos de la atención a actualizar. El ID debe corresponder a una atención existente.", required = true)
            @RequestBody Atencion atencion){
        if (atencion.getIdAtencion() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!atencionService.existsAtencion(atencion.getIdAtencion())) {
            return ResponseEntity.notFound().build();
        }
        Atencion updatedAtencion = atencionService.saveAtencion(atencion);
        return ResponseEntity.ok(updatedAtencion);
    }

    @Operation(summary = "Eliminar una atención", description = "Elimina una atención dado su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atención eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Atención no encontrada para eliminar")
    })
    @DeleteMapping("/{idAtencion}")
    public ResponseEntity<Void> deleteAtencion(
            @Parameter(description = "Código de la atención a eliminar", required = true)
            @PathVariable Integer idAtencion){
        if (!atencionService.existsAtencion(idAtencion)) {
            return ResponseEntity.notFound().build();
        }
        atencionService.deleteAtencion(idAtencion);
        return ResponseEntity.noContent().build();
    }



    // Métodos Personalizados

    @Operation(summary = "Buscar atenciones por fecha específica",
            description = "Obtiene una lista de todas las atenciones registradas en una fecha determinada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/por-fecha")
    public ResponseEntity<List<Atencion>> getAtencionesByFecha(
            @Parameter(description = "Fecha para filtrar las atenciones en formato AAAA-MM-DD", required = true, example = "2025-06-26")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Atencion> atenciones = atencionService.getAtencionesByFecha(fecha);
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para buscar atenciones entre fechas
    @Operation(summary = "Buscar atenciones en un rango de fechas",
            description = "Obtiene una lista de atenciones registradas entre una fecha de inicio y una fecha de fin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/entre-fechas")
    public ResponseEntity<List<Atencion>> getAtencionesBetweenFechas(
            @Parameter(description = "Fecha de inicio del rango (AAAA-MM-DD)", required = true, example = "2025-06-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(description = "Fecha de fin del rango (AAAA-MM-DD)", required = true, example = "2025-06-30")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Atencion> atenciones = atencionService.getAtencionesBetweenFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para buscar atenciones con costo menor a un valor
    @Operation(summary = "Buscar atenciones con costo menor a un valor",
            description = "Obtiene una lista de atenciones cuyo costo sea menor al valor especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/costo-menor-que")
    public ResponseEntity<List<Atencion>> getAtencionesCostoMenorQue(
            @Parameter(description = "Valor máximo (no inclusivo) del costo para filtrar", required = true, example = "50000")
            @RequestParam int cantidad) {
        List<Atencion> atenciones = atencionService.getAtencionesCostoMenorQue(cantidad);
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para buscar atenciones con costo mayor a un valor
    @Operation(summary = "Buscar atenciones con costo mayor a un valor",
            description = "Obtiene una lista de atenciones cuyo costo sea mayor al valor especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/costo-mayor-que")
    public ResponseEntity<List<Atencion>> getAtencionesCostoMayorQue(
            @Parameter(description = "Valor mínimo (no inclusivo) del costo para filtrar", required = true, example = "100000")
            @RequestParam int cantidad) {
        List<Atencion> atenciones = atencionService.getAtencionesCostoMayorQue(cantidad);
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para buscar atenciones por médico
    @Operation(summary = "Buscar atenciones por médico",
            description = "Obtiene todas las atenciones realizadas por un médico específico, identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class)))),
            @ApiResponse(responseCode = "204", description = "El médico no tiene atenciones registradas o no existe", content = @Content)
    })
    @GetMapping("/por-medico/{idMedico}")
    public ResponseEntity<List<Atencion>> getAtencionesByMedico(
            @Parameter(description = "ID del médico para obtener sus atenciones", required = true, example = "1")
            @PathVariable Integer idMedico) {
        List<Atencion> atenciones = atencionService.getAtencionesByMedicoId(idMedico);
        if (atenciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para el reporte de ganancias del hospital
    @Operation(summary = "Calcular ganancia total del hospital por altas",
            description = "Calcula y retorna el costo total sumado de todas las atenciones cuyo estado sea 'Alta'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ganancia total calculada exitosamente",
                    content = @Content(schema = @Schema(implementation = Double.class, example = "2550000.75")))
    })
    @GetMapping("/reportes/ganancias-hospital")
    public ResponseEntity<Double> getGananciaTotalAltas() {
        Double ganancia = atencionService.getGananciaTotalPorAltas();
        return ResponseEntity.ok(ganancia != null ? ganancia : 0.0);
    }

    // Documentación para consultar atenciones por paciente
    @Operation(summary = "Consultar atenciones por paciente",
            description = "Obtiene el historial completo de atenciones para un paciente específico, identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones del paciente obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/por-paciente/{id}")
    public ResponseEntity<List<Atencion>> getAtencionesPorPaciente(
            @Parameter(description = "ID del paciente para obtener su historial de atenciones", required = true, example = "1")
            @PathVariable Integer id) {
        List<Atencion> atenciones = atencionService.obtenerAtencionesPorPaciente(id);
        return ResponseEntity.ok(atenciones);
    }

    // Documentación para mostrar atenciones por estado
    @Operation(summary = "Buscar atenciones por estado",
            description = "Obtiene una lista de atenciones que coinciden con un estado determinado (ej. 'Alta', 'Hospitalizado').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de atenciones encontrada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Atencion.class))))
    })
    @GetMapping("/por-estado")
    public ResponseEntity<List<Atencion>> getAtencionesPorEstado(
            @Parameter(description = "Nombre del estado para filtrar las atenciones", required = true, example = "Alta")
            @RequestParam String estado) {
        List<Atencion> atenciones = atencionService.obtenerAtencionesPorEstado(estado);
        return ResponseEntity.ok(atenciones);
    }


    // Documentación para el reporte de total a pagar por un paciente
    @Operation(summary = "Calcular el total a pagar por un paciente",
            description = "Calcula el costo total sumado de todas las atenciones de un paciente específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo del total a pagar obtenido exitosamente",
                    content = @Content(schema = @Schema(
                            implementation = Map.class,
                            example = "{\"paciente\": \"Ana González\", \"totalAPagar\": 75000.0, \"cantidadAtenciones\": 3}"
                    ))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    @GetMapping("/total-a-pagar/{idPaciente}")
    public ResponseEntity<Map<String, Object>> getTotalPago(
            @Parameter(description = "ID del paciente para calcular su total a pagar", required = true, example = "1")
            @PathVariable Integer idPaciente) {
        // Asumiendo que el servicio puede lanzar una excepción si el paciente no existe,
        // que sería manejada por un @ControllerAdvice para devolver 404.
        return ResponseEntity.ok(atencionService.calcularTotalPagoPorPaciente(idPaciente));
    }
}



