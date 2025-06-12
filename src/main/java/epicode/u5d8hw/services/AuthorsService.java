package epicode.u5d8hw.services;

import epicode.u5d8hw.dto.AuthorDTO;
import epicode.u5d8hw.dto.NewAuthorDTO;
import epicode.u5d8hw.entities.Author;
import epicode.u5d8hw.exceptions.BadRequestException;
import epicode.u5d8hw.exceptions.NotFoundException;
import epicode.u5d8hw.repositories.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthorsService {
    @Autowired
    private AuthorsRepository authorsRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private EmailSenderService emailSenderService;

    public AuthorDTO save(NewAuthorDTO body, MultipartFile avatarFile) throws Exception {
        authorsRepository.findByEmail(body.getEmail()).ifPresent(user -> {
            throw new BadRequestException("L'email " + body.getEmail() + " è già stata utilizzata");
        });

        Author author = new Author();
        author.setName(body.getName());
        author.setSurname(body.getSurname());
        author.setEmail(body.getEmail());
        author.setDateOfBirth(body.getDateOfBirth());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String url = uploadService.upload(avatarFile);
            author.setAvatar(url);
        } else {
            author.setAvatar("https://ui-avatars.com/api/?name=" + body.getName().charAt(0) + "+" + body.getSurname().charAt(0));
        }

        Author saved = authorsRepository.save(author);

        // invio email di conferma
        emailSenderService.sendEmail(
                saved.getEmail(),
                "Benvenuto in Epicode Blog!",
                "Ciao " + saved.getName() + ", il tuo autore è stato creato con successo."
        );

        AuthorDTO dto = new AuthorDTO();
        dto.setId(saved.getId());
        dto.setName(saved.getName());
        dto.setSurname(saved.getSurname());
        dto.setEmail(saved.getEmail());
        dto.setDateOfBirth(saved.getDateOfBirth());
        dto.setAvatar(saved.getAvatar());

        return dto;
    }

    public Page<Author> getAuthors(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return authorsRepository.findAll(pageable);
    }

    public Author findById(int id) {
        return authorsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(int id) {
        Author found = this.findById(id);
        authorsRepository.delete(found);
    }

    public Author findByIdAndUpdate(int id, Author body) {
        Author found = this.findById(id);
        found.setEmail(body.getEmail());
        found.setName(body.getName());
        found.setSurname(body.getSurname());
        found.setDateOfBirth(body.getDateOfBirth());
        found.setAvatar(body.getAvatar());
        return authorsRepository.save(found);
    }
}
