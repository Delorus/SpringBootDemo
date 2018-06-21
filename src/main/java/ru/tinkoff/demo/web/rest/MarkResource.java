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
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.Mark;
import ru.tinkoff.demo.service.MarkService;

@RestController
@RequestMapping("/api/marks")
@Api(tags = "mark", description = "марки транспортных средств")
public class MarkResource {

    private final MarkService markService;

    public MarkResource(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping
    @ApiOperation("Создать новую марку")
    public Mark createMark(@Valid @RequestBody MarkChangeRequest changeRequest) {
        return markService.createMark(changeRequest);
    }

    @PutMapping("/{markId}")
    @ApiOperation("Обновить марку")
    public Mark updateMark(@PathVariable("markId") Long markId,
                           @Valid @RequestBody MarkChangeRequest changeRequest) throws EntityNotFoundException {
        return markService.updateMark(markId, changeRequest);
    }

    @GetMapping("{markId}")
    @ApiOperation("Получить марку")
    public Mark getMark(@PathVariable("markId") Long markId) throws EntityNotFoundException {
        return markService.getMark(markId);
    }

    @GetMapping
    @ApiOperation("Получить список марок")
    public List<Mark> getAll() {
        return markService.getAll();
    }

    @DeleteMapping("{markId}")
    @ApiOperation("Удалить марку")
    public void deleteMark(@PathVariable("markId") Long markId) throws EntityNotFoundException {
        markService.deleteMark(markId);
    }
}
