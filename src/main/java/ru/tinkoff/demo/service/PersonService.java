package ru.tinkoff.demo.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.demo.dto.PersonChangeRequest;
import ru.tinkoff.demo.entity.PersonEntity;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.mapper.PersonMapper;
import ru.tinkoff.demo.model.Person;
import ru.tinkoff.demo.repository.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<Person> getAll() {
        return personMapper.mapEntitiesToModels(personRepository.findAllByOrderByLastName());
    }

    public Person createPerson(PersonChangeRequest request) {
        final PersonEntity newPersonEntity = personMapper.changeRequestToEntity(request);

        return personMapper.entityToModel(personRepository.save(newPersonEntity));
    }

    public Person updatePerson(Long id, PersonChangeRequest changeRequest) throws EntityNotFoundException {
        final PersonEntity entity = personRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person", id));

        applyChangeRequestToEntity(changeRequest, entity);

        return personMapper.entityToModel(personRepository.saveAndFlush(entity));
    }

    public void deletePerson(Long id) throws EntityNotFoundException {
        try {
            personRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Person", id);
        }
    }

    public Person getPerson(Long id) throws EntityNotFoundException {
        final PersonEntity entity = personRepository.findFullById(id)
            .orElseThrow(() -> new EntityNotFoundException("Person", id));

        return personMapper.entityToFullModel(entity);
    }

    public List<Person> getAllByNameWithTransports(String firstName, String lastName, String parentName) {
        List<Person> persons = personMapper.mapEntitiesToFullModels(personRepository
                                                                        .findAllByFirstNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCaseAndParentNameStartsWithIgnoreCase(
                                                                            firstName, lastName, parentName));
        return persons;
    }

    private void applyChangeRequestToEntity(PersonChangeRequest changeRequest, PersonEntity targetEntity) {
        final PersonEntity changeRequestEntity = personMapper.changeRequestToEntity(changeRequest);

        targetEntity.setFirstName(changeRequestEntity.getFirstName());
        targetEntity.setParentName(changeRequestEntity.getParentName());
        targetEntity.setLastName(changeRequestEntity.getLastName());
        targetEntity.setCity(changeRequestEntity.getCity());
    }
}
