package br.ce.rodrigoganchieta.taskbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ce.rodrigoganchieta.taskbackend.model.Task;

public interface TaskRepo extends JpaRepository<Task, Long>{

}
