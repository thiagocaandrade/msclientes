package com.github.thiago.msclientes.api;

import com.github.thiago.msclientes.domain.Cliente;
import com.github.thiago.msclientes.domain.dto.ClienteSaveRequest;
import com.github.thiago.msclientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/status")
    public String status(){
        log.info("Obtendo status do microservice de clientes");
        return "Sucesso";
    }


    @PostMapping
    public ResponseEntity save(@RequestBody ClienteSaveRequest request){
        Cliente cliente = request.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();

    }

    @GetMapping
    public ResponseEntity dadosCliente(@RequestParam("cpf") String cpf){
        Optional<Cliente> cliente = clienteService.getByCpf(cpf);

        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(cliente);
    }
}
