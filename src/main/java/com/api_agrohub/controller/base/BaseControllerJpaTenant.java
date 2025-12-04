package com.api_agrohub.controller.base;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api_agrohub.context.TenantContext;
import com.api_agrohub.domain.sistema.repository.BaseRepository;
import com.api_agrohub.domain.sistema.repository.GenericSpecification;
import com.api_agrohub.domain.sistema.service.ValidacaoService;

public abstract class BaseControllerJpaTenant<T, ID> {

    protected final BaseRepository<T, ID> repository;

    @Autowired
    private ValidacaoService validacaoService;

    public BaseControllerJpaTenant(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Método opcional — controllers NÃO são obrigados a sobrescrever.
     */
    protected List<String> getSearchableFields() {
        return Collections.emptyList();
    }

    @GetMapping("/listar")
    public Page<T> listar(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        String tenantId = TenantContext.getTenantId();

        boolean temCamposPesquisa = !getSearchableFields().isEmpty();

        if (!temCamposPesquisa) {
            return repository.findByIdTenant(tenantId, pageable);
        }

        if (repository instanceof JpaSpecificationExecutor) {

            @SuppressWarnings("unchecked")
            JpaSpecificationExecutor<T> specRepo = (JpaSpecificationExecutor<T>) repository;

            var spec = new GenericSpecification<T>(search, getSearchableFields())
                    .and((root, query, cb) -> cb.equal(root.get("idTenant"), tenantId));

            return specRepo.findAll(spec, pageable);
        }

        return repository.findByIdTenant(tenantId, pageable);
    }

    @GetMapping("/")
    public ResponseEntity<List<T>> obterTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<T>> obterPorId(@PathVariable ID id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<?> cadastrar(@RequestBody T objeto) throws Exception {
        repository.save(objeto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Registro salvo com sucesso"));
    }

    @PutMapping("/")
    public ResponseEntity<T> atualizar(@RequestBody T objeto) throws Exception {
        return ResponseEntity.ok(repository.save(objeto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) throws Exception {
        repository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Registro deletado com sucesso"));
    }
}
