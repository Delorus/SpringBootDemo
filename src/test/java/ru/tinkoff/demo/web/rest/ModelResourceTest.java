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
import ru.tinkoff.demo.dto.ModelChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.service.ModelService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelResource.class)
public class ModelResourceTest {

    private static final Long MODEL_ID = new Random().nextLong();
    private static final Long MARK_ID = new Random().nextLong();
    private static final String MODEL_NAME = "Lancer";

    @Autowired
    private ModelResource modelResource;

    @MockBean
    private ModelService modelService;

    @Test
    public void testCreateModel() {
        val request = ModelChangeRequest.builder()
            .name(MODEL_NAME)
            .mark(LinkToModel.of(MARK_ID))
            .build();
        modelResource.createModel(request);

        BDDMockito.verify(modelService)
            .createModel(eq(
                ModelChangeRequest.builder()
                    .name(MODEL_NAME)
                    .mark(LinkToModel.of(MARK_ID))
                    .build()
            ));
    }

    @Test
    public void testUpdateModel() throws EntityNotFoundException {
        val request = ModelChangeRequest.builder()
            .name(MODEL_NAME)
            .mark(LinkToModel.of(MARK_ID))
            .build();
        modelResource.updateModel(MODEL_ID, request);

        BDDMockito.verify(modelService)
            .updateModel(eq(MODEL_ID), eq(
                ModelChangeRequest.builder()
                    .name(MODEL_NAME)
                    .mark(LinkToModel.of(MARK_ID))
                    .build()
            ));
    }

    @Test
    public void testGetModel() throws EntityNotFoundException {
        modelResource.getModel(MODEL_ID);

        BDDMockito.verify(modelService)
            .getModel(eq(MODEL_ID));
    }

    @Test
    public void testGetAll() {
        modelResource.getAll();

        BDDMockito.verify(modelService).getAll();
    }

    @Test
    public void testDeleteModel() throws EntityNotFoundException {
        modelResource.deleteModel(MODEL_ID);

        BDDMockito.verify(modelService)
            .deleteModel(eq(MODEL_ID));
    }
}
