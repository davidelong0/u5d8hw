package epicode.u5d8hw.services;

import epicode.u5d8hw.entities.Author;
import epicode.u5d8hw.entities.Blogpost;
import epicode.u5d8hw.exceptions.NotFoundException;
import epicode.u5d8hw.payloads.NewBlogPostPayload;
import epicode.u5d8hw.repositories.AuthorsRepository;
import epicode.u5d8hw.repositories.BlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogsService {
    @Autowired
    private BlogsRepository blogsRepository;
    @Autowired
    private AuthorsService authorsService;

    public Blogpost save(NewBlogPostPayload body) {
        Author author = authorsService.findById(body.getAuthorId());
        Blogpost newBlogPost = new Blogpost();
        newBlogPost.setReadingTime(body.getReadingTime());
        newBlogPost.setContent(body.getContent());
        newBlogPost.setTitle(body.getTitle());
        newBlogPost.setAuthor(author);
        newBlogPost.setCategory(body.getCategory());
        newBlogPost.setCover("http://picsum.photos/200/300");
        return blogsRepository.save(newBlogPost);
    }

    public List<Blogpost> getBlogs() {
        return blogsRepository.findAll();
    }

    public Blogpost findById(int id) {
        return blogsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(int id) {
        Blogpost found = this.findById(id);
        blogsRepository.delete(found);
    }

    public Blogpost findByIdAndUpdate(int id, NewBlogPostPayload body) {
        Blogpost found = this.findById(id);

        found.setReadingTime(body.getReadingTime());
        found.setContent(body.getContent());
        found.setTitle(body.getTitle());
        found.setCategory(body.getCategory());

        if(found.getAuthor().getId()!= body.getAuthorId()) {
            Author newAuthor = authorsService.findById(body.getAuthorId());
            found.setAuthor(newAuthor);
        }

        return blogsRepository.save(found);
    }

    public List<Blogpost> findByAuthor(int authorId){
        Author author = authorsService.findById(authorId);
        return blogsRepository.findByAuthor(author);
    }
}
