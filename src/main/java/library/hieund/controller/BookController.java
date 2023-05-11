package library.hieund.controller;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import library.hieund.dto.BookDTO;
import library.hieund.model.Book;
import library.hieund.service.BookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@Api(tags = "books")
@RequiredArgsConstructor

public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public Map<String, Object> addBook(@RequestBody BookDTO book) {
	return bookService.addBook(modelMapper.map(book, Book.class));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public Map<String, Object> delete(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
	    @RequestParam(value = "title", required = false, defaultValue = "") String title,
	    @RequestParam(value = "author", required = false, defaultValue = "") String author) {
	if (id != 0) {
	    return bookService.deleteById(id);
	} else {
	    return bookService.deleteByTitleAndAuthor(title, author);
	}

    }

}
