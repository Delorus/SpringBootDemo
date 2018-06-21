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
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.service.MarkService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MarkResource.class)
public class MarkResourceTest {

    private static final Long MARK_ID = new Random().nextLong();
    private static final String MARK_NAME = "Volvo";

    @Autowired
    private MarkResource markResource;

    @MockBean
    private MarkService markService;

    @Test
    public void testCreateMark() {
        val request = MarkChangeRequest.builder()
            .name(MARK_NAME)
            .build();
        markResource.createMark(request);

        BDDMockito.verify(markService)
            .createMark(eq(
                MarkChangeRequest.builder()
                    .name(MARK_NAME)
                    .build()
            ));
    }

    @Test
    public void testUpdateMark() throws EntityNotFoundException {
        val request = MarkChangeRequest.builder()
            .name(MARK_NAME)
            .build();
        markResource.updateMark(MARK_ID, request);

        BDDMockito.verify(markService)
            .updateMark(eq(MARK_ID), eq(
                MarkChangeRequest.builder()
                    .name(MARK_NAME)
                    .build()
            ));
    }

    @Test
    public void testGetMark() throws EntityNotFoundException {
        markResource.getMark(MARK_ID);

        BDDMockito.verify(markService)
            .getMark(eq(MARK_ID));
    }

    @Test
    public void testGetAll() {
        markResource.getAll();

        BDDMockito.verify(markService)
            .getAll();
    }

    @Test
    public void testDeleteMark() throws EntityNotFoundException {
        markResource.deleteMark(MARK_ID);

        BDDMockito.verify(markService)
            .deleteMark(eq(MARK_ID));
    }
}
