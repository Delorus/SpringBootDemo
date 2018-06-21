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
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.error.EntityAlreadyExistException;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.Mark;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkServiceIntTest {

    @Autowired
    private MarkService markService;

    private MarkChangeRequest mitsubishiChangeRequest;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        mitsubishiChangeRequest = MarkChangeRequest.builder()
            .name("Mitsubishi")
            .build();
    }

    @Test
    public void testGetAll() {
        final List<String> marks = markService.getAll().stream()
            .map(Mark::getName)
            .collect(Collectors.toList());

        assertThat(marks).contains("Mazda", "Toyota");
    }

    @Test
    public void testCreateMark() throws Exception {
        Mark mark1 = markService.createMark(mitsubishiChangeRequest);

        assertThat(mark1.getName()).isEqualTo("Mitsubishi");
    }

    @Test
    public void testUpdateMark() throws Exception {
        val changeRequest = MarkChangeRequest.builder()
            .name("BMW")
            .build();
        Long id = markService.createMark(changeRequest).getId();
        changeRequest.setName("Mitsubishi");

        Mark mark = markService.updateMark(id, changeRequest);

        assertThat(mark.getId()).isEqualTo(id);
        assertThat(mark.getName()).isEqualTo("Mitsubishi");
    }

    @Test
    public void testGetMark() throws Exception {
        Long id = markService.createMark(mitsubishiChangeRequest).getId();
        Mark mark = markService.getMark(id);

        assertThat(mark.getId()).isEqualTo(id);
        assertThat(mark.getName()).isEqualTo(mitsubishiChangeRequest.getName());
    }

    @Test
    public void testDeleteCity() throws Exception {
        Long id = markService.createMark(mitsubishiChangeRequest).getId();

        markService.deleteMark(id);
    }

    @Test
    public void testUpdateMarkNotExistingMarkUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Mark with id=" + id + " is not found.");

        markService.updateMark(id, mitsubishiChangeRequest);
    }

    @Test
    public void testDeleteMarkNotExistingMarkUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Mark with id=" + id + " is not found.");

        markService.deleteMark(id);
    }

    @Test
    public void testGetMarkNotExistingMarkUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Mark with id=" + id + " is not found.");

        markService.getMark(id);
    }

}
