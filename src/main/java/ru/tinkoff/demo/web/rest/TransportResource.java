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
import ru.tinkoff.demo.dto.TransportChangeRequest;
import ru.tinkoff.demo.dto.TransportCreateRequest;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.error.TransportWithAlreadyExistingGovnumberException;
import ru.tinkoff.demo.model.Transport;
import ru.tinkoff.demo.service.TransportService;

@RestController
@RequestMapping("/api/transports")
@Api(tags = "transports", description = "Транспортные средства")
public class TransportResource {

    private final TransportService transportService;

    public TransportResource(TransportService transportService) {
        this.transportService = transportService;
    }

    @PostMapping
    @ApiOperation("Создать новое транспортное средство")
    public Transport createTransport(@Valid @RequestBody TransportCreateRequest createRequest) {
        return transportService.createTransport(createRequest);
    }

    @PutMapping("/{transportId}")
    @ApiOperation("Обновить транспортное средство")
    public Transport updateTransport(@PathVariable("transportId") Long transportId,
                                     @Valid @RequestBody TransportChangeRequest changeRequest)
        throws EntityNotFoundException {
        return transportService.updateTransport(transportId, changeRequest);
    }

    @GetMapping("{transportId}")
    @ApiOperation("Получить транспортное средство")
    public Transport getTransport(@PathVariable("transportId") Long transportId) throws EntityNotFoundException {
        return transportService.getTransport(transportId);
    }

    @GetMapping
    @ApiOperation("Получить список всех транспортных средств")
    public List<Transport> getAll() {
        return transportService.getAll();
    }

    @GetMapping("/person/{personId}")
    @ApiOperation("Получить все транспортные средства по персоне-владельцу")
    public List<Transport> getAllTransportsByPersonId(@PathVariable("personId") Long personId) {
        return transportService.getAllTransportsByPersonId(personId);
    }

    @GetMapping("/govnumber/{govnumber}")
    @ApiOperation("Получить транспортное средство по государственному номеру")
    public Transport getTransportByGovnumber(@PathVariable("govnumber") String govnumber)
        throws TransportWithAlreadyExistingGovnumberException {
        return transportService.getTransportByGovnumber(govnumber);
    }

    @DeleteMapping("/{transportId}")
    @ApiOperation("Удалить транспортное средство")
    public void deleteTransport(@PathVariable("transportId") Long transportId) throws EntityNotFoundException {
        transportService.deleteTransport(transportId);
    }
}
