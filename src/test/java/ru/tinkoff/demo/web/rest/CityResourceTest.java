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
import ru.tinkoff.demo.dto.CityChangeRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.service.CityService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CityResource.class)
public class CityResourceTest {

    private static final Long CITY_ID = new Random().nextLong();
    private static final String CITY_NAME = "Воронеж";

    @Autowired
    private CityResource cityResource;

    @MockBean
    private CityService cityService;

    @Test
    public void testCreateCity() {
        val request = CityChangeRequest.builder()
            .name(CITY_NAME)
            .build();
        cityResource.createCity(request);

        BDDMockito.verify(cityService)
            .createCity(eq(
                CityChangeRequest.builder()
                    .name(CITY_NAME)
                    .build()
            ));
    }

    @Test
    public void testUpdateCity() throws EntityNotFoundException {
        val request = CityChangeRequest.builder()
            .name(CITY_NAME)
            .build();
        cityResource.updateCity(CITY_ID, request);

        BDDMockito.verify(cityService)
            .updateCity(eq(CITY_ID), eq(
                CityChangeRequest.builder()
                    .name(CITY_NAME)
                    .build()
            ));
    }

    @Test
    public void testGetCity() throws EntityNotFoundException {
        cityResource.getCity(CITY_ID);

        BDDMockito.verify(cityService)
            .getCity(eq(CITY_ID));
    }

    @Test
    public void testGetCityByName() {
        cityResource.getCityByName(CITY_NAME);

        BDDMockito.verify(cityService)
            .getCityByName(eq(CITY_NAME));
    }

    @Test
    public void testGetAll() {
        cityResource.getAll();

        BDDMockito.verify(cityService)
            .getAll();
    }

    @Test
    public void testDeleteCity() throws EntityNotFoundException {
        cityResource.deleteCity(CITY_ID);

        BDDMockito.verify(cityService)
            .deleteCity(eq(CITY_ID));
    }
}
