package com.example.demo.controllers;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Room;
import com.example.demo.repositories.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    final
    AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        return appointment.map(value -> status(HttpStatus.OK).body(value))
                .orElseGet(() -> status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment) {
        boolean isValidTime = validateAppointmentDate(appointment.getStartsAt(), appointment.getFinishesAt());
        if (!isValidTime) {
            return status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isAvalibleRoom = validateRoomAvailability(appointment.getStartsAt(), appointment.getRoom());
        if (!isAvalibleRoom) {
            return status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        appointmentRepository.save(appointment);
        return status(HttpStatus.OK).body(appointmentRepository.findAll());
    }

    private boolean validateRoomAvailability(LocalDateTime startsAt, Room room) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findAll().stream()
                .filter(a -> a.getStartsAt().equals(startsAt))
                .findFirst();

        boolean availableTime = false;
        boolean roomAvailable = false;
        if (optionalAppointment.isPresent()) {
            availableTime = true;
            roomAvailable = !optionalAppointment.get()
                    .getRoom().getRoomName().equals(room.getRoomName());
        }

        return availableTime == roomAvailable;
    }

    private boolean validateAppointmentDate(LocalDateTime startsAt, LocalDateTime finishesAt) {
        return startsAt.plusHours(1).isEqual(finishesAt);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
