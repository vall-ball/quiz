package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Integer> {
    @Query("SELECT q FROM CompletedQuiz q WHERE q.username = ?1 ORDER BY completedAt DESC")
    Page<CompletedQuiz> findAllQuizByUserPagination(String username, Pageable pageable);

}
