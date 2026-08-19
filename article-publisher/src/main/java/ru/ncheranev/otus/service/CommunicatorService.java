package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.CommunicatorDto;

/**
 * Сервис для взаимодействия с каналами передачи данных
 */
public interface CommunicatorService {
    /**
     * Отправить сообщение в канал
     *
     * @param dto данные сообщения
     */
    void sendMessage(CommunicatorDto dto);
}
