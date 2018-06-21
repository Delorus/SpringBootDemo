package ru.tinkoff.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.demo.dto.LinkToModel;
import ru.tinkoff.demo.dto.PersonChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.City;
import ru.tinkoff.demo.model.Person;
import ru.tinkoff.demo.model.Transport;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceIntTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private TransportService transportService;

    private PersonChangeRequest changeRequest;
    private List<Person> personsList;
    private HashMap<Person, List<Transport>> persons = new HashMap<>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws EntityNotFoundException {

        changeRequest = PersonChangeRequest.builder()
            .firstName("Алена")
            .parentName("Юрьевна")
            .lastName("Кондратьева")
            .city(LinkToModel.of(1L))
            .build();
    }

    @Test
    public void testGetAll() {
        final List<String> firstNames = personService.getAll().stream()
            .map(Person::getFirstName)
            .collect(Collectors.toList());
        final List<Long> cities = personService.getAll().stream()
            .map(Person::getCity)
            .map(City::getId)
            .collect(Collectors.toList());

        assertThat(firstNames).contains("Иван", "Анатолий");
        assertThat(cities).contains(1L, 2L);
    }

    @Test
    public void testCreatePerson() {
        Person createdPerson = personService.createPerson(changeRequest);

        assertThat(createdPerson.getFirstName()).isEqualTo(this.changeRequest.getFirstName());
        assertThat(createdPerson.getLastName()).isEqualTo(this.changeRequest.getLastName());
        assertThat(createdPerson.getCity().getId()).isEqualTo(this.changeRequest.getCity().getId());
    }

    @Test
    public void testCreatePersonWithoutCity() {
        val request = PersonChangeRequest.builder()
            .firstName("Алена")
            .parentName("Юрьевна")
            .lastName("Кондратьева")
            .build();
        Person createdPerson = personService.createPerson(request);

        assertThat(createdPerson.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(createdPerson.getLastName()).isEqualTo(request.getLastName());
        assertThat(createdPerson.getCity()).isNull();
    }

    @Test
    public void testUpdatePerson() throws EntityNotFoundException {
        Person startedPerson = personService.createPerson(changeRequest);
        Long id = startedPerson.getId();
        PersonChangeRequest changeRequest = PersonChangeRequest.builder()
            .city(LinkToModel.of(2L))
            .firstName("Александр")
            .lastName("Кошкин")
            .build();
        Person updatedPerson = personService.updatePerson(id, changeRequest);

        assertThat(updatedPerson.getId()).isEqualTo(startedPerson.getId());
        assertThat(updatedPerson.getFirstName()).isEqualTo(changeRequest.getFirstName());
        assertThat(updatedPerson.getLastName()).isEqualTo(changeRequest.getLastName());
        assertThat(updatedPerson.getParentName()).isEqualTo(changeRequest.getParentName());
        assertThat(updatedPerson.getCity().getId()).isEqualTo(changeRequest.getCity().getId());
    }

    @Test
    public void testDeletePerson() throws EntityNotFoundException {
        Long id = personService.createPerson(changeRequest).getId();

        personService.deletePerson(id);
    }

    @Test
    public void testGetPerson() throws EntityNotFoundException {
        Long id = personService.createPerson(changeRequest).getId();
        Person gettedPerson = personService.getPerson(id);

        assertThat(gettedPerson.getFirstName()).isEqualTo(changeRequest.getFirstName());
        assertThat(gettedPerson.getLastName()).isEqualTo(changeRequest.getLastName());
        assertThat(gettedPerson.getCity().getId()).isEqualTo(changeRequest.getCity().getId());
    }

    @Test
    public void testGetAllByFirstNameWithTransports() {
        String firstName = "серг";
        List<Person> persons = personService.getAllByNameWithTransports(firstName, "", "");
        Map<Long, List<String>> govnumberToPersonIdMap = persons.stream()
            .flatMap(person -> person.getTransports().stream())
            .collect(Collectors.groupingBy(it -> it.getPerson().getId(),
                                           Collectors.mapping(Transport::getGovnumber, Collectors.toList())));

        assertThat(govnumberToPersonIdMap.get(3L)).contains("Г333АП066", "Г444АП066", "Г555АП066");
        assertThat(govnumberToPersonIdMap.get(4L)).contains("Г666АП066", "Г777АП066");
    }

    @Test
    public void testGetAllByLastNameWithTransports() {
        String lastName = "ло";
        List<Person> persons = personService.getAllByNameWithTransports("", lastName, "");
        Map<Long, List<String>> govnumberToPersonIdMap = persons.stream()
            .flatMap(person -> person.getTransports().stream())
            .collect(Collectors.groupingBy(it -> it.getPerson().getId(),
                                           Collectors.mapping(Transport::getGovnumber, Collectors.toList())));

        assertThat(govnumberToPersonIdMap.get(2L)).contains("в002вв066", "Г111АП066", "Г222АП066");
        assertThat(govnumberToPersonIdMap.get(3L)).contains("Г333АП066", "Г444АП066", "Г555АП066");
    }

    @Test
    public void testGetAllByParentNameWithTransports() {
        String parentName = "Ивано";
        List<Person> persons = personService.getAllByNameWithTransports("", "", parentName);
        Map<Long, List<String>> govnumberToPersonIdMap = persons.stream()
            .flatMap(person -> person.getTransports().stream())
            .collect(Collectors.groupingBy(it -> it.getPerson().getId(),
                                           Collectors.mapping(Transport::getGovnumber, Collectors.toList())));

        assertThat(govnumberToPersonIdMap.get(1L)).contains("а001аа999");
        assertThat(govnumberToPersonIdMap.get(4L)).contains("Г666АП066", "Г777АП066");
    }

    @Test
    public void testCreatePersonWithIdThatExist() {
        Person person = personService.createPerson(changeRequest);
        Person againCreatedPerson = personService.createPerson(changeRequest);

        assertThat(againCreatedPerson.getId()).isNotEqualTo(person.getId());
        assertThat(againCreatedPerson.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(againCreatedPerson.getCity().getName()).isEqualTo(person.getCity().getName());
    }

    @Test
    public void testGetPersonNotExistingPersonUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Person with id=" + id + " is not found");

        personService.getPerson(id);
    }

    @Test
    public void testUpdatePersonNotExistingPersonUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();
        PersonChangeRequest changeRequest = PersonChangeRequest.builder()
            .city(LinkToModel.of(2L))
            .firstName("Игнат")
            .build();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Person with id=" + id + " is not found");

        personService.updatePerson(id, changeRequest);
    }

    @Test
    public void testDeletePersonNotExistingPersonUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Person with id=" + id + " is not found");

        personService.deletePerson(id);
    }

}
