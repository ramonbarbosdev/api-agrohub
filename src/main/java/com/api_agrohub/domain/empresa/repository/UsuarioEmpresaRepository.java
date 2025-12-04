package com.api_agrohub.domain.empresa.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api_agrohub.domain.empresa.model.UsuarioEmpresa;

import jakarta.transaction.Transactional;

@Repository
public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresa, Long> {

        @Query(value = "SELECT * FROM usuario_empresa WHERE id_usuario = ?1", nativeQuery = true)
        List<UsuarioEmpresa> findbyIdMestre(Long id);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM usuario_empresa  WHERE id_usuario = ?1 ", nativeQuery = true)
        void deleteByIdMestre(Long id);

        @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END " +
                        "FROM UsuarioEmpresa ue " +
                        "WHERE ue.id_usuario = :idUsuario AND ue.id_empresa = :idEmpresa")
        boolean existsByIdUsuarioAndIdEmpresa(@Param("idUsuario") Long idUsuario,
                        @Param("idEmpresa") Long idEmpresa);

        @Query(value = """
                select cast(1 as boolean)  from usuario_empresa ue
                where ue.id_empresa in
                (
                        select id_empresa from empresa where id_tenant = :id_tenant
                )
                and ue.id_usuario = :id_usuario
                        """, nativeQuery = true)
        boolean existsVinculoEmpresaByIdUsuarioAndByIdTenant(@Param("id_tenant") String id_tenant,
                        @Param("id_usuario") Long id_usuario);

}