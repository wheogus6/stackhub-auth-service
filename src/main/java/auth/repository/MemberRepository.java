package auth.repository;

import auth.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import static auth.entity.QMember.member;

public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
}


interface MemberRepositoryCustom {

    Member findByMemberId(String memberId);
}

@RequiredArgsConstructor
class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;


    @Override
    public Member findByMemberId(String memberId) {
        return query.selectFrom(member)
                .where(member.memberId.eq(memberId))
                .fetchOne();
    }
}
