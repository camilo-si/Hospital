package com.example.ActHos.ActividadHospital_19_05.service;

import com.example.ActHos.ActividadHospital_19_05.model.Atencion;
import com.example.ActHos.ActividadHospital_19_05.model.Medico;
import com.example.ActHos.ActividadHospital_19_05.model.Paciente;
import com.example.ActHos.ActividadHospital_19_05.repository.AtencionRepository;
import com.example.ActHos.ActividadHospital_19_05.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private AtencionRepository atencionRepository;

    public List<Medico> getAllMedicos(){
        return medicoRepository.findAll();
    }

    public Optional<Medico> getMedicoById(Integer idMedico){
        return medicoRepository.findById(idMedico);
    }

    public Medico saveMedico(Medico medico){
        return medicoRepository.save(medico);
    }

    public void deleteMedico(Integer idMedico){
        medicoRepository.deleteById(idMedico);
    }

    public long countMedicos(){
        return medicoRepository.count();
    }

    public boolean existsMedico(Integer idMedico){
        return medicoRepository.existsById(idMedico);
    }
    public Optional<Medico> findByRun(String run) {
        return medicoRepository.findByRun(run);
    }

    public List<Medico> buscarPorNombreOApellido(String texto) {
        return medicoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(texto, texto);
    }

    public List<Medico> buscarMedicosConMenosDeAniosAntiguedad(Integer anios) {
        return medicoRepository.findMedicosMenosDeAniosAntiguedad(anios);
    }

    public List<Medico> buscarMedicosConMasDeAniosAntiguedad(Integer anios) {
        return medicoRepository.findMedicosMasDeAniosAntiguedad(anios);
    }

    public List<Medico> findByNombreAndApellido(String nombre, String apellido){
        return medicoRepository.findByNombreAndApellido(nombre,apellido);
    }

    public float calcularSueldoTotal(Integer idMedico) {
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        List<Atencion> atenciones = atencionRepository.findByMedicoIdMedico(idMedico);

        float totalAtenciones = 0;
        for (Atencion atencion : atenciones) {
            totalAtenciones += atencion.getCosto(); // getCosto() retorna float
        }

        float bono = totalAtenciones * 0.2f; // el sufijo f es importante
        return medico.getSueldo_base() + bono;

    }
}
