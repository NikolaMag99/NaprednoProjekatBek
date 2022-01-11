package raf.rs.NwpNikolaDomaci3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raf.rs.NwpNikolaDomaci3.model.Permission;

@Repository
public interface UserPermissionsRepository extends CrudRepository<Permission, Long> { }
