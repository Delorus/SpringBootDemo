package ru.tinkoff.demo.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.demo.dto.TransportChangeRequest;
import ru.tinkoff.demo.dto.TransportCreateRequest;
import ru.tinkoff.demo.entity.TransportEntity;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.error.TransportWithAlreadyExistingGovnumberException;
import ru.tinkoff.demo.mapper.TransportMapper;
import ru.tinkoff.demo.model.Transport;
import ru.tinkoff.demo.repository.TransportRepository;

@Service
public class TransportService {

    private final TransportRepository transportRepository;
    private final TransportMapper transportMapper;

    public TransportService(TransportRepository transportRepository,
                            TransportMapper transportMapper) {
        this.transportRepository = transportRepository;
        this.transportMapper = transportMapper;
    }

    public List<Transport> getAll() {
        return transportMapper.mapEntitiesToModels(transportRepository.findAllByOrderByGovnumber());
    }

    public Transport createTransport(TransportCreateRequest createRequest) {
        final TransportEntity transportEntity = transportMapper.createRequestToEntity(createRequest);

        return transportMapper.entityToModel(transportRepository.save(transportEntity));
    }

    public Transport updateTransport(Long id, TransportChangeRequest changeRequest)
        throws EntityNotFoundException {
        final TransportEntity transportEntity = transportRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transport", id));

        applyChangeRequestToEntity(changeRequest, transportEntity);

        return transportMapper.entityToModel(transportRepository.saveAndFlush(transportEntity));
    }

    public Transport getTransport(Long id) throws EntityNotFoundException {
        final TransportEntity transportEntity = transportRepository.findFullById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transport", id));

        return transportMapper.entityToFullModel(transportEntity);
    }

    public void deleteTransport(Long id) throws EntityNotFoundException {
        try {
            transportRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Transport", id);
        }
    }

    public Transport getTransportByGovnumber(String govnumber) throws TransportWithAlreadyExistingGovnumberException {
        final TransportEntity transportEntity = transportRepository.findByGovnumberIgnoreCase(govnumber)
            .orElseThrow(() -> new TransportWithAlreadyExistingGovnumberException(govnumber));

        return transportMapper.entityToFullModel(transportEntity);
    }

    public List<Transport> getAllTransportsByPersonId(long PersonId) {
        return transportMapper.mapEntitiesToModels(transportRepository.findAllByPersonIdOrderByGovnumber(PersonId));
    }

    private void applyChangeRequestToEntity(TransportChangeRequest changeRequest, TransportEntity targetEntity) {
        final TransportEntity changeEntity = transportMapper.changeRequestToEntity(changeRequest);

        targetEntity.setGovnumber(changeEntity.getGovnumber());
        targetEntity.setPerson(changeEntity.getPerson());
    }
}
