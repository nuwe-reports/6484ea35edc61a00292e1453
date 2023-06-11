
package com.example.demo;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testdCreateDoctor() throws Exception {
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(doctor)));
    }

    @Test
    void testdFindAllDoctors() throws Exception {
        List<Doctor> doctors = Arrays.asList(
                new Doctor("John", "Doe", 35, "john.doe@example.com"),
                new Doctor("Jane", "Smith", 40, "jane.smith@example.com")
        );
        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctors)));
    }

    @Test
    void testdFindDoctorById() throws Exception {
        long doctorId = 1;
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/{id}", doctorId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctor)));
    }

    @Test
    void testdDeleteDoctorById() throws Exception {
        long doctorId = 1;
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        mockMvc.perform(delete("/api/doctors/{id}", doctorId))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).deleteById(doctorId);
    }

    @Test
    void testdDeleteAllDoctors() throws Exception {
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).deleteAll();
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testdCreatePatient() throws Exception {
        Patient patient = new Patient("John", "Doe", 35, "john.doe@example.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(patient)));
    }

    @Test
    void testdGetAllPatients() throws Exception {
        Patient patient1 = new Patient("John", "Doe", 35, "john.doe@example.com");
        Patient patient2 = new Patient("Jane", "Smith", 40, "jane.smith@example.com");
        List<Patient> patients = Arrays.asList(patient1, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testdGetPatientById() throws Exception {
        long patientId = 1;
        Patient patient = new Patient("John", "Doe", 35, "john.doe@example.com");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(patientRepository, times(1)).findById(patientId);
    }


    @Test
    void testdDeleteAllPatient() throws Exception {
        long patientId = 1;
        Patient patient = new Patient("John", "Doe", 35, "john.doe@example.com");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    void testdDeleteAllDoctors() throws Exception {
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).deleteAll();
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testdGetAllRooms() throws Exception {
        Room room1 = new Room("101");
        Room room2 = new Room("102");
        List<Room> rooms = Arrays.asList(room1, room2);

        given(roomRepository.findAll()).willReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].roomName", is("101")))
                .andExpect(jsonPath("$[1].roomName", is("102")));
    }

    @Test
    void testdCreateRoom() throws Exception {
        Room room = new Room("101");
        String requestBody = objectMapper.writeValueAsString(room);

        mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(room)));
    }

    @Test
    void testdDeleteRoomByName() throws Exception {
        String roomName = "404";
        Room room = new Room(roomName);
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.of(room));

        mockMvc.perform(delete("/api/rooms/{id}", roomName))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteByRoomName(roomName);
    }

    @Test
    void testdDeleteAllRoom() throws Exception {
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteAll();
    }

}
