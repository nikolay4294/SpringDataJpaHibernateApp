package ru.vorobiev.springApp.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 20, message = "Название книги должно быть от 2 до 20 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Поле автор книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Поле автор должно быть от 2 до 100 символов")
    @Column(name = "author")
    private String author;

    @NotEmpty(message = "Год написания не должен быть пустым")
    @Min(value = 1500, message = "Год написания книги не может быть ранее, чем 1500")
    @Column(name = "year_of_writing")
    private int yearOfWriting;

    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id")
    private Person owner;

    //текущее время в млс, когда книгу кто-то забирает.Для авт проверки на просроченность книги
    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    //hibernate не видит это поле.В нем будет сохранятся значение просрочена книга или нет.
    @Transient
    private boolean expired;

    public Book() {

    }

    public Book(String name, String author, int yearOfWriting) {
        this.name = name;
        this.author = author;
        this.yearOfWriting = yearOfWriting;
    }

    public Book(int id, String name, String author, int yearOfWriting, Person owner, Date takenAt, boolean expired) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.yearOfWriting = yearOfWriting;
        this.owner = owner;
        this.takenAt = takenAt;
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
