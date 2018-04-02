package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.AccessCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCodeRepository extends CrudRepository<AccessCode, Long> {

}
