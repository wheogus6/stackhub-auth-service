package auth.repository;

import auth.entity.Admin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import static auth.entity.QAdmin.admin;

public interface AdminRepository extends JpaRepository<Admin, String>, AdminRepositoryCustom {
}

interface AdminRepositoryCustom {

    Admin findByAdminId(String adminId);
}

@RequiredArgsConstructor
class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Admin findByAdminId(String adminId) {
        return query.selectFrom(admin)
                .where(admin.adminId.eq(adminId))
                .fetchOne();
    }
}