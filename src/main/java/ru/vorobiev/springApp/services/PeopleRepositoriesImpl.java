package ru.vorobiev.springApp.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobiev.springApp.models.Book;
import ru.vorobiev.springApp.models.Person;
import ru.vorobiev.springApp.repositories.PeopleRepositories;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleRepositoriesImpl {

    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleRepositoriesImpl(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> findAll() {
        return peopleRepositories.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepositories.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepositories.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> getPersonBuFullName(String fullName) {
        return peopleRepositories.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepositories.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            //Внизу итерируемся по книгам, потому что они точно будут загружены

            person.get().getBooks().forEach(book -> {
                //получаем время  в течении которого книга у человека
                long diffInMilles = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMilles > 864000000)
                    book.setExpired(true);
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
