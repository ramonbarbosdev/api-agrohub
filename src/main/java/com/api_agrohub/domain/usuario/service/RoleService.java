package com.api_agrohub.domain.usuario.service;


import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_agrohub.domain.usuario.model.Role;
import com.api_agrohub.domain.usuario.model.RotaPermission;
import com.api_agrohub.domain.usuario.repository.RoleRepository;
import com.api_agrohub.domain.usuario.repository.RotaPermissionRepository;
import com.api_agrohub.util.MestreDetalheUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RotaPermissionRepository rotaPermissionRepository;

    @Transactional(rollbackFor = Exception.class)
    public Role salvar(Role objeto) throws Exception {

        List<RotaPermission> itensRotaPermission = objeto.getItensRotaPermission();

        objeto.setItensRotaPermission(null);

        validarObjeto(objeto);
        objeto = repository.save(objeto);

        salvarRotaPermissionDetalhe(objeto, itensRotaPermission);

        objeto = repository.save(objeto);

        return objeto;
    }

    public void salvarRotaPermissionDetalhe(Role objeto,
            List<RotaPermission> itens) throws Exception {

        MestreDetalheUtils.removerItensGenerico(
                objeto.getId(),
                itens,
                rotaPermissionRepository::findbyIdRole,
                rotaPermissionRepository::deleteById,
                RotaPermission::getId_rotapermission);

        if (itens != null && itens.size() > 0) {
            for (RotaPermission item : itens) {
                item.setId_role(objeto.getId());

                if (item.getId_rotapermission() == null || item.getId_rotapermission() == 0) {
                    item.setId_rotapermission(null);
                }

                validarItemRotaPermissaoObjeto(item, itens, objeto);
                item = rotaPermissionRepository.save(item);
            }
        }

        objeto.setItensRotaPermission(itens);

    }

    public void validarItemRotaPermissaoObjeto(RotaPermission item, List<RotaPermission> itens, Role objeto)
            throws Exception {

        long count = itens.stream()
                .filter(i -> i.getPath().equals(item.getPath()))
                .count();

        if (count > 1) {
            throw new Exception("Não é permitido incluir mais de uma rota: '" + item.getPath()
                    + "'. Está se retindo " + count + " vezes.");
        }
    }

    public void validarObjeto(Role objeto) throws Exception {

        String nomeRole =  converterParaRole(objeto.getNomeRole());

        // 1. Validar se começa com ROLE_
        if (nomeRole == null || !nomeRole.startsWith("ROLE_")) {
            throw new Exception("O nome do papel deve começar com 'ROLE_'. Exemplo: ROLE_GESTAO");
        }

        objeto.setNomeRole(nomeRole);

        Role nomeExistente = repository.findByNomeRole(objeto.getNomeRole());
        if (nomeExistente != null && objeto.getId() == null && objeto.getId() != nomeExistente.getId())
            throw new Exception("Esse papel já existe!");

    }

    public String converterParaRole(String nomeAmigavel) {
    if (nomeAmigavel == null || nomeAmigavel.isBlank()) {
        throw new IllegalArgumentException("O nome do papel não pode ser vazio");
    }

    // 1. Remove acentos
    String normalizado = Normalizer.normalize(nomeAmigavel, Normalizer.Form.NFD)
                          .replaceAll("\\p{M}", "");

    // 2. Remove caracteres inválidos (mantém letras, números e espaço)
    normalizado = normalizado.replaceAll("[^a-zA-Z0-9 ]", "");

    // 3. Substitui espaços por underline e transforma em maiúsculas
    normalizado = normalizado.trim().replaceAll("\\s+", "_").toUpperCase();

    // 4. Adiciona prefixo ROLE_
    return "ROLE_" + normalizado;
}

    @Transactional(rollbackFor = Exception.class)
    public void excluir(Long id) {

        rotaPermissionRepository.deleteMethodByIdMestre(id);
        rotaPermissionRepository.deleteByIdMestre(id);
        repository.deleteById(id);
    }

}
