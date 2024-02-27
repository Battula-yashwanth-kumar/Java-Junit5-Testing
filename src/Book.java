
import javax.persistence.Entity;
import javax.annotation.processing.Generated;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name='book_record')
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long bookID;

    @NonNull
    private String name;

    @NonNull
    private String summary;

    private int reating;
}
