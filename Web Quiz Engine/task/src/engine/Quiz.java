package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    @Column
    private String title;
    @NotBlank
    @Column
    private String text;
    @NotNull
    @NotEmpty
    @Size(min = 2)
    private String[] options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    @CollectionTable(
            name="answer",
            joinColumns=@JoinColumn(name="quiz_id")
    )
    @OrderColumn(name="sequence")
    private List<Integer> answer = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }
}
