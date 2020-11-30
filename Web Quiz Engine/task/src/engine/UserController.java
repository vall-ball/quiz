package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/register", produces = "application/json")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        try {
            userService.save(user.toUser(passwordEncoder));
            //System.out.println(user.getEmail());
            return new ResponseEntity<>("200 (OK)", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("400 (Bad request)", HttpStatus.BAD_REQUEST);
        }
    }
}
