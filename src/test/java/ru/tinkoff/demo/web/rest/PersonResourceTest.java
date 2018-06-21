package ru.tinkoff.demo.web.rest;

import static org.mockito.ArgumentMatchers.eq;

import java.util.Random;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.demo.dto.LinkToModel;
import ru.tinkoff.demo.dto.PersonChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonResource.class)
public class PersonResourceTest {

    private static final Long PERSON_ID = new Random().nextLong();
    private static final Long CITY_ID = new Random().nextLong();
    private static final String PERSON_LASTNAME = "Сидоров";

    @Autowired
    private PersonResource personResource;

    @MockBean
    private PersonService personService;

    @Test
    public void testCreatePerson() {
        val request = PersonChangeRequest.builder()
            .lastName(PERSON_LASTNAME)
            .city(LinkToModel.of(CITY_ID))
            .build();
        personResource.createPerson(request);

        BDDMockito.verify(personService)
            .createPerson(eq(
                PersonChangeRequest.builder()
                    .lastName(PERSON_LASTNAME)
                    .city(LinkToModel.of(CITY_ID))
                    .build()
            ));
    }

    @Test
    public void testUpdatePerson() throws EntityNotFoundException {
        val request = PersonChangeRequest.builder()
            .lastName(PERSON_LASTNAME)
            .city(LinkToModel.of(CITY_ID))
            .build();
        personResource.updatePerson(PERSON_ID, request);

        BDDMockito.verify(personService)
            .updatePerson(eq(PERSON_ID), eq(
                PersonChangeRequest.builder()
                    .lastName(PERSON_LASTNAME)
                    .city(LinkToModel.of(CITY_ID))
                    .build()
            ));
    }

    @Test
    public void testGetPerson() throws EntityNotFoundException {
        personResource.getPerson(PERSON_ID);

        BDDMockito.verify(personService)
            .getPerson(eq(PERSON_ID));
    }

    @Test
    public void testGetAll() {
        personResource.getAll();

        BDDMockito.verify(personService).getAll();
    }

    @Test
    public void testGetAllByNameWithTransports() {
        personResource.getAllByNameWithTransports("", PERSON_LASTNAME, "");

        BDDMockito.verify(personService)
            .getAllByNameWithTransports(eq(""), eq(PERSON_LASTNAME), eq(""));
    }

    @Test
    public void testDeletePerson() throws EntityNotFoundException {
        personResource.deletePerson(PERSON_ID);

        BDDMockito.verify(personService)
            .deletePerson(eq(PERSON_ID));
    }
}
