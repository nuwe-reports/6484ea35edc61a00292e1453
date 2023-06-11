package com.example.demo;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;


    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;

    @BeforeEach
    void setup() {
        d1 = null;
        p1 = null;
        r1 = null;
        a1 = null;
    }

    /**
     * Pruebas de persistencia.
     * Con este tipo de prueba se verifica que no haya errores de persistencia, ya sea por un tipo de dato no soportado,
     * un dato mal serializado o mal formado.
     */
    @Test
    void testPersistAppointment() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
        patient.setEmail("john.doe@example.com");
        entityManager.persist(patient);

        Doctor doctor = new Doctor();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setAge(35);
        entityManager.persist(doctor);

        Room room = new Room("101");
        entityManager.persist(room);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(room);

        entityManager.persist(appointment);

        Appointment persistedAppointment = entityManager.find(Appointment.class, appointment.getId());

        assertNotNull(persistedAppointment);
        assertEquals(appointment.getPatient(), persistedAppointment.getPatient());
        assertEquals(appointment.getDoctor(), persistedAppointment.getDoctor());
        assertEquals(appointment.getRoom(), persistedAppointment.getRoom());
    }

    @Test
    void testDoctorPersistEntity() {
        // Crear una entidad para la prueba
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setAge(35);

        // Persistir la entidad utilizando TestEntityManager
        entityManager.persist(doctor);

        // Recuperar la entidad persistida desde la base de datos
        Doctor persistedDoctor = entityManager.find(Doctor.class, doctor.getId());

        // Verificar que la entidad persistida no sea nula y tenga los mismos valores que la entidad original
        assertThat(persistedDoctor).isNotNull();
        assertEquals(doctor.getFirstName(), persistedDoctor.getFirstName());
        assertEquals(doctor.getLastName(), persistedDoctor.getLastName());
        assertEquals(doctor.getAge(), persistedDoctor.getAge());
    }

    @Test
    void testPersistPatient() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAge(30);
        patient.setEmail("john.doe@example.com");

        entityManager.persist(patient);

        Patient persistedPatient = entityManager.find(Patient.class, patient.getId());

        assertThat(persistedPatient).isNotNull();
        assertEquals(patient.getFirstName(), persistedPatient.getFirstName());
        assertEquals(patient.getLastName(), persistedPatient.getLastName());
        assertEquals(patient.getAge(), persistedPatient.getAge());
        assertEquals(patient.getEmail(), persistedPatient.getEmail());
    }

    @Test
    void testPersistRoom() {
        Room room = new Room("101");

        entityManager.persist(room);

        Room persistedRoom = entityManager.find(Room.class, room.getRoomName());

        assertThat(persistedRoom).isNotNull();
        assertEquals(room.getRoomName(), persistedRoom.getRoomName());
    }

    /**
     * Prueba de constructor, setter y getter.
     * Tiene la finalidad de determinar si se ha realizado algún cambio en la firma
     * de los constructores de alguna de las clases o en la lógica de sus setters o getters.
     */
    @Test
    void testDoctorSetterGetterAndConstructor() {
        long id = new Random().nextLong();
        String name = "Arthur";
        String lastName = "Gonzales";
        int age = 29;
        String email = "agonzales@hospitalx.ec";
        d1 = new Doctor(name, lastName, age, email);
        assertAll("Getters",
                () -> assertEquals(0, d1.getId()),
                () -> assertEquals(age, d1.getAge()),
                () -> assertEquals(name, d1.getFirstName()),
                () -> assertEquals(lastName, d1.getLastName()),
                () -> assertEquals(email, d1.getEmail()));

        String name2 = "Alfredo";
        String lastName2 = "Pozo";
        int age2 = 22;
        String email2 = "apozo@gmail.com";

        d1.setId(id);
        d1.setFirstName(name2);
        d1.setLastName(lastName2);
        d1.setAge(age2);
        d1.setEmail(email2);
        assertAll("Setters",
                () -> assertEquals(age2, d1.getAge()),
                () -> assertEquals(name2, d1.getFirstName()),
                () -> assertEquals(lastName2, d1.getLastName()),
                () -> assertEquals(age2, d1.getAge()),
                () -> assertEquals(email2, d1.getEmail()));
    }

    @Test
    void testPatientSetterGetterAndConstructor() {
        long id = new Random().nextLong();
        String name = "Geampier";
        String lastName = "Novillo";
        int age = 29;
        String email = "gnovillo@edu.ec";
        p1 = new Patient(name, lastName, age, email);
        assertAll("Getters",
                () -> assertEquals(0, p1.getId()),
                () -> assertEquals(age, p1.getAge()),
                () -> assertEquals(name, p1.getFirstName()),
                () -> assertEquals(lastName, p1.getLastName()),
                () -> assertEquals(email, p1.getEmail()));

        String name2 = "Alfredo";
        String lastName2 = "Pozo";
        int age2 = 22;
        String email2 = "apozo@gmail.com";

        p1.setId(id);
        p1.setFirstName(name2);
        p1.setLastName(lastName2);
        p1.setAge(age2);
        p1.setEmail(email2);
        assertAll("Setters",
                () -> assertEquals(age2, p1.getAge()),
                () -> assertEquals(name2, p1.getFirstName()),
                () -> assertEquals(lastName2, p1.getLastName()),
                () -> assertEquals(age2, p1.getAge()),
                () -> assertEquals(email2, p1.getEmail()));
    }

    @Test
    void testRoomGetterAndConstructor() {
        String roomName = "test";
        r1 = new Room(roomName);
        assertEquals(r1.getRoomName(), roomName);
    }

    @Test
    void testAppointmentGetterSetterAndConstructor() {
        d1 = new Doctor("Jose", "Garcia", 30, "jgarcia@hostipalx.com");
        p1 = new Patient("Luber", "Torrez", 19, "ltorrez@gmail.com");
        r1 = new Room("test");
        LocalDateTime date = LocalDateTime.now();
        a1 = new Appointment(p1, d1, r1, date, date);

        assertAll("Getters",
                () -> assertEquals(0, a1.getId()),
                () -> assertEquals(d1, a1.getDoctor()),
                () -> assertEquals(p1, a1.getPatient()),
                () -> assertEquals(r1, a1.getRoom()),
                () -> assertEquals(date, a1.getStartsAt()),
                () -> assertEquals(date, a1.getFinishesAt())
        );
        a1.setId(2);
        a1.setDoctor(null);
        a1.setPatient(null);
        a1.setRoom(null);
        a1.setStartsAt(null);
        a1.setFinishesAt(null);
        assertAll("Setters",
                () -> assertEquals(2, a1.getId()),
                () -> assertNull(a1.getDoctor()),
                () -> assertNull(a1.getPatient()),
                () -> assertNull(a1.getRoom()),
                () -> assertNull(a1.getStartsAt()),
                () -> assertNull(a1.getFinishesAt())
        );
    }
}
