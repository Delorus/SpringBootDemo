package ru.tinkoff.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.demo.dto.LinkToModel;
import ru.tinkoff.demo.dto.TransportChangeRequest;
import ru.tinkoff.demo.dto.TransportCreateRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.error.TransportWithAlreadyExistingGovnumberException;
import ru.tinkoff.demo.model.Person;
import ru.tinkoff.demo.model.Transport;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransportServiceTest {

    @Autowired
    private TransportService transportService;

    private TransportCreateRequest createRequest;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        createRequest = TransportCreateRequest.builder()
            .govnumber("у003ва066")
            .model(LinkToModel.of(1L))
            .person(LinkToModel.of(1L))
            .build();
    }

    @Test
    public void testGetAll() {
        List<String> transports = transportService.getAll().stream()
            .map(Transport::getGovnumber)
            .collect(Collectors.toList());

        assertThat(transports).contains("а001аа999", "в002вв066");
    }

    @Test
    public void testCreateTransport() throws EntityNotFoundException, TransportWithAlreadyExistingGovnumberException {
        TransportCreateRequest request = TransportCreateRequest.builder()
            .govnumber("Х004АЕ072")
            .model(LinkToModel.of(2L))
            .person(LinkToModel.of(2L))
            .build();
        Transport transport = transportService.createTransport(request);

        assertThat(transport.getGovnumber()).isEqualTo("Х004АЕ072");
        assertThat(transport.getModel().getId()).isEqualTo(2L);
        assertThat(transport.getPerson().getId()).isEqualTo(2L);
    }

    @Test
    public void testUpdateTransport() throws EntityNotFoundException, TransportWithAlreadyExistingGovnumberException {
        createRequest.setGovnumber("у001ва066");
        Transport transport = transportService.createTransport(createRequest);
        Long id = transport.getId();
        TransportChangeRequest updateRequest = TransportChangeRequest.builder()
            .person(LinkToModel.of(2L))
            .govnumber("в005вв066")
            .build();
        Transport updatedTransport = transportService.updateTransport(id, updateRequest);

        assertThat(updatedTransport.getId()).isEqualTo(id);
        assertThat(updatedTransport.getGovnumber()).isEqualTo("в005вв066");
        assertThat(updatedTransport.getModel().getId()).isEqualTo(1L);
        assertThat(updatedTransport.getPerson().getId()).isEqualTo(2L);
    }

    @Test
    public void testDeleteTransport() throws EntityNotFoundException {
        createRequest.setGovnumber("в008вв066");
        Long id = transportService.createTransport(createRequest).getId();

        transportService.deleteTransport(id);
    }

    @Test
    public void testGetTransport() throws EntityNotFoundException {
        createRequest.setGovnumber("в009вв066");
        Long id = transportService.createTransport(createRequest).getId();
        Transport transport = transportService.getTransport(id);

        assertThat(transport.getId()).isEqualTo(id);
        assertThat(transport.getGovnumber()).isEqualTo(createRequest.getGovnumber());
        assertThat(transport.getPerson().getId()).isEqualTo(createRequest.getPerson().getId());
        assertThat(transport.getModel().getId()).isEqualTo(createRequest.getModel().getId());
    }

    @Test
    public void testGetByGovnomer() throws TransportWithAlreadyExistingGovnumberException {
        String number = "в010ВВ066";
        createRequest.setGovnumber(number);
        transportService.createTransport(createRequest);
        number = "В010вв066";

        Transport transport = transportService.getTransportByGovnumber(number);

        assertThat(transport.getGovnumber()).containsIgnoringCase(number);
        assertThat(transport.getModel().getId()).isEqualTo(createRequest.getModel().getId());
        assertThat(transport.getPerson().getId()).isEqualTo(createRequest.getPerson().getId());
    }

    @Test
    public void testGetAllByPersonId() {
        List<Transport> transports = transportService.getAllTransportsByPersonId(2L);
        List<String> govnumbers = transports.stream()
            .map(Transport::getGovnumber)
            .collect(Collectors.toList());
        List<Long> personIDs = transports.stream()
            .map(Transport::getPerson)
            .map(Person::getId)
            .collect(Collectors.toList());

        assertThat(govnumbers).contains("Г111АП066", "Г222АП066");
        assertThat(personIDs).containsOnly(2L);
    }

    @Test
    public void testCreateTransportWithAlreadyUsingGovnomerUnsuccesfulFlow()
        throws TransportWithAlreadyExistingGovnumberException, EntityNotFoundException {
        String govnumber = "Х006АЕ072";
        TransportCreateRequest request = TransportCreateRequest.builder()
            .govnumber(govnumber)
            .model(LinkToModel.of(2L))
            .person(LinkToModel.of(2L))
            .build();
        transportService.createTransport(request);

        thrown.expectMessage("could not execute statement; SQL [n/a]; constraint [\"CONSTRAINT_INDEX_E ON PUBLIC"
                                 + ".TRANSPORT(GOVNUMBER)");
        thrown.expect(DataIntegrityViolationException.class);

        transportService.createTransport(request);
    }

    @Test
    public void testUpdateTransportWithAlreadyUsingGovnomerUnsuccesfulFlow()
        throws EntityNotFoundException {
        String conflictNumber = "в011вв066";
        createRequest.setGovnumber(conflictNumber);
        transportService.createTransport(createRequest);
        TransportCreateRequest request = TransportCreateRequest.builder()
            .govnumber("Х107АЕ072")
            .model(LinkToModel.of(2L))
            .person(LinkToModel.of(2L))
            .build();
        Long updateId = transportService.createTransport(request).getId();

        TransportChangeRequest conflictUpdateRequest = TransportChangeRequest.builder()
            .govnumber(conflictNumber)
            .build();

        thrown.expectMessage("could not execute statement; SQL [n/a]; constraint [\"CONSTRAINT_INDEX_E ON PUBLIC"
                                 + ".TRANSPORT(GOVNUMBER)");
        thrown.expect(DataIntegrityViolationException.class);

        transportService.updateTransport(updateId, conflictUpdateRequest);
    }

    @Test
    public void testUpdateTransportNotExistingTransportUnsuccesfulFlow()
        throws EntityNotFoundException {
        Long id = new Random().nextLong();
        TransportChangeRequest request = TransportChangeRequest.builder()
            .govnumber("Х207АЕ072")
            .person(LinkToModel.of(2L))
            .build();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Transport with id=" + id + " is not found");

        transportService.updateTransport(id, request);
    }

    @Test
    public void testGetTransportNotExistingTransportUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Transport with id=" + id + " is not found");

        transportService.getTransport(id);
    }
}
