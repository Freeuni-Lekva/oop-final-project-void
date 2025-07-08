package repository;

import java.util.List;

public interface GenericRepository<GetDto, CreateDto, UpdateDto> {
    GetDto getById(int id);
    List<GetDto> getAll();
    int create(CreateDto entity);
    void update(UpdateDto entity);
    void deleteById(int id);
} 