package epicode.u5d8hw.controllers;

import epicode.u5d8hw.dto.AuthorDTO;
import epicode.u5d8hw.dto.NewAuthorDTO;
import epicode.u5d8hw.entities.Author;
import epicode.u5d8hw.services.AuthorsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    private AuthorsService authorsService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO saveAuthor(@Valid @RequestPart("author") NewAuthorDTO body,
                                @RequestPart(value = "avatar", required = false) MultipartFile file) throws Exception {
        return authorsService.save(body, file);
    }

    @GetMapping("")
    public Page<Author> getAuthors(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        return authorsService.getAuthors(page, size, sortBy);
    }

    @GetMapping("/{authorId}")
    public Author findById(@PathVariable int authorId){
        return authorsService.findById(authorId);
    }

    @PutMapping("/{authorId}")
    public Author findAndUpdate(@PathVariable int authorId, @RequestBody Author body){
        return authorsService.findByIdAndUpdate(authorId, body);
    }

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable int authorId) {
        authorsService.findByIdAndDelete(authorId);
    }
}
