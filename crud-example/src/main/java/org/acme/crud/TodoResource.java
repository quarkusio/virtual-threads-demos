package org.acme.crud;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;


@Path("/api")
@RunOnVirtualThread
public class TodoResource {

	private void log() {
		Log.infof("Called on %s", Thread.currentThread());
	}

	@GET
	public List<Todo> getAll() {
		log();
		// pinTheCarrierThread(); // To demonstrate pinning detection
		return Todo.listAll(Sort.by("order"));
	}

	@GET
	@Path("/{id}")
	public Todo getOne(@PathParam("id") Long id) {
		log();
		Todo entity = Todo.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
		}
		return entity;
	}

	@POST
	@Transactional
	public Response create(@Valid Todo item) {
		log();
		item.persist();
		return Response.status(Status.CREATED).entity(item).build();
	}

	@PATCH
	@Path("/{id}")
	@Transactional
	public Response update(@Valid Todo todo, @PathParam("id") Long id) {
		log();
		Todo entity = Todo.findById(id);
		entity.id = id;
		entity.completed = todo.completed;
		entity.order = todo.order;
		entity.title = todo.title;
		entity.url = todo.url;
		return Response.ok(entity).build();
	}

	@DELETE
	@Transactional
	public Response deleteCompleted() {
		log();
		Todo.deleteCompleted();
		return Response.noContent().build();
	}

	@DELETE
	@Transactional
	@Path("/{id}")
	public Response deleteOne(@PathParam("id") Long id) {
		log();
		Todo entity = Todo.findById(id);
		if (entity == null) {
			throw new WebApplicationException("Todo with id of " + id + " does not exist.", Status.NOT_FOUND);
		}
		entity.delete();
		return Response.noContent().build();
	}

	private void pinTheCarrierThread() {
		synchronized (this) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ignored) {
				// For testing purpose only.
			}
		}
	}

}