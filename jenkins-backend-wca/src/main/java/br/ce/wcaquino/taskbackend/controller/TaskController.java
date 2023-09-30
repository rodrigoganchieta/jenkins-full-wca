package br.ce.rodrigoganchieta.taskbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ce.rodrigoganchieta.taskbackend.model.Task;
import br.ce.rodrigoganchieta.taskbackend.repo.TaskRepo;
import br.ce.rodrigoganchieta.taskbackend.utils.DateUtils;
import br.ce.rodrigoganchieta.taskbackend.utils.ValidationException;

@RestController
@RequestMapping(value ="/all")
public class TaskController {

	@Autowired
	private TaskRepo allRepo;
	
	@GetMapping
	public List<Task> findAll() {
		return allRepo.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Task> save(@RequestBody Task all) throws ValidationException {
		if(all.getTask() == null || all.getTask() == "") {
			throw new ValidationException("Fill the task description");
		}
		if(all.getDueDate() == null) {
			throw new ValidationException("Fill the due date");
		}
		if(!DateUtils.isEqualOrFutureDate(all.getDueDate())) {
			throw new ValidationException("Due date must not be in past");
		}
		Task saved = allRepo.save(all);
		return new ResponseEntity<Task>(saved, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		allRepo.deleteById(id);
	}
}
