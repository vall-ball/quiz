package engine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/quizzes", produces = "application/json")
public class QuizController {

    private List<Quiz> quizList = new ArrayList<>();

    @GetMapping
    public List<Quiz> list() {
        return quizList;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getQuiz(@PathVariable(value = "id") int id) {
        Quiz quiz;
        try {
            quiz = quizList.get(id);
        } catch (Exception e) {
            return new ResponseEntity<>("404 (Not found)", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addQuiz(@Valid @RequestBody Quiz quiz) {
        try {
            quizList.add(quiz);
            quiz.setId(quizList.indexOf(quiz));
        } catch (Exception e) {
            return new ResponseEntity<>("400 (Bad request)", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<Object> postAnswer(@PathVariable(value = "id") int id, @RequestBody Answer answer) throws IOException {
        Quiz quiz = null;
        try {
            quiz = quizList.get(id);
            if (compareArrays(quiz.getAnswer(), answer.getAnswer())) {
                return new ResponseEntity<>("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}", HttpStatus.OK);
            }
        } catch (ValidationException e) {
            FileWriter fw = new FileWriter("D:\\programming\\1.txt");
            //fw.write();
            fw.write(" " + (answer== null) + (quiz==null) + " " + id + " " + quizList.get(id).getAnswer().length);
            fw.close();
            return new ResponseEntity<>("404 (Not found)", HttpStatus.NOT_FOUND);
        }
    }

    public void sort(int[] array) {
        // i - номер прохода
        for (int i = 0; i < array.length - 1; i++) {
            // внутренний цикл прохода
            for (int j = array.length - 1; j > i; j--) {
                if (array[j - 1] > array[j]) {
                    int tmp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    public boolean compareArrays(int[] first, int[] second) {
        boolean isFirstNull = first == null || first.length == 0;
        boolean isSecondNull = second == null || second.length == 0;
        if (isFirstNull && isSecondNull) {
            return true;
        } else if (!isFirstNull && isSecondNull) {
            return false;
        } else if (isFirstNull && !isSecondNull) {
            return false;
        } else if (!isFirstNull && !isSecondNull) {
            sort(first);
            sort(second);
            if (first.length != second.length) {
                return false;
            } else {
                for (int i = 0; i < first.length; i++) {
                    if (first[i] != second[i]) {
                        return false;
                    }
                }
            }
        }
       return true;
    }


}

