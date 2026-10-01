package com.example.greeting.service;

import com.example.greeting.entity.Counter;
import com.example.greeting.repository.CounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CounterService {

    private final CounterRepository repository;

    public CounterService(CounterRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public long increment() {
        Counter counter = repository.findById(1L)
                .orElseGet(() -> new Counter(1L, 0L));

        counter.setValue(counter.getValue() + 1);

        return repository.save(counter).getValue();
    }

    public long get() {
        return repository.findById(1L)
                .map(Counter::getValue)
                .orElse(0L);
    }
}