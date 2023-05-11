package library.hieund.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @GetMapping("/test")
    @ApiOperation(value = "${BookController.test}")
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied============"), //
	    @ApiResponse(code = 422, message = "Book title is already in use") })
    public String test() {
	return bookService.test();
    }

    @PostMapping("/addBook")
    @ApiOperation(value = "${BookController.addBook}")
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 422, message = "Book title is already in use") })
    public String addBook(@ApiParam("Add book") @RequestBody BookDTO book) {
	return bookService.addBook(modelMapper.map(book, Book.class));
    }

}
