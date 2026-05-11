package auth.repository;

import auth.entity.CodePolicy;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CodePolicyRepository extends JpaRepository<CodePolicy, String> {

    @Modifying
    @Query("UPDATE CodePolicy c SET c.currentNo = c.currentNo + 1 WHERE c.codeId = :codeId")
    int incrementCurrentNo(@Param("codeId") String codeId);

}
