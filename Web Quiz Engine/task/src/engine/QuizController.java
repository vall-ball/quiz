package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/quizzes", produces = "application/json")
public class QuizController {

    @Autowired
    public QuizRepository repository;

    @Autowired
    public UserService userService;

    @Autowired
    public CompletedQuizService completedQuizService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getQuiz(@PathVariable(value = "id") int id) {
        try {
            Quiz quiz = repository.findById(id).get();
            return new ResponseEntity<>(quiz, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("404 (Not found)", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> addQuiz(@Valid @RequestBody Quiz quiz) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            quiz.setUser(user);
            //System.out.println(user.getEmail());
            repository.save(quiz);
            return new ResponseEntity<>(quiz, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("400 (Bad request)", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Object> postAnswer(@PathVariable(value = "id") int id, @RequestBody Answer answer) throws IOException {
        Quiz quiz = null;
        try {
            quiz = repository.findById(id).get();
            if (compareLists(quiz.getAnswer(), answer.getAnswer())) {
                CompletedQuiz completedQuiz = new CompletedQuiz();
                completedQuiz.setId(quiz.getId());
                completedQuiz.setUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
                completedQuiz.setCompletedAt(LocalDateTime.now());
                completedQuizService.save(completedQuiz);
                return new ResponseEntity<>("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}", HttpStatus.OK);
            }
        } catch (ValidationException e) {
            return new ResponseEntity<>("404 (Not found)", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Integer id) {
        try {
            Quiz quiz = repository.findById(id).get();
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (quiz.getUser().getId() == user.getId()) {
                repository.delete(quiz);
                return new ResponseEntity<>("204 (No content)", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("403 (Forbidden)", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("404 (Not found)", HttpStatus.NOT_FOUND);
        }
    }

    public boolean compareLists(List<Integer> first, List<Integer> second) {
        boolean isFirstNull = first == null || first.size() == 0;
        boolean isSecondNull = second == null || second.size() == 0;
        if (isFirstNull && isSecondNull) {
            return true;
        } else if (!isFirstNull && isSecondNull) {
            return false;
        } else if (isFirstNull && !isSecondNull) {
            return false;
        } else if (!isFirstNull && !isSecondNull) {
            Collections.sort(first);
            Collections.sort(second);
            if (first.size() != second.size()) {
                return false;
            } else {
                for (int i = 0; i < first.size(); i++) {
                    if (first.get(i) != second.get(i)) {
                        return false;
                    }
                }
            }
        }
       return true;
    }

    @GetMapping
    public ResponseEntity<Page<Quiz>> pageList(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "id") String sortBy) {
        Page<Quiz> pagedResult = findAll(page, pageSize, sortBy);
        //System.out.println("@RequestParam page = "  + page);
        return new ResponseEntity<Page<Quiz>>(pagedResult, new HttpHeaders(), HttpStatus.OK);
    }

    public Page<Quiz> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Quiz> pagedResult = repository.findAll(paging);
        return pagedResult;
    }


    @GetMapping("/completed")
    public ResponseEntity<Page<CompletedQuiz>> pageCompleted(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "id") String sortBy) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Page<CompletedQuiz> pagedResult = completedQuizService.findAll(page, pageSize, sortBy, username);
        //System.out.println("@RequestParam page = "  + page);
        return new ResponseEntity<Page<CompletedQuiz>>(pagedResult, new HttpHeaders(), HttpStatus.OK);
    }


}

