package br.ce.rodrigoganchieta.taskbackend.controller;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.rodrigoganchieta.taskbackend.model.Task;
import br.ce.rodrigoganchieta.taskbackend.repo.TaskRepo;
import br.ce.rodrigoganchieta.taskbackend.utils.ValidationException;

public class TaskControllerTest {
	
	@Mock
	private TaskRepo taskRepo;
	
	@InjectMocks
	private TaskController controller;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotSaveTasksDescription() {
		Task all = new Task();
		all.setDueDate(LocalDate.now());
		try {
			controller.save(all);
			Assert.fail("It shouldn't get to that point!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
	}
	
	@Test
	public void You shouldNotSaveTaskWithoutDate() {
		Task all = new Task();
		all.setTask("description");
		try {
			controller.save(all);
			Assert.fail("It shouldn't get to that point!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}
	
	@Test
	public void shouldNotSaveTaskWithPastDate() {
		Task all = new Task();
		all.setTask("description");
		all.setDueDate(LocalDate.of(2010, 01, 01));
		try {
			controller.save(all);
			Assert.fail("It shouldn't get to that point!");
		} catch (ValidationException e) {
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}
	
	@Test
	public void mustSaveTaskSuccessfully() throws ValidationException {
		Task all = new Task();
		all.setTask("description");
		all.setDueDate(LocalDate.now());
		controller.save(all);
		
		Mockito.verify(taskRepo).save(all);
	}
}
