package br.com.fiap.checkpoint.service;

import br.com.fiap.checkpoint.dto.BookRequestDTO;
import br.com.fiap.checkpoint.dto.BookResponseDTO;
import br.com.fiap.checkpoint.model.Book;
import br.com.fiap.checkpoint.repository.BookRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private MeterRegistry registry;

    private Counter booksCreatedCounter;

    @PostConstruct
    public void initMetrics() {
        booksCreatedCounter = Counter.builder("books_created_total")
                .description("Total de livros criados com sucesso")
                .register(registry);
    }

    public List<BookResponseDTO> getAllBooks(){
        List<Book> books = repository.findAll();
        return books.stream().map(this::convertToDto).toList();
    }

    public void createBook(BookRequestDTO dto){
        Timer.Sample sample = Timer.start(registry);

        Book book = convertToEntity(dto);

        repository.save(book);

        sample.stop(registry.timer("book.creation.time"));

        booksCreatedCounter.increment();
    }

    public void delete(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
    }

    public BookResponseDTO getById(Long id){
        Optional<Book> bookById =  repository.findById(id);
        if (bookById.isEmpty()){
            throw new IllegalArgumentException("Livro de id " + id + " nao encontrado");
        }
        return this.convertToDto(bookById.get());
    }

    public void update(Long id, BookRequestDTO dto) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()){
            if (dto.title() != null){
                book.get().setTitle(dto.title());
            }
            if (dto.authorName() != null){
                book.get().setAuthorName(dto.authorName());
            }
            if (dto.releaseDate() != null){
                book.get().setReleaseDate(dto.releaseDate());
            }
            if (dto.genre() != null){
                book.get().setGenre(dto.genre());
            }
            if (dto.numberOfPages() > 0){
                book.get().setNumberOfPages(dto.numberOfPages());
            }
            if (dto.publisher() != null){
                book.get().setPublisher(dto.publisher());
            }
            if (dto.price() != null){
                book.get().setPrice(dto.price());
            }
            if (dto.publisherPhone() != null){
                book.get().setPublisherPhone(dto.publisherPhone());
            }
            repository.save(book.get());
        }
    }

    private BookResponseDTO convertToDto(Book book){
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getReleaseDate(),
                book.getGenre(),
                book.getNumberOfPages(),
                book.getPublisher(),
                book.getPublisherPhone(),
                book.getPrice());
    }

    private Book convertToEntity(BookRequestDTO dto){
        Book entity = new Book();

        entity.setTitle(dto.title());
        entity.setAuthorName(dto.authorName());
        entity.setReleaseDate(LocalDate.now());
        entity.setGenre(dto.genre());
        entity.setNumberOfPages(dto.numberOfPages());
        entity.setPublisher(dto.publisher());
        entity.setPrice(dto.price());
        entity.setPublisherPhone(dto.publisherPhone());

        return entity;
    }
}

