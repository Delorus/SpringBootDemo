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
import ru.tinkoff.demo.dto.CityChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.City;
import ru.tinkoff.demo.service.CityService;

@RestController
@RequestMapping("/api/cities")
@Api(tags = "city", description = "Города")
public class CityResource {

    private final CityService cityService;

    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    @ApiOperation("Создать новый город")
    public City createCity(@Valid @RequestBody CityChangeRequest changeRequest) {
        return cityService.createCity(changeRequest);
    }

    @PutMapping("/{cityId}")
    @ApiOperation("Обновить город")
    public City updateCity(@PathVariable("cityId") Long cityId,
                           @Valid @RequestBody CityChangeRequest changeRequest) throws EntityNotFoundException {
        return cityService.updateCity(cityId, changeRequest);
    }

    @GetMapping("{cityId}")
    @ApiOperation("Получить город")
    public City getCity(@PathVariable("cityId") Long cityId) throws EntityNotFoundException {
        return cityService.getCity(cityId);
    }

    @GetMapping("/name/{cityName}")
    @ApiOperation("Получить город по имени")
    public City getCityByName(@PathVariable("cityName") String cityName) {
        return cityService.getCityByName(cityName);
    }

    @GetMapping
    @ApiOperation("Получить список всех городов")
    public List<City> getAll() {
        return cityService.getAll();
    }

    @DeleteMapping("{cityId}")
    @ApiOperation("Удалить марку автомобиля")
    public void deleteCity(@PathVariable("cityId") Long cityId) throws EntityNotFoundException {
        cityService.deleteCity(cityId);
    }
}
