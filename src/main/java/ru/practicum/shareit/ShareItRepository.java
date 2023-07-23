package ru.practicum.shareit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareItRepository<T extends AbstractModel> extends JpaRepository<T, Long> {
}
