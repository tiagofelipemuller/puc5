package pucpr.br.exemplo.veiculo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pucpr.br.exemplo.veiculo.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {
}
