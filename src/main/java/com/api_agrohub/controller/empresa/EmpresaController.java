package com.api_agrohub.controller.empresa;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_agrohub.controller.base.BaseControllerJpa;
import com.api_agrohub.domain.empresa.model.Empresa;
import com.api_agrohub.domain.empresa.service.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/empresa")
@Tag(name = "Empresas")
public class EmpresaController extends BaseControllerJpa<Empresa, Long> {

    @Autowired
    private EmpresaService service;

    public EmpresaController(JpaRepository<Empresa, Long> repository) {
        super(repository);
    }

    @PostMapping(value = "/cadastrar", produces = "application/json")
    public ResponseEntity<?> cadastrar(@RequestBody Empresa objeto) throws Exception {

        service.salvar(objeto);
        return new ResponseEntity<>(Map.of("message", "Registro salvo com sucesso"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/sequencia", produces = "application/json")
    @Operation(summary = "Gerar sequencia")
    public ResponseEntity<?> obterSequencia() throws Exception {

        String resposta = service.sequencia();

        return new ResponseEntity<>(Map.of("sequencia", resposta), HttpStatus.OK);
    }

}
