package com.hospital.api.config;

import com.hospital.api.model.*;
import com.hospital.api.repository.*;
import com.hospital.api.model.*;
import com.hospital.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Inicializador implements CommandLineRunner {
    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private PrevisionRepository previsionRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AtencionRepository atencionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicialización de Estados
        Estado estado1 = new Estado();
        estado1.setNombre("PENDIENTE");
        estado1.setDescripcion("Paciente a espera de atención");

        Estado estado2 = new Estado();
        estado2.setNombre("HOSPITALIZADO");
        estado2.setDescripcion("Paciente hospitalizado y en observación");

        Estado estado3 = new Estado();
        estado3.setNombre("ALTA");
        estado3.setDescripcion("Paciente en estado libre para irse a casa");

        // Inicialización de Especialidades
        Especialidad especialidad1 = new Especialidad();
        especialidad1.setNombre("Medicina General");
        especialidad1.setDescripcion("Medicina general para toda la familia.");

        Especialidad especialidad2 = new Especialidad();
        especialidad2.setNombre("Pediatría");
        especialidad2.setDescripcion("Especialidad médica dedicada a la atención de niños y adolescentes.");

        Especialidad especialidad3 = new Especialidad();
        especialidad3.setNombre("Cardiología");
        especialidad3.setDescripcion("Diagnóstico y tratamiento de enfermedades del corazón y el sistema circulatorio.");

        Especialidad especialidad4 = new Especialidad();
        especialidad4.setNombre("Dermatología");
        especialidad4.setDescripcion("Diagnóstico y tratamiento de enfermedades de la piel.");

        // Inicialización de Previsiones
        Prevision prevision1 = new Prevision();
        prevision1.setNombre("FONASA");
        prevision1.setCobertura(50);

        Prevision prevision2 = new Prevision();
        prevision2.setNombre("ISAPRE");
        prevision2.setCobertura(60);

        // Guardar datos básicos solo si no existen
        if (estadoRepository.count() == 0) {
            estado1 = estadoRepository.save(estado1);
            estado2 = estadoRepository.save(estado2);
            estado3 = estadoRepository.save(estado3);
        }

        if (especialidadRepository.count() == 0) {
            especialidad1 = especialidadRepository.save(especialidad1);
            especialidad2 = especialidadRepository.save(especialidad2);
            especialidad3 = especialidadRepository.save(especialidad3);
            especialidad4 = especialidadRepository.save(especialidad4);
        }

        if (previsionRepository.count() == 0) {
            prevision1 = previsionRepository.save(prevision1);
            prevision2 = previsionRepository.save(prevision2);
        }

        // Inicialización de Médicos
        Medico medico1 = new Medico();
        medico1.setRun("10418567-1");
        medico1.setNombre("Julio");
        medico1.setApellido("Perez");
        medico1.setFechaContrato(LocalDate.parse("2020-03-15"));
        medico1.setSueldo_base(900000f);
        medico1.setCorreo("ju.perez@hospital.cl");
        medico1.setTelefono("569845823");
        medico1.setEspecialidad(especialidad1);

        Medico medico2 = new Medico();
        medico2.setRun("11345987-2");
        medico2.setNombre("Ana");
        medico2.setApellido("Morales");
        medico2.setFechaContrato(LocalDate.parse("2018-09-01"));
        medico2.setSueldo_base(950000f);
        medico2.setCorreo("ana.morales@hospital.cl");
        medico2.setTelefono("56987451236");
        medico2.setEspecialidad(especialidad2);

        Medico medico3 = new Medico();
        medico3.setRun("12456879-5");
        medico3.setNombre("Carlos");
        medico3.setApellido("Rojas");
        medico3.setFechaContrato(LocalDate.parse("2019-06-10"));
        medico3.setSueldo_base(910000f);
        medico3.setCorreo("carlos.rojas@hospital.cl");
        medico3.setTelefono("56976548932");
        medico3.setEspecialidad(especialidad4);

        if (medicoRepository.count() == 0) {
            medico1 = medicoRepository.save(medico1);
            medico2 = medicoRepository.save(medico2);
            medico3 = medicoRepository.save(medico3);
        }

        // Inicialización de Pacientes (5 pacientes completos)
        Paciente paciente1 = new Paciente();
        paciente1.setRun("15123588-2");
        paciente1.setNombre("María");
        paciente1.setApellido("Sanchez");
        paciente1.setFechaNacimiento(LocalDate.parse("1992-05-20"));
        paciente1.setCorreo("ma.sanchez@correo.cl");
        paciente1.setTelefono("586989652");
        paciente1.setPrevision(prevision1);

        Paciente paciente2 = new Paciente();
        paciente2.setRun("16111222-3");
        paciente2.setNombre("Luis");
        paciente2.setApellido("Gonzalez");
        paciente2.setFechaNacimiento(LocalDate.parse("1985-11-12"));
        paciente2.setCorreo("lu.gonzalez@correo.cl");
        paciente2.setTelefono("598761234");
        paciente2.setPrevision(prevision2);

        Paciente paciente3 = new Paciente();
        paciente3.setRun("17222333-4");
        paciente3.setNombre("Patricia");
        paciente3.setApellido("Castro");
        paciente3.setFechaNacimiento(LocalDate.parse("1990-08-30"));
        paciente3.setCorreo("pa.castro@correo.cl");
        paciente3.setTelefono("591234567");
        paciente3.setPrevision(prevision1);

        Paciente paciente4 = new Paciente();
        paciente4.setRun("18333444-5");
        paciente4.setNombre("Jorge");
        paciente4.setApellido("Ramirez");
        paciente4.setFechaNacimiento(LocalDate.parse("1978-03-22"));
        paciente4.setCorreo("jo.ramirez@correo.cl");
        paciente4.setTelefono("587654321");
        paciente4.setPrevision(prevision2);

        Paciente paciente5 = new Paciente();
        paciente5.setRun("19444555-6");
        paciente5.setNombre("Camila");
        paciente5.setApellido("Torres");
        paciente5.setFechaNacimiento(LocalDate.parse("2000-01-15"));
        paciente5.setCorreo("ca.torres@correo.cl");
        paciente5.setTelefono("599876543");
        paciente5.setPrevision(prevision1);

        if (pacienteRepository.count() == 0) {
            paciente1 = pacienteRepository.save(paciente1);
            paciente2 = pacienteRepository.save(paciente2);
            paciente3 = pacienteRepository.save(paciente3);
            paciente4 = pacienteRepository.save(paciente4);
            paciente5 = pacienteRepository.save(paciente5);
        }

        // Inicialización de Atenciones (10 atenciones completas)
        Atencion atencion1 = new Atencion();
        atencion1.setFechaAtencion(LocalDate.parse("2025-05-20"));
        atencion1.setHoraAtencion(LocalDateTime.parse("2025-05-20T10:30:00"));
        atencion1.setCosto(15000);
        atencion1.setComentario("Paciente ingresa con dolor de cabeza");
        atencion1.setEstado(estado1);
        atencion1.setPaciente(paciente1);
        atencion1.setMedico(medico1);

        Atencion atencion2 = new Atencion();
        atencion2.setFechaAtencion(LocalDate.parse("2025-05-20"));
        atencion2.setHoraAtencion(LocalDateTime.parse("2025-05-20T11:00:00"));
        atencion2.setCosto(18000);
        atencion2.setComentario("Control pediátrico de rutina");
        atencion2.setEstado(estado3);
        atencion2.setPaciente(paciente2);
        atencion2.setMedico(medico2);

        Atencion atencion3 = new Atencion();
        atencion3.setFechaAtencion(LocalDate.parse("2025-05-21"));
        atencion3.setHoraAtencion(LocalDateTime.parse("2025-05-21T09:45:00"));
        atencion3.setCosto(20000);
        atencion3.setComentario("Consulta dermatológica por alergia");
        atencion3.setEstado(estado2);
        atencion3.setPaciente(paciente3);
        atencion3.setMedico(medico3);

        Atencion atencion4 = new Atencion();
        atencion4.setFechaAtencion(LocalDate.parse("2025-05-21"));
        atencion4.setHoraAtencion(LocalDateTime.parse("2025-05-21T12:15:00"));
        atencion4.setCosto(22000);
        atencion4.setComentario("Paciente consulta por taquicardia");
        atencion4.setEstado(estado1);
        atencion4.setPaciente(paciente4);
        atencion4.setMedico(medico1);

        Atencion atencion5 = new Atencion();
        atencion5.setFechaAtencion(LocalDate.parse("2025-05-21"));
        atencion5.setHoraAtencion(LocalDateTime.parse("2025-05-21T14:00:00"));
        atencion5.setCosto(17000);
        atencion5.setComentario("Revisión post-operatoria");
        atencion5.setEstado(estado3);
        atencion5.setPaciente(paciente5);
        atencion5.setMedico(medico2);

        Atencion atencion6 = new Atencion();
        atencion6.setFechaAtencion(LocalDate.parse("2025-05-22"));
        atencion6.setHoraAtencion(LocalDateTime.parse("2025-05-22T08:30:00"));
        atencion6.setCosto(19000);
        atencion6.setComentario("Paciente presenta infección cutánea");
        atencion6.setEstado(estado2);
        atencion6.setPaciente(paciente1);
        atencion6.setMedico(medico3);

        Atencion atencion7 = new Atencion();
        atencion7.setFechaAtencion(LocalDate.parse("2025-05-22"));
        atencion7.setHoraAtencion(LocalDateTime.parse("2025-05-22T10:00:00"));
        atencion7.setCosto(21000);
        atencion7.setComentario("Consulta por hipertensión");
        atencion7.setEstado(estado1);
        atencion7.setPaciente(paciente2);
        atencion7.setMedico(medico1);

        Atencion atencion8 = new Atencion();
        atencion8.setFechaAtencion(LocalDate.parse("2025-05-23"));
        atencion8.setHoraAtencion(LocalDateTime.parse("2025-05-23T11:30:00"));
        atencion8.setCosto(16000);
        atencion8.setComentario("Evaluación pediátrica por fiebre");
        atencion8.setEstado(estado2);
        atencion8.setPaciente(paciente3);
        atencion8.setMedico(medico2);

        Atencion atencion9 = new Atencion();
        atencion9.setFechaAtencion(LocalDate.parse("2025-05-23"));
        atencion9.setHoraAtencion(LocalDateTime.parse("2025-05-23T13:45:00"));
        atencion9.setCosto(23000);
        atencion9.setComentario("Paciente con dolor de pecho");
        atencion9.setEstado(estado3);
        atencion9.setPaciente(paciente4);
        atencion9.setMedico(medico1);

        Atencion atencion10 = new Atencion();
        atencion10.setFechaAtencion(LocalDate.parse("2025-05-23"));
        atencion10.setHoraAtencion(LocalDateTime.parse("2025-05-23T15:00:00"));
        atencion10.setCosto(17500);
        atencion10.setComentario("Consulta dermatológica preventiva");
        atencion10.setEstado(estado1);
        atencion10.setPaciente(paciente5);
        atencion10.setMedico(medico3);

        if (atencionRepository.count() == 0) {
            atencionRepository.save(atencion1);
            atencionRepository.save(atencion2);
            atencionRepository.save(atencion3);
            atencionRepository.save(atencion4);
            atencionRepository.save(atencion5);
            atencionRepository.save(atencion6);
            atencionRepository.save(atencion7);
            atencionRepository.save(atencion8);
            atencionRepository.save(atencion9);
            atencionRepository.save(atencion10);
        }
    }
}