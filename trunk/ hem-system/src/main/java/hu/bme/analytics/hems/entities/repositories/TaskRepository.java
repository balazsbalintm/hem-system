package hu.bme.analytics.hems.entities.repositories;

import hu.bme.analytics.hems.entities.Task;

import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
