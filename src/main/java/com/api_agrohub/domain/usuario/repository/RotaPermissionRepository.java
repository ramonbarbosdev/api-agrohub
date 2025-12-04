package com.api_agrohub.domain.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api_agrohub.domain.usuario.model.RotaPermission;

import jakarta.transaction.Transactional;

@Repository
public interface RotaPermissionRepository extends JpaRepository<RotaPermission, Long> {

    @Query(value = "SELECT * FROM rota_permission WHERE id_role = ?1", nativeQuery = true)
    List<RotaPermission> findbyIdRole(Long id);

    @Transactional
    @Modifying
    @Query(value = """
                        delete from rota_permission_methods rpm
                            where id_rotapermission in (
                            select id_rotapermission  FROM rota_permission  WHERE id_role = ?1
            )
                        """, nativeQuery = true)
    void deleteMethodByIdMestre(Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM rota_permission  WHERE id_role = ?1 ", nativeQuery = true)
    void deleteByIdMestre(Long id);

}