package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id){
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()){
            return new ResponseEntity<>(appointment.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment){
        boolean isValid = validateAppointment(appointment.getStartsAt(), appointment.getFinishesAt());
        if (!isValid) {
            return status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isDisponible = validateDisponibility(appointment.getStartsAt(), appointment.getRoom());
        if (!isDisponible) {
            return status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        appointmentRepository.save(appointment);
        return status(HttpStatus.OK).body(appointmentRepository.findAll());
    }

    private boolean validateDisponibility(LocalDateTime startsAt, Room room) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findAll().stream()
                .filter(a -> a.getStartsAt().equals(startsAt))
                .findFirst();

        boolean horaDisponible = false;
        boolean habitacionDisponible = false;
        if (optionalAppointment.isPresent()) {
            horaDisponible = true;
            habitacionDisponible = !optionalAppointment.get()
                    .getRoom().getRoomName().equals(room.getRoomName());
        }

        return horaDisponible == habitacionDisponible;
    }

    private boolean validateAppointment(LocalDateTime startsAt, LocalDateTime finishesAt) {
        return startsAt.plusHours(1).isEqual(finishesAt);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id){

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments(){
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
