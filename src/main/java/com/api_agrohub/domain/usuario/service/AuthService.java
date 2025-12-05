package com.api_agrohub.domain.usuario.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.api_agrohub.enums.TipoRole;
import com.api_agrohub.context.TenantContext;
import com.api_agrohub.domain.empresa.model.Empresa;
import com.api_agrohub.domain.empresa.model.UsuarioEmpresa;
import com.api_agrohub.domain.empresa.service.EmpresaService;
import com.api_agrohub.domain.usuario.dto.AuthLoginDTO;
import com.api_agrohub.domain.usuario.model.Role;
import com.api_agrohub.domain.usuario.model.Usuario;
import com.api_agrohub.domain.usuario.repository.RoleRepository;
import com.api_agrohub.domain.usuario.repository.UsuarioRepository;
import com.api_agrohub.security.JWTTokenAutenticacaoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    @Autowired
    private JWTTokenAutenticacaoService jwtTokenAutenticacaoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioOnlineService onlineService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Map efetuarLogin(AuthLoginDTO obj, HttpServletResponse response, HttpServletRequest request)
            throws Exception {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            throw new Exception("Token temporário ausente!");
        }

        Long idUsuario = jwtTokenAutenticacaoService.extractLogin(authHeader);

        Usuario objeto = usuarioService.obterPorId(idUsuario);

        String idTenant = obj.getId_tenant();
        Boolean isAreaDev = obj.getIsAreaDev();
        String login = objeto.getLogin();
        validarLogin(objeto, idTenant);

        String finalToken = jwtTokenAutenticacaoService.addAuthentication(response, login, idTenant);

        onlineService.adicionarUsuario(login);
        messagingTemplate.convertAndSend("/topic/online", "update");

        return Map.of(
                "login", login,
                "nome", objeto.getNome(),
                "role", objeto.getRoles().iterator().next().getNomeRole(),
                "token", finalToken,
                "isAreaDev", isAreaDev != null && isAreaDev ? true : false,
                "img", objeto.getImg() == null ? "" : objeto.getImg());

    }

    public Map obterEmpresaVinculada(String login, String senha)
            throws Exception {

        var usernamePassword = new UsernamePasswordAuthenticationToken(login, senha);

        var auth = this.authenticationManager.authenticate(usernamePassword);

        if (auth == null || !auth.isAuthenticated()) {
            throw new Exception("Usuário ou senha inválidos!");
        }

        Usuario objeto = repository.findUserByLogin(login);

        if (objeto == null) {
            throw new Exception("Usuário ou senha inválidos!");
        }

        List<Empresa> list = empresaService.buscarListagemVinculoPorUsuario(objeto.getId());

        String tempToken = jwtTokenAutenticacaoService.addAuthenticationSemTenant(login);

        return Map.of(
                "tenants", list,
                "tempToken", tempToken,
                "role", objeto.getRoles().iterator().next().getNomeRole());

    }

    public Map efetuarCadastro(String login, String senha, String nome, HttpServletResponse response) throws Exception {

        Map<String, String> erros = validarCadastro(login, senha, nome);
        if (erros != null) {
            return erros;
        }

        Usuario objeto = new Usuario();
        objeto.setLogin(login);
        objeto.setNome(nome);
        objeto.setSenha(senha);

        criarEmpresaBase(objeto);
        criarRoleDev(objeto);

        Usuario usuarioSalvo = usuarioService.salvar(objeto);

        return Map.of("usuario", usuarioSalvo, "message", "Usuário criado com sucesso!");

    }

    public void validarLogin(Usuario objeto, String idTenant) throws Exception {
        String TenantIdContext = TenantContext.getTenantId();

        if (objeto == null) {
            TenantContext.clear();
            throw new Exception("Usuário ou senha inválidos!");
        }

        if (idTenant == null || idTenant.isEmpty()) {
            TenantContext.clear();
            throw new Exception("Organização precisa ser informado!");
        }

        boolean existeVinculo = usuarioService.existsVinculoEmpresaByIdUsuarioAndByIdTenant(idTenant, objeto.getId());

        if (!existeVinculo) {
            TenantContext.clear();
            throw new Exception("Usuário não está vinculado ao inquilino (Tenant)!");
        }

        // boolean tenantValido = false;

        // for (UsuarioEmpresa ue : objeto.getItensUsuarioEmpresa()) {
        // Empresa empresa = empresaService.buscarPorId(ue.getId_empresa());
        // if (empresa != null && empresa.getId_tenant().equalsIgnoreCase(idTenant)) {
        // tenantValido = true;
        // break;
        // }
        // }
        // if (!tenantValido) {
        // throw new Exception("Inquilino (Tenant) inválido para o usuário.");
        // }

    }

    public Map<String, String> validarCadastro(String login, String senha, String nome) {
        if (login.isEmpty()) {
            return Map.of("message", "O Login não pode ser vazio!");

        }
        if (nome.isEmpty()) {
            return Map.of("message", "O Nome não pode ser vazio!");

        }
        if (senha.isEmpty()) {
            return Map.of("message", "A Senha não pode ser vazio!");
        }

        if (repository.findUserByLogin(login) != null) {
            return Map.of("message", "Usuário já existe!");
        }
        return null;
    }

    public void criarEmpresaBase(Usuario objeto) throws Exception {

        String nomeBase = "Desenvolvimento";
        Empresa empresa = empresaService.verificarExistenciaPorNome(nomeBase);
        if (empresa == null) {
            empresa = new Empresa();
            empresa.setNm_empresa(nomeBase);
            empresa.setFl_ativo(true);
            empresa.setCd_empresa(empresaService.sequencia());
            empresa = empresaService.salvar(empresa);

        }

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
        usuarioEmpresa.setId_usuario(objeto.getId());
        usuarioEmpresa.setId_empresa(empresa.getId_empresa());
        objeto.getItensUsuarioEmpresa().add(usuarioEmpresa);
    }

    public void criarRoleDev(Usuario objeto) throws Exception {
        Role roleUser = roleRepository.findByNomeRole(TipoRole.ROLE_DEV.name());
        if (roleUser == null) {
            roleUser = new Role();
            roleUser.setNomeRole(TipoRole.ROLE_DEV.name());
            roleRepository.save(roleUser);
        }

        objeto.getRoles().clear();
        objeto.getRoles().add(roleUser);

    }

    public Boolean logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            onlineService.removerUsuario(auth.getName());
            messagingTemplate.convertAndSend("/topic/online", "update");
            return true;
        }

        return false;
    }

}
