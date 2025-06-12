package epicode.u5d8hw.services;

import epicode.u5d8hw.dto.BlogPostDTO;
import epicode.u5d8hw.dto.NewBlogPostDTO;
import epicode.u5d8hw.entities.Author;
import epicode.u5d8hw.entities.Blogpost;
import epicode.u5d8hw.exceptions.NotFoundException;
import epicode.u5d8hw.repositories.BlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogsService {
    @Autowired
    private BlogsRepository blogsRepository;
    @Autowired
    private AuthorsService authorsService;
    @Autowired
    private UploadService uploadService;

    public BlogPostDTO save(NewBlogPostDTO body, MultipartFile coverFile) throws Exception {
        Author author = authorsService.findById(body.getAuthorId());
        Blogpost post = new Blogpost();
        post.setAuthor(author);
        post.setCategory(body.getCategory());
        post.setTitle(body.getTitle());
        post.setContent(body.getContent());
        post.setReadingTime(body.getReadingTime());

        if (coverFile != null && !coverFile.isEmpty()) {
            post.setCover(uploadService.upload(coverFile));
        } else {
            post.setCover("https://picsum.photos/200/300");
        }

        return convertToDTO(blogsRepository.save(post));
    }

    public List<BlogPostDTO> getBlogs() {
        return blogsRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BlogPostDTO findByIdDTO(int id) {
        return convertToDTO(findById(id));
    }

    public Blogpost findById(int id) {
        return blogsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(int id) {
        blogsRepository.delete(findById(id));
    }

    public BlogPostDTO findByIdAndUpdate(int id, NewBlogPostDTO body, MultipartFile coverFile) throws Exception {
        Blogpost found = findById(id);
        found.setCategory(body.getCategory());
        found.setTitle(body.getTitle());
        found.setContent(body.getContent());
        found.setReadingTime(body.getReadingTime());

        if (found.getAuthor().getId() != body.getAuthorId()) {
            found.setAuthor(authorsService.findById(body.getAuthorId()));
        }

        if (coverFile != null && !coverFile.isEmpty()) {
            found.setCover(uploadService.upload(coverFile));
        }

        return convertToDTO(blogsRepository.save(found));
    }

    public List<BlogPostDTO> findByAuthor(int authorId) {
        Author author = authorsService.findById(authorId);
        return blogsRepository.findByAuthor(author).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BlogPostDTO convertToDTO(Blogpost post) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCategory(post.getCategory());
        dto.setReadingTime(post.getReadingTime());
        dto.setCover(post.getCover());
        dto.setAuthorId(post.getAuthor().getId());
        return dto;
    }
}
