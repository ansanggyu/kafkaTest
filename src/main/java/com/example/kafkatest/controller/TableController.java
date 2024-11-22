package com.example.kafkatest.controller;

import com.example.kafkatest.entity.TableEntity;
import com.example.kafkatest.kafka.KafkaProducer;
import com.example.kafkatest.repository.TableRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/tables")
public class TableController {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping
    public TableEntity createTable(@RequestBody TableEntity tableEntity) {
        // 1. MariaDB에 데이터 저장
        TableEntity savedEntity = tableRepository.save(tableEntity);

        // 2. Kafka 메시지 전송
        kafkaProducer.sendMessage(savedEntity);

        return savedEntity;
    }

    @GetMapping
    public List<TableEntity> getAll(){
        String savedEntity = "겟을 실행해버렸음.";
        kafkaProducer.sendMessage(savedEntity);

        return tableRepository.findAll();

    }

    @GetMapping("/{id}")
    public TableEntity getById(@PathVariable Long id){
        return tableRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public TableEntity update(@PathVariable Long id, @RequestBody TableEntity tableEntity){
        return tableRepository.findById(id).map(entity -> {
            entity.setName(tableEntity.getName());
            entity.setDescription(tableEntity.getDescription());
            return tableRepository.save(entity);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        tableRepository.deleteById(id);
    }
}
