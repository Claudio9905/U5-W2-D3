package claudiopostiglione.u5w2d3.services;

import claudiopostiglione.u5w2d3.entities.Autore;
import claudiopostiglione.u5w2d3.entities.Blog;
import claudiopostiglione.u5w2d3.exceptions.BadRequestException;
import claudiopostiglione.u5w2d3.exceptions.IdNotFoundException;
import claudiopostiglione.u5w2d3.payloads.BlogPayload;
import claudiopostiglione.u5w2d3.repositories.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AutoreService autoreService;

    // 1.
    public Page<Blog> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.blogRepository.findAll(pageable);
    }

    // 2.
    public Blog findBlogById(UUID idBlog) {
        return this.blogRepository.findById(idBlog).orElseThrow(() -> new IdNotFoundException(idBlog));
    }

    // 3.
    public Blog saveBlog(BlogPayload body) {

        //Verifico che un blog non ci sia già
        this.blogRepository.findByTitolo(body.getTitolo()).ifPresent(blog -> {
            throw new BadRequestException("Il blog con titolo " + blog.getTitolo() + " esiste già");
        });
        Blog newBlog = new Blog(body.getCategoria(), body.getTitolo(), body.getCover(), body.getContenuto(), body.getTempoDiLettura(),this.autoreService.findAutoreById(body.getAuthorId()));
        this.blogRepository.save(newBlog);
        log.info("Il blog + " + body.getTitolo() + " è stato aggiunto al database");
        return newBlog;
    }

    // 4.
    public Blog findBlogByIdAndUpdate(UUID idBlog, BlogPayload newBody) {

        Blog blogFound = this.findBlogById(idBlog);

        if (!blogFound.getTitolo().equals(newBody.getTitolo())) {
            this.blogRepository.findByTitolo(newBody.getTitolo()).ifPresent(blog -> {
                        throw new BadRequestException("Il blog con titolo " + blog.getTitolo() + " esiste già");
                    }
            );
        }

        blogFound.setCategoria(newBody.getCategoria());
        blogFound.setTitolo(newBody.getTitolo());
        blogFound.setCover(newBody.getCover());
        blogFound.setContenuto(newBody.getContenuto());
        blogFound.setTempoDiLettura(newBody.getTempoDiLettura());

        Blog updateBlog = this.blogRepository.save(blogFound);

        log.info("Il blog con id " + updateBlog.getId() + " è stato modificato correttamente");

        return blogFound;

    }

    // 5.
    public void findBlogByIdAndDelete(UUID idBlog) {
        Blog blogFound = this.findBlogById(idBlog);
        this.blogRepository.delete(blogFound);
    }
}
