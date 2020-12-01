package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CompletedQuizService {

    @Autowired
    CompletedQuizRepository repository;

    public Page<CompletedQuiz> findAll(Integer pageNo, Integer pageSize, String sortBy, String username) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CompletedQuiz> pagedResult = repository.findAllQuizByUserPagination(username, paging);
        return pagedResult;
    }

    public void save(CompletedQuiz quiz) {
        repository.save(quiz);
    }
}
