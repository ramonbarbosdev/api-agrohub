package com.api_agrohub.domain.empresa.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_agrohub.domain.empresa.model.Empresa;
import com.api_agrohub.domain.empresa.model.UsuarioEmpresa;
import com.api_agrohub.domain.empresa.repository.EmpresaRepository;
import com.api_agrohub.domain.sistema.service.ValidacaoService;
import com.api_agrohub.domain.usuario.model.Usuario;
import com.api_agrohub.util.TenantUtil;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private ValidacaoService validacaoService;

    public static final Function<Empresa, Long> ID_FUNCTION = Empresa::getId_empresa;

    public static final Function<Empresa, String> SEQUENCIA_FUNCTION = Empresa::getCd_empresa;

    @Transactional(rollbackFor = Exception.class)
    public Empresa salvar(Empresa objeto) throws Exception {

        validarObjeto(objeto);

        return repository.save(objeto);
    }

    public void validarObjeto(Empresa objeto) throws Exception {
        validacaoService.validarCodigoExistente(
                ID_FUNCTION.apply(objeto),
                repository.verificarCodigoExistente(SEQUENCIA_FUNCTION.apply(objeto)),
                ID_FUNCTION);

        if (objeto.getId_tenant() == null || objeto.getId_tenant().isEmpty()) {
            objeto.setId_tenant(TenantUtil.generateTenantId());
        }
    }

    public Empresa buscarPorId(Long id) throws Exception {

        Optional<Empresa> objeto = repository.findById(id);

        return objeto.isPresent() ? objeto.get() : null;
    }

    public List<Empresa> buscarListagemVinculoPorUsuario(Long id_usuario) {
        List<Empresa> list = repository.buscarEmpresaPorTenantUsuario(id_usuario);
        return list == null ? Collections.emptyList() : list;

    }

    public Empresa verificarExistenciaPorNome(String nome) throws Exception {

        Optional<Empresa> objeto = repository.verificarNomeExistente(nome);

        return objeto.isPresent() ? objeto.get() : null;
    }

    public String sequencia() throws Exception {

        String sq_sequencia = validacaoService.gerarSequencia(repository.obterSequencial());

        return sq_sequencia;
    }
}
