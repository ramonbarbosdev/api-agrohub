package com.api_agrohub.controller.empresa;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_agrohub.controller.base.BaseController;
import com.api_agrohub.domain.empresa.model.PlanoAssinatura;
import com.api_agrohub.domain.empresa.service.PlanoAssinaturaService;
import com.api_agrohub.domain.usuario.dto.UsuarioDTO;
import com.api_agrohub.domain.usuario.service.UsuarioOnlineService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/usuarioonline", produces = "application/json")
@Tag(name = "Plano de assinatura")
public class UsuarioOnlineController {
    @Autowired
    private UsuarioOnlineService usuarioOnlineService;


    @GetMapping("/")
    public ResponseEntity<?> listarUsuariosOnline() {
        return ResponseEntity.ok(usuarioOnlineService.obterInformacoesUsuario());
    }
}