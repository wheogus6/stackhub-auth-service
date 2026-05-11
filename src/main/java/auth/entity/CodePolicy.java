package auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "code_policy")
public class CodePolicy {

    @Id
    @Column(name = "code_id")
    private String codeId;

    @Column(name = "flag")
    private String flag;

    @Column(name = "current_no")
    private Long currentNo;


}
