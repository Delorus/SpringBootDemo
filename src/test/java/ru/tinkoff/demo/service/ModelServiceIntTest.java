package ru.tinkoff.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.dto.ModelChangeRequest;
import ru.tinkoff.demo.error.EntityAlreadyExistException;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.Mark;
import ru.tinkoff.demo.model.Model;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelServiceIntTest {

    @Autowired
    private ModelService modelService;
    @Autowired
    private MarkService markService;

    private Mark mitsubishiTestMark;
    private Mark renaultTestMark;
    private ModelChangeRequest pageroModelChangeRequest;
    private ModelChangeRequest meganModelChangeRequest;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MarkChangeRequest changeRequest = MarkChangeRequest.builder()
            .name("Mitsubishi")
            .build();
        MarkChangeRequest changeRequest2 = MarkChangeRequest.builder()
            .name("Renault")
            .build();
        mitsubishiTestMark = markService.createMark(changeRequest);
        renaultTestMark = markService.createMark(changeRequest2);
        pageroModelChangeRequest = ModelChangeRequest.builder()
            .mark(LinkToModel.of(mitsubishiTestMark.getId()))
            .name("Pagero")
            .build();
        meganModelChangeRequest = ModelChangeRequest.builder()
            .mark(LinkToModel.of(renaultTestMark.getId()))
            .name("Megan")
            .build();
    }

    @Test
    public void testGetAll() {
        final List<String> models = modelService.getAll().stream()
            .map(Model::getName)
            .collect(Collectors.toList());
        final List<Long> marks = modelService.getAll().stream()
            .map(Model::getMark)
            .map(Mark::getId)
            .collect(Collectors.toList());

        assertThat(models).contains("cx-5", "Camry");
        assertThat(marks).contains(1L, 2L);
    }

    @Test
    public void testCreateModel() {
        Model createdModel = modelService.createModel(pageroModelChangeRequest);

        assertThat(createdModel.getName()).isEqualTo("Pagero");
        assertThat(createdModel.getMark().getId()).isEqualTo(mitsubishiTestMark.getId());
    }

    @Test
    public void testUpdateModel() throws EntityNotFoundException {
        Long pageroId = modelService.createModel(pageroModelChangeRequest).getId();
        Long meganId = modelService.createModel(meganModelChangeRequest).getId();
        val pageroModelChangeRequest = ModelChangeRequest.builder()
            .mark(LinkToModel.of(mitsubishiTestMark.getId()))
            .name("Lancer")
            .build();
        val meganModelChangeRequest = ModelChangeRequest.builder()
            .mark(LinkToModel.of(mitsubishiTestMark.getId()))
            .name("Pagero")
            .build();

        Model updatedModel1 = modelService.updateModel(pageroId, pageroModelChangeRequest);
        Model updatedModel2 = modelService.updateModel(meganId, meganModelChangeRequest);

        assertThat(updatedModel1.getId()).isEqualTo(pageroId);
        assertThat(updatedModel2.getId()).isEqualTo(meganId);
        assertThat(updatedModel1.getName()).isEqualTo(pageroModelChangeRequest.getName());
        assertThat(updatedModel2.getName()).isEqualTo(meganModelChangeRequest.getName());
        assertThat(updatedModel2.getMark().getId()).isEqualTo(meganModelChangeRequest.getMark().getId());
    }

    @Test
    public void testDeleteModel() throws EntityAlreadyExistException, EntityNotFoundException {
        Long id = modelService.createModel(pageroModelChangeRequest).getId();

        modelService.deleteModel(id);
    }

    @Test
    public void testGetModel() throws EntityNotFoundException {
        Long id = 2L;
        Model model = modelService.getModel(id);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Camry");
        assertThat(model.getMark().getName()).isEqualTo("Toyota");
    }

    @Test
    public void testDeleteModelNotExistingModelUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Model with id=" + id + " is not found");

        modelService.deleteModel(id);
    }

    @Test
    public void testUpdateModelNotExistingModelUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Model with id=" + id + " is not found");

        modelService.updateModel(id, meganModelChangeRequest);
    }

    @Test
    public void testGetModelNotExistingModelUnsuccesfulFlow() throws EntityNotFoundException {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Model with id=" + id + " is not found");

        modelService.getModel(id);
    }

}
