package pucpr.br.exemplo.veiculo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pucpr.br.exemplo.veiculo.entity.Veiculo;
import pucpr.br.exemplo.veiculo.service.VeiculoService;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;

    @PostMapping
    public Veiculo salvar(Veiculo veiculo){
        return veiculoService.salvar(veiculo);
    }

    @GetMapping
    public List<Veiculo> listar(){
       return veiculoService.listar();
    }

    @DeleteMapping
    public void excluir(Veiculo veiculo){

    }
}
