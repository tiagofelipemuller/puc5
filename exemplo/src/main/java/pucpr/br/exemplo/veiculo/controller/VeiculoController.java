package pucpr.br.exemplo.veiculo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pucpr.br.exemplo.veiculo.entity.Veiculo;
import pucpr.br.exemplo.veiculo.service.VeiculoService;

import java.util.List;

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

    @GetMapping
    public void excluir(Veiculo veiculo){

    }
}
