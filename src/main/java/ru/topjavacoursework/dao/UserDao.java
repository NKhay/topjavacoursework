package ru.topjavacoursework.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.User;

import java.util.List;

//import static ModelUtil.SORT_BY_NAME_EMAIL;


@Repository
@Transactional(readOnly = true)
public interface UserDao extends JpaRepository<User, Integer> {

    @Transactional
    @Override
    User save(User s);

    @Override
    User getOne(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    default List<User> findAll() {
        return findAll(Sort.by(Sort.Direction.DESC, "id", "name"));
    }

    User findByEmail(String email);

}
