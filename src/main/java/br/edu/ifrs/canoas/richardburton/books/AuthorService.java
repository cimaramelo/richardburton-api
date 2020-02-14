package br.edu.ifrs.canoas.richardburton.books;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AuthorService {

    @Inject
    private AuthorDAO authorDAO;

    public List<Author> search(Long afterId, int pageSize, String queryString) {

        return authorDAO.search(afterId, pageSize, queryString);
    }

}