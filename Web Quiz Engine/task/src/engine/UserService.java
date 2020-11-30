package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //System.out.println("email = " + email);
        //System.out.println(userRepository.findUserByEmail(email).getAuthorities());
        return userRepository.findUserByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).get();
    }
}
