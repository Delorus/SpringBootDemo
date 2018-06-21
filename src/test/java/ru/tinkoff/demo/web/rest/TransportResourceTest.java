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
import ru.tinkoff.demo.dto.TransportChangeRequest;
import ru.tinkoff.demo.dto.TransportCreateRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.error.TransportWithAlreadyExistingGovnumberException;
import ru.tinkoff.demo.service.TransportService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportResource.class)
public class TransportResourceTest {

    private static final Long TRANSPORT_ID = new Random().nextLong();
    private static final Long MODEL_ID = new Random().nextLong();
    private static final Long PERSON_ID = new Random().nextLong();
    private static final String TRANSPORT_GOVNUMBER = "А123БХ098";

    @Autowired
    private TransportResource transportResource;

    @MockBean
    private TransportService transportService;

    @Test
    public void testCreateTransport() {
        val request = TransportCreateRequest.builder()
            .model(LinkToModel.of(MODEL_ID))
            .govnumber(TRANSPORT_GOVNUMBER)
            .person(LinkToModel.of(PERSON_ID))
            .build();
        transportResource.createTransport(request);

        BDDMockito.verify(transportService)
            .createTransport(eq(
                TransportCreateRequest.builder()
                    .model(LinkToModel.of(MODEL_ID))
                    .govnumber(TRANSPORT_GOVNUMBER)
                    .person(LinkToModel.of(PERSON_ID))
                    .build()
            ));
    }

    @Test
    public void testUpdateTransport() throws EntityNotFoundException {
        val request = TransportChangeRequest.builder()
            .govnumber(TRANSPORT_GOVNUMBER)
            .person(LinkToModel.of(PERSON_ID))
            .build();
        transportResource.updateTransport(TRANSPORT_ID, request);

        BDDMockito.verify(transportService)
            .updateTransport(eq(TRANSPORT_ID), eq(
                TransportChangeRequest.builder()
                    .govnumber(TRANSPORT_GOVNUMBER)
                    .person(LinkToModel.of(PERSON_ID))
                    .build()
            ));
    }

    @Test
    public void testGetTransport() throws EntityNotFoundException {
        transportResource.getTransport(TRANSPORT_ID);

        BDDMockito.verify(transportService)
            .getTransport(eq(TRANSPORT_ID));
    }

    @Test
    public void testGetAll() {
        transportResource.getAll();

        BDDMockito.verify(transportService).getAll();
    }

    @Test
    public void testGetAllTransportsByPersonId() {
        transportResource.getAllTransportsByPersonId(PERSON_ID);

        BDDMockito.verify(transportService)
            .getAllTransportsByPersonId(eq(PERSON_ID));
    }

    @Test
    public void testGetTransportByGovnumber() throws TransportWithAlreadyExistingGovnumberException {
        transportResource.getTransportByGovnumber(TRANSPORT_GOVNUMBER);

        BDDMockito.verify(transportService)
            .getTransportByGovnumber(eq(TRANSPORT_GOVNUMBER));
    }

    @Test
    public void testDeleteTransport() throws EntityNotFoundException {
        transportResource.deleteTransport(TRANSPORT_ID);

        BDDMockito.verify(transportService)
            .deleteTransport(eq(TRANSPORT_ID));
    }
}
