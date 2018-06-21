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
import ru.tinkoff.demo.dto.ModelChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.Model;
import ru.tinkoff.demo.service.ModelService;

@RestController
@RequestMapping("/api/models")
@Api(tags = "model", description = "модели транспортных средств")
public class ModelResource {

    private final ModelService modelService;

    public ModelResource(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping
    @ApiOperation("Создать новую модель транспортного средства")
    public Model createModel(@Valid @RequestBody ModelChangeRequest changeRequest) {
        return modelService.createModel(changeRequest);
    }

    @PutMapping("/{modelId}")
    @ApiOperation("Обновить модель транспортного средства")
    public Model updateModel(@PathVariable("modelId") Long modelId,
                             @Valid @RequestBody ModelChangeRequest changeRequest) throws EntityNotFoundException {
        return modelService.updateModel(modelId, changeRequest);
    }

    @GetMapping("{modelId}")
    @ApiOperation("Получить модель транспортного средства")
    public Model getModel(@PathVariable("modelId") Long modelId) throws EntityNotFoundException {
        return modelService.getModel(modelId);
    }

    @GetMapping
    @ApiOperation("Получить список всех моделей транспортных средств")
    public List<Model> getAll() {
        return modelService.getAll();
    }

    @DeleteMapping("/{modelId}")
    @ApiOperation("Удалить модель транспортного средства")
    public void deleteModel(@PathVariable("modelId") Long modelId) throws EntityNotFoundException {
        modelService.deleteModel(modelId);
    }
}
