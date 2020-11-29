package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/quizzes", produces = "application/json")
public class QuizController {

    @Autowired
    public QuizRepository repository;

    @GetMapping
    public List<Quiz> list() {
        return repository.findAll();
    }

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
                return new ResponseEntity<>("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}", HttpStatus.OK);
            }
        } catch (ValidationException e) {
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


}

