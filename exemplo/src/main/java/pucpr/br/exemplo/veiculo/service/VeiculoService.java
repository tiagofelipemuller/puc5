package pucpr.br.exemplo.veiculo.service;

import org.springframework.stereotype.Service;
import pucpr.br.exemplo.veiculo.entity.Veiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class VeiculoService {
    Logger logger = Logger.getLogger(VeiculoService.class.getName());

    public Veiculo salvar(Veiculo veiculo){
        logger.info(veiculo.getPlaca());
        logger.info(veiculo.getModelo());
        return veiculo;
    }

    public List<Veiculo> listar (){
        List<Veiculo> veiculos = new ArrayList<>();
        Veiculo v = new Veiculo();
        v.setPlaca("BBF7831");
        v.setModelo("Golf GTI");
        veiculos.add(v);
        return veiculos;
    }
     public void deletar(Veiculo veiculo){

     }
}
