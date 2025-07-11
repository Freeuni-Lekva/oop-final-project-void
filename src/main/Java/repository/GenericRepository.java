package repository;

import java.util.List;

public interface GenericRepository<Entity> {
    Entity getById(long id);

    List<Entity> getAll();

    void create(Entity entity);

    void update(Entity entity);

    void deleteById(long id);
}
