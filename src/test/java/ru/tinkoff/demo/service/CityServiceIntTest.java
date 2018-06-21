package ru.tinkoff.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import lombok.val;

import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.demo.dto.CityChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.model.City;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityServiceIntTest {

    @Autowired
    private CityService cityService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CityChangeRequest ekbChangeRequest;

    @Before
    public void setUp() {
        ekbChangeRequest = CityChangeRequest.builder()
            .name("Екатеринбург")
            .build();
    }

    @Test
    public void testGetAll() {
        final List<String> cities = cityService.getAll().stream()
            .map(City::getName)
            .collect(Collectors.toList());

        assertThat(cities).contains("Екатеринбург", "Новосибирск");
    }

    @Test
    public void testCreateCity() throws Exception {
        City city = cityService.createCity(ekbChangeRequest);

        assertThat(city.getName()).isEqualTo(ekbChangeRequest.getName());
    }

    @Test
    public void testUpdateCity() throws Exception {
        val changeRequest = CityChangeRequest.builder()
            .name("Челябинск")
            .build();
        Long id = cityService.createCity(changeRequest).getId();

        changeRequest.setName("Караганда");
        City city = cityService.updateCity(id, changeRequest);

        assertThat(city.getId()).isEqualTo(id);
        assertThat(city.getName()).isEqualTo(changeRequest.getName());
    }

    @Test
    public void testGetCity() throws Exception {
        Long id = cityService.createCity(ekbChangeRequest).getId();

        City city = cityService.getCity(id);
        assertThat(city.getId()).isEqualTo(id);
        assertThat(city.getName()).isEqualTo(ekbChangeRequest.getName());
    }

    @Test
    public void testGetCityByName() throws Exception {
        String name1 = "Новосибир";
        String name2 = "НовосибирСК";
        City city1 = cityService.getCityByName(name1);
        City city2 = cityService.getCityByName(name2);

        assertThat(city1.getName()).isEqualTo("Новосибирск");
        assertThat(city2.getName()).isEqualTo("Новосибирск");
    }

    @Test
    public void testDeleteCity() throws Exception {
        Long id = cityService.createCity(ekbChangeRequest).getId();

        cityService.deleteCity(id);
    }

    @Test
    public void testUpdateCityNotExistingCityUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("City with id=" + id + " is not found.");

        cityService.updateCity(id, ekbChangeRequest);
    }

    @Test
    public void testDeleteCityNotExistingCityUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("City with id=" + id + " is not found.");

        cityService.deleteCity(id);
    }

    @Test
    public void testGetCityNotExistingCityUnsuccesfulFlow() throws Exception {
        Long id = new Random().nextLong();

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("City with id=" + id + " is not found.");

        cityService.getCity(id);
    }
}
