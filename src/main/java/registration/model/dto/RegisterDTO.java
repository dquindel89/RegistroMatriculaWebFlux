package registration.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import registration.model.Course;
import registration.model.Student;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDTO {


    private String id;

    private LocalDateTime registrationDate;

    private Student student;

    private List<Course> listCourses;

    private Boolean state;
}
