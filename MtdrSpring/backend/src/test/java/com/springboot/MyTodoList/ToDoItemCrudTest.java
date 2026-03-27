package com.springboot.MyTodoList;

import com.springboot.MyTodoList.dto.ToDoItemRequest;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ToDoItemCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @BeforeEach
    void setUp() {
        toDoItemRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("GET /api/todos - Listar todos (vacío)")
    void testListAllEmpty() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/todos - Crear un ToDoItem")
    void testCreateTodo() throws Exception {
        ToDoItemRequest request = new ToDoItemRequest();
        request.setDescription("Tarea de prueba");
        request.setDone(false);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("Tarea de prueba")))
                .andExpect(jsonPath("$.done", is(false)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.creationTs", notNullValue()));
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/todos - Listar después de crear")
    void testListAfterCreate() throws Exception {
        // Crear un item primero
        ToDoItem item = new ToDoItem();
        item.setDescription("Item para listar");
        item.setCreation_ts(OffsetDateTime.now());
        item.setDone(false);
        toDoItemRepository.save(item);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("Item para listar")));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/todos/{id} - Obtener por ID")
    void testGetById() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setDescription("Item por ID");
        item.setCreation_ts(OffsetDateTime.now());
        item.setDone(false);
        ToDoItem saved = toDoItemRepository.save(item);

        mockMvc.perform(get("/api/todos/" + saved.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Item por ID")))
                .andExpect(jsonPath("$.id", is(saved.getID())));
    }

    @Test
    @Order(5)
    @DisplayName("GET /api/todos/{id} - 404 por ID inexistente")
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/todos/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("no encontrado")));
    }

    @Test
    @Order(6)
    @DisplayName("PUT /api/todos/{id} - Actualizar ToDoItem")
    void testUpdateTodo() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setDescription("Original");
        item.setCreation_ts(OffsetDateTime.now());
        item.setDone(false);
        ToDoItem saved = toDoItemRepository.save(item);

        ToDoItemRequest updateRequest = new ToDoItemRequest();
        updateRequest.setDescription("Actualizado");
        updateRequest.setDone(true);

        mockMvc.perform(put("/api/todos/" + saved.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Actualizado")))
                .andExpect(jsonPath("$.done", is(true)));
    }

    @Test
    @Order(7)
    @DisplayName("PUT /api/todos/{id} - 404 al actualizar ID inexistente")
    void testUpdateNotFound() throws Exception {
        ToDoItemRequest updateRequest = new ToDoItemRequest();
        updateRequest.setDescription("No existe");
        updateRequest.setDone(false);

        mockMvc.perform(put("/api/todos/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /api/todos/{id} - Eliminar ToDoItem")
    void testDeleteTodo() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setDescription("Para eliminar");
        item.setCreation_ts(OffsetDateTime.now());
        item.setDone(false);
        ToDoItem saved = toDoItemRepository.save(item);

        mockMvc.perform(delete("/api/todos/" + saved.getID()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/api/todos/" + saved.getID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /api/todos/{id} - 404 al eliminar ID inexistente")
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/todos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    @DisplayName("POST /api/todos - Validación: descripción vacía retorna 400")
    void testCreateValidationError() throws Exception {
        ToDoItemRequest request = new ToDoItemRequest();
        request.setDescription(""); // vacía
        request.setDone(false);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
