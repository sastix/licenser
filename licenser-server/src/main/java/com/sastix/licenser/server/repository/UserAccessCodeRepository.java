package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.UserAccessCode;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserAccessCodeRepository extends CrudRepository<UserAccessCode, Integer> {
}
