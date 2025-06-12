package epicode.u5d8hw.controllers;

import epicode.u5d8hw.dto.BlogPostDTO;
import epicode.u5d8hw.dto.NewBlogPostDTO;
import epicode.u5d8hw.services.BlogsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogsController {
    @Autowired
    private BlogsService blogsService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPostDTO saveBlog(@Valid @RequestPart("blog") NewBlogPostDTO body,
                                @RequestPart(value = "cover", required = false) MultipartFile cover) throws Exception {
        return blogsService.save(body, cover);
    }

    @GetMapping("")
    public List<BlogPostDTO> getBlogs(@RequestParam(required = false) Integer authorId) {
        if(authorId != null) return blogsService.findByAuthor(authorId);
        else return blogsService.getBlogs();
    }

    @GetMapping("/{blogId}")
    public BlogPostDTO findById(@PathVariable int blogId) {
        return blogsService.findByIdDTO(blogId);
    }

    @PutMapping("/{blogId}")
    public BlogPostDTO findAndUpdate(@PathVariable int blogId,
                                     @Valid @RequestPart("blog") NewBlogPostDTO body,
                                     @RequestPart(value = "cover", required = false) MultipartFile cover) throws Exception {
        return blogsService.findByIdAndUpdate(blogId, body, cover);
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable int blogId) {
        blogsService.findByIdAndDelete(blogId);
    }
}

