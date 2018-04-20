package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.PackageAccessCode;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface PackageAccessCodeRepository extends CrudRepository<PackageAccessCode, Integer> {

}
