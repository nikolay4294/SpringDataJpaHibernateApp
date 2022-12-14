package ru.vorobiev.springApp.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vorobiev.springApp.models.Person;
import ru.vorobiev.springApp.services.PeopleRepositoriesImpl;

@Component
public class PersonValidator implements Validator {

    private final PeopleRepositoriesImpl peopleService;

    public PersonValidator(PeopleRepositoriesImpl peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (peopleService.getPersonBuFullName(person.getFio()).isPresent()) {
            errors.rejectValue("fio", "", "Человек с таким именем уже существует");
        }
    }
}
