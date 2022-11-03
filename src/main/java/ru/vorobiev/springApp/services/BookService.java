package ru.vorobiev.springApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobiev.springApp.models.Book;
import ru.vorobiev.springApp.models.Person;
import ru.vorobiev.springApp.repositories.BookRepositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepositories booksRepositories;

    @Autowired
    public BookService(BookRepositories booksRepositories) {
        this.booksRepositories = booksRepositories;
    }


    public List<Book> findAll(boolean sortByYear) {
        //сортируем книги по году. В BookController либо передается параметр sortByYear либо нет
        if (sortByYear)
            return booksRepositories.findAll(Sort.by("yearOfWriting"));
        else
            return booksRepositories.findAll();
    }

    //метод для пагинации. Либо сортируем, либо нет
    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepositories.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfWriting"))).getContent();
        else
            return booksRepositories.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepositories.findById(id);
        return foundBook.orElse(null);
    }

    public void save(Book book) {
        booksRepositories.save(book);
    }

    //метод для функционала поиска книги
    public List<Book> searchByTitle(String author) {
        //вызываем метод из BookRepository для поиска книг по автору
        //никакю логику не реализуем, весь функционал дает Spring Data JPA.
        return booksRepositories.findByTitleStartingWith(author);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepositories.findById(id).get();

        //добавляем новую книгу (которая не находилась в Persistence context), поэтому нужен save()
        updatedBook.setId(id);
        updatedBook.setAuthor(bookToBeUpdated.getAuthor()); //что бы не терялась связь при обновлении
        booksRepositories.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepositories.deleteById(id);
    }

    //метод возвращает null если у книги нет владельца
    public Person getBookOwner(int id) {
        return booksRepositories.findById(id).map(Book::getOwner).orElse(null);
    }

    //метод освобождает книгу (вызывается когда человек сдал книгу в библиотеку)
    @Transactional
    public void release(int id) {
        booksRepositories.findById(id).ifPresent(
                book -> {
                    book.setAuthor(null);
                    book.setTakenAt(null);
                }
        );
    }

    //метод устанавливает владельца, когда берут книгу
    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepositories.findById(id).ifPresent(book -> {
            book.setOwner(selectedPerson);
            book.setTakenAt(new Date());
        });
    }
}
