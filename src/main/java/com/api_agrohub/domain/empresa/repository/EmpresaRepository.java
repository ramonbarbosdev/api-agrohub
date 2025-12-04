package com.api_agrohub.domain.empresa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api_agrohub.domain.empresa.model.Empresa;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query(value = "SELECT CASE WHEN MAX(c.cd_empresa) IS NULL THEN '0' ELSE MAX(c.cd_empresa) END FROM empresa c ", nativeQuery = true)
    Long obterSequencial();

    @Query(value = "SELECT *  FROM empresa b WHERE b.cd_empresa = ?1 limit 1  ", nativeQuery = true)
    Optional<Empresa> verificarCodigoExistente(String codigo);

    @Query(value = "SELECT *  FROM empresa b WHERE b.nm_empresa = ?1 limit 1  ", nativeQuery = true)
    Optional<Empresa> verificarNomeExistente(String nome);

    @Query(value = """
            SELECT e.*
            FROM empresa e
            JOIN usuario_empresa ue ON ue.id_empresa = e.id_empresa
            WHERE  ue.id_usuario = :id_usuario
            AND e.fl_ativo = true;

                                     """, nativeQuery = true)
    List<Empresa> buscarEmpresaPorTenantUsuario(
            @Param("id_usuario") Long id_usuario);

}
