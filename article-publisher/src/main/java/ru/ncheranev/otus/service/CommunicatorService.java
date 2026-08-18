package ru.ncheranev.otus.service;

import ru.ncheranev.otus.dto.CommunicatorDto;

public interface CommunicatorService {
    void sendMessage(CommunicatorDto dto);
}
