package auth.service.common;

import auth.entity.CodePolicy;
import auth.repository.CodePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CreateCodeService {

    private final CodePolicyRepository codePolicyRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createCodeByCodeId(String codeId) {

        int updated = codePolicyRepository.incrementCurrentNo(codeId);
        if (updated == 0) {
            throw new NoSuchElementException("Not found codePolicy codeId ===" + codeId);
        }

        CodePolicy codePolicy = codePolicyRepository.findById(codeId).orElseThrow(
                () -> new NoSuchElementException("Not found codePolicy codeId ===" + codeId)
        );

        return codePolicy.getFlag() + codePolicy.getCurrentNo();
    }

}
