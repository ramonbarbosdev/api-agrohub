package com.api_agrohub.domain.usuario.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api_agrohub.domain.usuario.model.Role;

import jakarta.transaction.Transactional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNomeRole(String nomeRole);

    @Transactional
    @Modifying
    @Query(value= "DELETE FROM usuarios_role ur WHERE ur.usuario_id = :usuarioId", nativeQuery = true)
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

}