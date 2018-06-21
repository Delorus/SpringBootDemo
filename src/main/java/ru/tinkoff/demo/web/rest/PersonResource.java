package ru.tinkoff.demo.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.demo.dto.PersonChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.Person;
import ru.tinkoff.demo.service.PersonService;

@RestController
@RequestMapping("/api/persons")
@Api(tags = "persons", description = "Персоны")
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ApiOperation("Создать новую персону")
    public Person createPerson(@Valid @RequestBody PersonChangeRequest changeRequest) {
        return personService.createPerson(changeRequest);
    }

    @PutMapping("/{personId}")
    @ApiOperation("Обновить персону")
    public Person updatePerson(@PathVariable("personId") Long personId,
                               @Valid @RequestBody PersonChangeRequest changeRequest) throws EntityNotFoundException {
        return personService.updatePerson(personId, changeRequest);
    }

    @GetMapping("{personId}")
    @ApiOperation("Получить персону")
    public Person getPerson(@PathVariable("personId") Long personId) throws EntityNotFoundException {
        return personService.getPerson(personId);
    }

    @GetMapping
    @ApiOperation("Получить список всех персон")
    public List<Person> getAll() {
        return personService.getAll();
    }

    @GetMapping("/all_with_transports")
    @ApiOperation("Получить список персон с транспортом по имени, фамилии и отчеству")
    public List<Person> getAllByNameWithTransports(
        @RequestParam(name = "firstName", required = false, defaultValue = "") String firstName,
        @RequestParam(name = "lastName", required = false, defaultValue = "") String lastName,
        @RequestParam(name = "parentName", required = false, defaultValue = "") String parentName) {
        return personService.getAllByNameWithTransports(firstName, lastName, parentName);
    }

    @DeleteMapping("/{personId}")
    @ApiOperation("Удалить персону")
    public void deletePerson(@PathVariable("personId") Long personId) throws EntityNotFoundException {
        personService.deletePerson(personId);
    }
}
