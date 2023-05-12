package library.hieund.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import library.hieund.exception.CustomException;
import library.hieund.model.Book;
import library.hieund.service.BookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/books", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Api(tags = "books")
@RequiredArgsConstructor

public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;
//    int code = 0;
//    String message = "";

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @ExceptionHandler(CustomException.class)
    public String addBook(@RequestBody BookDTO book) {
	JSONObject jsonObject = new JSONObject();
	JSONObject errors = new JSONObject();
	if (!NumberUtils.isParsable(book.getTotal())) {
	    errors.put("message", "Total must be numeric");
	} else if (!NumberUtils.isParsable(book.getStock())) {
	    errors.put("message", "Stock must be numeric");
	} else {
	    return bookService.addBook(modelMapper.map(book, Book.class));

	}
	jsonObject.put("error", errors);
	return jsonObject.toString();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
	    @RequestParam(value = "title", required = false, defaultValue = "") String title,
	    @RequestParam(value = "author", required = false, defaultValue = "") String author) {
	if (id != 0) {
	    return bookService.deleteById(id);
	} else {
	    return bookService.deleteByTitleAndAuthor(title, author);
	}

    }

}
