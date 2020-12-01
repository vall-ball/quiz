package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "complete_quizes")
public class CompletedQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer cid;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;
    @NotNull
    @Column
    private Integer id;

    private LocalDateTime completedAt;

    public Integer getCid() {
        return cid;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
