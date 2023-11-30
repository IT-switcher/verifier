package com.isadounikau.sqiverifier.service.dto.task;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.isadounikau.sqiverifier.domain.Task} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskCreateRequestDTO implements Serializable {

    @NotNull
    private String text;

    @NotNull
    private String answer;

    @NotNull
    private String title;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskCreateRequestDTO taskDTO = (TaskCreateRequestDTO) o;
        return Objects.equals(text, taskDTO.text) && Objects.equals(answer, taskDTO.answer) && Objects.equals(title, taskDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answer, title);
    }

    @Override
    public String toString() {
        return "TaskCreateRequestDTO{" +
            ", text='" + text + '\'' +
            ", answer='" + answer + '\'' +
            ", title='" + title + '\'' +
            '}';
    }
}
